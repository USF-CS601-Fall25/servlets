package jdbc.register;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.util.Random;

public class DatabaseHandler {

    private static DatabaseHandler dbHandler = new DatabaseHandler();
    private String uri = null; // uri to connect to mysql using jdbc
    private String user, password;
    private Random random = new Random(); // used in password  generation

    /**
     * DataBaseHandler is a singleton, we want to prevent other classes
     * from creating objects of this class using the constructor
     * Expects you to store USER, PASSWORD, and the name of the database DB in environmental variables
     */
    private DatabaseHandler() {
        user = System.getenv("USER");
        password = System.getenv("PASSWORD");
        String database = System.getenv("DB");
        if (user == null || user.isEmpty() || password == null || password.isEmpty() || database == null || database.isEmpty()) {
            System.out.println("Please provide username and password");
            return;
        }
        uri = "jdbc:mysql://localhost:3306/" + database + "?useSSL=false&allowPublicKeyRetrieval=true";
        System.out.println(uri);
        System.out.println(user);
    }

    /**
     * Returns the instance of the database handler.
     *
     * @return instance of the database handler
     */
    public static DatabaseHandler getInstance() {
        return dbHandler;
    }

    public void createTable() {
        Statement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, user, password)) {
            System.out.println("dbConnection successful");
            statement = dbConnection.createStatement();
            statement.execute(PreparedStatements.CREATE_USER_TABLE);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public boolean checkIfTableExists(String tableName) {
        try (Connection dbConnection = DriverManager.getConnection(uri, user, password)) {
            PreparedStatement statement = dbConnection.prepareStatement(PreparedStatements.DOES_TABLE_EXIST);
            statement.setString(1, tableName);
            try (ResultSet rs = statement.executeQuery()) {
                boolean exists = rs.next();
                if (exists) {
                    System.out.println("Table exists!");
                    return true;
                } else {
                    System.out.println("Table does NOT exist.");
                    return false;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }


    /**
     * Returns the hex encoding of a byte array.
     * Courtesy of Prof. Engle.
     * @param bytes - byte array to encode
     * @param length - desired length of encoding
     * @return hex encoded byte array
     */
    public static String encodeHex(byte[] bytes, int length) {
        BigInteger bigint = new BigInteger(1, bytes);
        String hex = String.format("%0" + length + "X", bigint);

        assert hex.length() == length;
        return hex;
    }

    /**
     * Calculates the hash of a password and salt using SHA-256.
     * Courtesy of Prof. Engle.
     * @param password - password to hash
     * @param salt - salt associated with user
     * @return hashed password
     */
    public static String getHash(String password, String salt) {
        String salted = salt + password;
        String hashed = salted;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salted.getBytes());
            hashed = encodeHex(md.digest(), 64);
        }
        catch (Exception ex) {
            System.out.println(ex);
        }

        return hashed;
    }

    /**
     * Registers a new user, placing the username, password hash, and
     * salt into the database.
     * Courtesy of Prof.Engle.
     * @param newUser - username of new user
     * @param newPass - password of new user
     */
    public void registerUser(String newUser, String newPass) {
        // Generate salt
        // FILL IN CODE: First, we need to check if the username exists already
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);

        String usersalt = encodeHex(saltBytes, 32); // salt
        String passhash = getHash(newPass, usersalt); // hashed password

        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, user, password)) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.REGISTER_SQL);
                statement.setString(1, newUser);
                statement.setString(2, passhash);
                statement.setString(3, usersalt);
                statement.executeUpdate();
                statement.close();
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * FILL IN CODE in this method that is used to login the user
     * Logs in the user if the username and password are correct
     * @param username
     * @param password
     * @return true if login was successful and false otherwise
     */
    public boolean authenticateUser(String username, String password) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, user, password)) {
            statement = connection.prepareStatement(PreparedStatements.AUTH_SQL);
            // FILL IN CODE:
            String usersalt = "" ; // get salt from the database
            // Write a helper method  getSalt that uses SALT_SQL statement to get salt from the database

            String passhash = getHash(password, usersalt); // combine password with salt

            // Check if username and password combined with salt match what's in the database
            statement.setString(1, username);
            statement.setString(2, passhash);
            ResultSet results = statement.executeQuery();
            boolean flag = results.next();
            return flag;
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }
}


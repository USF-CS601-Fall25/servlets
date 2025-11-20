package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SimpleDBExample {

    public static void main(String[] args) {
        String user = System.getenv("USER");
        String password = System.getenv("PASSWORD");
        String database = System.getenv("DB");
        if (user == null || user.isEmpty() || password == null || password.isEmpty() || database == null || database.isEmpty()) {
            System.out.println("Provide username and password");
            return;
        }
        String url = "jdbc:mysql://localhost:3306/" + database + "?useSSL=false&allowPublicKeyRetrieval=true";
        System.out.println(url);
        //"jdbc:mysql://localhost:3306/CS601DB?useSSL=false&allowPublicKeyRetrieval=true";

        try(Connection connection = DriverManager.getConnection(url, user, password)) {
            // 1. Open a connection
            // 2. Create a statement
            Statement statement = connection.createStatement();

            // 3. Execute a query
            String sql = "SELECT * FROM students";
            ResultSet rs = statement.executeQuery(sql);

            // 4. Process the result set
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String username = rs.getString("username");
                double gpa = rs.getDouble("GPA");
                System.out.println(id + " | " + name + " | " + username + " | GPA = " + gpa);
            }
            // 5. Cleanup
            rs.close();
            statement.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

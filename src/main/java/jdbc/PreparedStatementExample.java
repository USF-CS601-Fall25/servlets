package jdbc;

import java.sql.*;

/** Shows how to use prepared statements */
public class PreparedStatementExample {

	public static void main(String[] args) {
		String user = System.getenv("USER");
		String password = System.getenv("PASSWORD");
		String database = System.getenv("DB");
		if (user == null || user.isEmpty() || password == null || password.isEmpty() || database == null || database.isEmpty()) {
			System.out.println("Provide username and password");
			return;
		}
		String url = "jdbc:mysql://localhost:3306/" + database + "?useSSL=false&allowPublicKeyRetrieval=true";
		connectToDatabase(url, user, password);

	}

	public static void connectToDatabase(String url, String user, String password) {
		try(Connection connection = DriverManager.getConnection(url, user, password)) {
			PreparedStatement sql = connection.prepareStatement("select * from students where id > ? and GPA >= ?");
			sql.setInt(1, 2);
			sql.setDouble(2, 3.9);

			ResultSet results = sql.executeQuery();
			ResultSetMetaData rsmd = results.getMetaData();
			int columnsNumber = rsmd.getColumnCount(); // number of columns
			while (results.next()) { // iterate over rows
				for (int i = 1; i <= columnsNumber; i++) // go over columns
					System.out.print(results.getString(i) + " ");
				System.out.println();
			}
		}
		catch (Exception e) {
			System.err.println("Unable to connect to the database.");
			System.err.println(e.getMessage());
		}
	}
}

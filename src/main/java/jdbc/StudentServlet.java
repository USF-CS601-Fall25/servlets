package jdbc;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/** Expects that you have a local MySQL running which  has a table students in the database
 *  which contains the id column. (populate your database using students.sql script)
 */
public class StudentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        String user = System.getenv("USER");
        String password = System.getenv("PASSWORD");
        String database = System.getenv("DB");

        if (user == null || user.isEmpty() ||
                password == null || password.isEmpty() ||
                database == null || database.isEmpty()) {
            out.println("Environment variables USER, PASSWORD, DB must be set.");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/" + database +
                "?useSSL=false&allowPublicKeyRetrieval=true";

        // Extract student id from path, like /students/1
        String path = request.getPathInfo();
        if (path == null || path.length() <= 1) {
            out.println("Please specify a student ID as the path parameter");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(path.substring(1));
        } catch (NumberFormatException e) {
            out.println("Invalid ID format.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, name, username, GPA FROM students WHERE id = ?"
            );
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                out.println("Student not found.");
                return;
            }

            // Print result as plain text
            out.println("ID: " + rs.getInt("id"));
            out.println("Name: " + rs.getString("name"));
            out.println("Username: " + rs.getString("username"));
            out.println("GPA: " + rs.getDouble("GPA"));

        } catch (SQLException e) {
            out.println("Database error: " + e.getMessage());
        }
    }
}

package jdbc.register;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

// Expects you to run MySql server locally and store
// USER, PASSWORD, and the name of the database DB in environmental variables.
// Run the server, then go to localhost:8090/register and try registering a new user
// The code will create a "users"  table in your database and add a user there.
public class MyServer {
	public static final int PORT = 8090;

	public static void main(String[] args) throws Exception {
		DatabaseHandler dbHandler = DatabaseHandler.getInstance();
		if (!dbHandler.checkIfTableExists("users")) {
			dbHandler.createTable();
			System.out.println("created a user table ");
		}
		// If you wanted to test DatabaseHandler, you could register users "manually":
		// dbHandler.registerUser("luke", "lukeS1k23w");
		//System.out.println("Registered  luke");

		Server server = new Server(PORT);
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(RegistrationServlet.class, "/register");
		server.setHandler(handler);
		server.start();
		server.join();
	}
}
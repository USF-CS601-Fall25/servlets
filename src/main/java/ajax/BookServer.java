package ajax;

import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class BookServer {

	public static void main(String[] args) {
		Server server = new Server(8080);
		ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		handler.addServlet(BookServlet.class, "/books");

		VelocityEngine velocity = new VelocityEngine();
		velocity.init();
		handler.setAttribute("templateEngine", velocity);
		server.setHandler(handler);

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			System.out.println("Exception occurred while running the server: " + e);
		}

	}

}

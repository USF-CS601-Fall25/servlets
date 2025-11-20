package jdbc;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class StudentServer {
    public static void main(String[] args) {
       Server server = new Server(8000);
       ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
       handler.addServlet(StudentServlet.class, "/students/*");
       server.setHandler(handler);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            System.out.println("Could not start the server");
        }
    }
}

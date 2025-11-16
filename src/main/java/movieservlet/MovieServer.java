package movieservlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import velocity.ShowHotelsServlet;

public class MovieServer {
    public static final int PORT = 6500;
    private Server server;  // Jetty server

    public MovieServer(MovieRepository movieRepository) {
        server = new Server(PORT);
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.addServlet(MovieServlet.class, "/movies"); // when the user goes to http://localhost:6500/movies,
        // MovieServlet will be processing the request

        handler.setAttribute("movieRepository", movieRepository); // MovieServlet would be able to access it
        server.setHandler(handler);
    }

    /**
     * Function that starts the server
     *
     * @throws Exception throws exception if access failed
     */
    public void start() throws Exception {
         try {
            server.start();
            server.join();
        } catch (Exception e) {
            System.out.println("Could not start the server");
        }
    }
}

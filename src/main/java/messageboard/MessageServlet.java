package messageboard;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * An example demonstrating how to create a simple message board using
 * jetty/servlets.
 * Modified from the example of Prof. Engle.
 */
@SuppressWarnings("serial")
public class MessageServlet extends HttpServlet {
	private ConcurrentLinkedQueue<String> messages; // thread-safe, from the
													// concurrent package

	public MessageServlet() {
		super();
		messages = new ConcurrentLinkedQueue<>();

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		PrintWriter out = response.getWriter();
		out.println("<h1>Message Board</h1>");

		// Multiple threads may access messages at once. We use a thread-safe queue to store messages
		for (String message : messages) {
			out.println("<p>" + message + "</p>");
		}

		printForm(request, response);
		out.println("<p>" + "This request was handled by thread " + Thread.currentThread().getName() + "</p>");

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		String username = request.getParameter("username");
		String message = request.getParameter("message");

		username = (username == null || username.equals(""))  ? "anonymous" : username;
		message = message == null ? "" : message;
		username = StringEscapeUtils.escapeHtml4(username);
		message = StringEscapeUtils.escapeHtml4(message);

		String formatted = message  + " posted by " + username + " on " + new Date();
		messages.add(formatted);

		response.sendRedirect(request.getServletPath());
	}

	private static void printForm(HttpServletRequest request, HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();

		out.println("<form action=\"" + request.getServletPath() + "\" method = \"post\">");
		out.println("Name: <br>");
		out.println("<input type=\"text\" name=\"username\"><br>");

		out.println("Message: <br>");
		out.println("<input type=\"text\" name=\"message\">");
		out.println("<br><br>");
		out.println("<input type=\"submit\">");
		out.println("</form>");
	}

}
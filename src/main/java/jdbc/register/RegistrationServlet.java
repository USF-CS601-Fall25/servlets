package jdbc.register;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.io.PrintWriter;

/** Allows users to register.
 */
@SuppressWarnings("serial")
public class RegistrationServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println();
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		//out.printf("<html>%n%n");
		//out.printf("<head><title>%s</title></head>%n", "Form");

		//out.printf("<body>%n");
		printForm(request, response);

		//out.printf("%n</body>%n");
		//out.printf("</html>%n");

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		String usernameParam = request.getParameter("username");
		usernameParam = StringEscapeUtils.escapeHtml4(usernameParam);
		String password = request.getParameter("pass");
		password = StringEscapeUtils.escapeHtml4(password);

		DatabaseHandler dbHandler = DatabaseHandler.getInstance();
		dbHandler.registerUser(usernameParam, password);

		response.getWriter().println("Successfully registered the user " + usernameParam);
	}

	private static void printForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();

		out.println("<form action=\"" + request.getServletPath() + "\" method = \"post\">");
		out.println("Enter your username: <br>");
		out.println("<input type=\"text\" name=\"username\"><br>");
		out.println("Enter your password: <br>");
		out.println("<input type=\"password\" name=\"pass\">");
		out.println("<br><br>");
		out.println("<input type=\"submit\" value=\"Enter\">");
		out.println("</form>");
	}
}
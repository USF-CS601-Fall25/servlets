package cookies;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


/**
 * Demonstrates how to use cookies to track the number of user's visits to the website.
 * Modified from the example of Prof. Engle.
 */
public class NumVisitsTrackingServlet extends HttpServlet {

	@Override
	protected void doGet(
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException {

		if (request.getRequestURI().endsWith("src/main/favicon.ico")) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		Cookie[] cookies = request.getCookies();
		String count = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("numVisits")) {
					count = cookie.getValue();
					count = Integer.toString(Integer.parseInt(count) + 1);
					break;
				}
			}
			if (count == null) {
				count = "1";
			}
			out.println("<p>You have visited this website " + count + " times. </p>");
		}
		response.addCookie(new Cookie("numVisits", count));
	}
}
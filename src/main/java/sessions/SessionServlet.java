package sessions;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Demonstrates how to use the HttpSession class to keep track of the number of visits for each client 
 * (and the date of the last visit).
 * Modified from the example of Prof. Rollins.
 *
 */

@SuppressWarnings("serial")
public class SessionServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(60);

		String visitDate = (String) session.getAttribute("date");
		Integer visitCount = (Integer) session.getAttribute("visitCount");

		if (visitCount == null)
			visitCount = 0;
		visitCount = visitCount + 1;
		session.setAttribute("visitCount", visitCount);

		PrintWriter out = response.getWriter();
		String body = "<p>Hello! You have visited " + visitCount + " time(s).</p>\n";
		if (visitDate != null) {
			body = body + "<p> Your last visit was on " + visitDate + "</p>";
		}

		out.println(body);

		String format = "yyyy-MM-dd hh:mm:ss";
		DateFormat formatter = new SimpleDateFormat(format);
		visitDate = formatter.format(Calendar.getInstance().getTime());
		session.setAttribute("date", visitDate);

		response.setStatus(HttpServletResponse.SC_OK);
		System.out.println(response);
	}

}
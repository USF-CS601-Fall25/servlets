package velocity;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ShowHotelsServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();

		// Get the "name" parameter from the GET request
		// The request may look like this: /hotels?name=Ali
		String name = request.getParameter("name");
		if (name == null || name.isEmpty())
			name = "anonymous";
		name = StringEscapeUtils.escapeHtml4(name);

		VelocityEngine ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
		Template template = ve.getTemplate("templates/hotelsTemplate.html");

		VelocityContext context = new VelocityContext();
		context.put("name", name);

		// usually the data would come from some kind of database. Using a
		// simple ArrayList here instead
		ArrayList<Hotel>  hotels = new ArrayList<>();
		hotels.add(new Hotel("Sheraton Pier 39"));
		hotels.add(new Hotel("Best Western SF Downtown"));
		hotels.add(new Hotel("Marriott SF Airport"));
		context.put("hotels", hotels);

		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		System.out.println(writer);

		out.println(writer);
	}
}
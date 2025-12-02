package ajax;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class BookServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("in doGet");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();

        VelocityEngine ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
        Template template = ve.getTemplate("static/fetchBooks.html");

        VelocityContext context = new VelocityContext();
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        out.println(writer);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("in doPost");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();
        JsonObject obj = createJsonWithBooks();
        out.println(obj);
        System.out.println(obj);
    }

    public JsonObject createJsonWithBooks() {
        // returns a json object
        JsonObject book1 = new JsonObject();
        book1.addProperty("title", "Harry Potter");
        book1.addProperty("author", "Rowling");

        JsonObject book2 = new JsonObject();
        book2.addProperty("title", "Effective Java");
        book2.addProperty("author", "Joshua Bloch");

        JsonObject book3 = new JsonObject();
        book3.addProperty("title", "Elephants can remember");
        book3.addProperty("author", "Agatha Christie");

        JsonArray jarray = new JsonArray();
        jarray.add(book1);
        jarray.add(book2);
        jarray.add(book3);

        JsonObject obj = new JsonObject();
        obj.add("books",  jarray);

        return obj;
    }
}

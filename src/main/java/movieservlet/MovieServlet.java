package movieservlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MovieServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        MovieRepository movieRepository = (MovieRepository) getServletContext().getAttribute("movieRepository");
        String keyword = request.getParameter("search");
        if (keyword == null)
            keyword = "";
        //System.out.println(keyword);
        keyword =  StringEscapeUtils.escapeHtml4(keyword);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        // Displays json with movies that contain this keyword:
        JsonObject moviesObj = new JsonObject();
        JsonArray jsonArr = new JsonArray();
        List<Movie> selectedMovies = movieRepository.getMovies(keyword);
        for (Movie movie: selectedMovies ) {
            if (movie.getName().contains(keyword)) {
                JsonObject oneMovieJsonObj = movie.serializeMovie();
                jsonArr.add(oneMovieJsonObj);
            }
        }
        moviesObj.add("movies", jsonArr);
        out.println(moviesObj);
    }

}

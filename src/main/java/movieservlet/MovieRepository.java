package movieservlet;

import java.util.ArrayList;
import java.util.List;

public class MovieRepository {
    private List<Movie> movies = new ArrayList<>();

    public void addMovie(String title, String director, int year) {
        movies.add(new Movie(title, director, year));
    }

    public List<Movie> getMovies(String keyword) {
        List<Movie> selectedMovies = new ArrayList<>();
        // find movies whose title contains the given keyword
        for (Movie movie: movies) {
            if (movie.getName().contains(keyword)) {
               selectedMovies.add(movie);
            }
        }
        return selectedMovies;
    }
}

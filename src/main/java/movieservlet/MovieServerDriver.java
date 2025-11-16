package movieservlet;

public class MovieServerDriver {
    public static void main(String[] args) {
        MovieRepository movieRepository = new MovieRepository();
        movieRepository.addMovie("Inception", "Christopher Nolan", 2010);
        movieRepository.addMovie("Titanic", "James Cameron", 1997);
        movieRepository.addMovie("The Godfather", "Francis Ford Coppola", 1972);

        MovieServer movieServer = new MovieServer(movieRepository);
        try {
            movieServer.start();
        } catch (Exception e) {
            System.out.println("Errors occurred while running the server " + e);
        }
    }
}

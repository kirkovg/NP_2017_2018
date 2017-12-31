package mk.ukim.finki.np.midterms.second;

import java.util.*;
import java.util.stream.Collectors;

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}


class Movie {
    private String title;
    private int[] ratings;

    public Movie(String title, int[] ratings) {
        this.title = title;
        this.ratings = ratings;
    }

    public String getTitle() {
        return title;
    }

    public int[] getRatings() {
        return ratings;
    }

    public double getAvgRating() {
        return Arrays.stream(ratings).average().getAsDouble();
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings", title, this.getAvgRating(), ratings.length);
    }
}

class MoviesList {
    private List<Movie> movies;

    public MoviesList() {
        movies = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings) {
        movies.add(new Movie(title, ratings));
    }

    public List<Movie> top10ByAvgRating() {
        return movies.stream()
                .sorted(Comparator.comparing(Movie::getAvgRating).reversed().thenComparing(Movie::getTitle))
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoef() {
        return movies.stream()
                .sorted(new CoefficientRatingComparator(getMaxNumRatings()))
                .limit(10)
                .collect(Collectors.toList());
    }


    private int getMaxNumRatings() {
        return movies.stream().mapToInt(movie -> movie.getRatings().length).max().getAsInt();
    }
}

class CoefficientRatingComparator implements Comparator<Movie> {

    private int maxRatings;

    public CoefficientRatingComparator(int maxRatings) {
        this.maxRatings = maxRatings;
    }

    @Override
    public int compare(Movie o1, Movie o2) {
        int coefR = Double.compare(o1.getAvgRating() * o1.getRatings().length / maxRatings,
                o2.getAvgRating() * o2.getRatings().length / maxRatings);
        if (coefR == 0) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
        return -coefR;
    }
}

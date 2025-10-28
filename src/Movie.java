/*******************************************************
 * @file: Movie.java
 * @description: One record from the dataset (IMDB-like).
 *               Comparable by title (case-insensitive),
 *               then by year.
 * @author: Sebastien Pierre
 * @date: October 22, 2025
 *******************************************************/
public class Movie implements Comparable<Movie> {
    private String title;
    private int year;
    private double rating;
    private int votes;

    // default
    public Movie() {
        this("", 0, 0.0, 0);
    }

    // param
    public Movie(String title, int year, double rating, int votes) {
        this.title = title;
        this.year = year;
        this.rating = rating;
        this.votes = votes;
    }

    // copy
    public Movie(Movie other) {
        this(other.title, other.year, other.rating, other.votes);
    }

    // getters
    public String getTitle() { return title; }
    public int getYear() { return year; }
    public double getRating() { return rating; }
    public int getVotes() { return votes; }

    @Override
    public int compareTo(Movie o) {
        int byTitle = this.title.toLowerCase().compareTo(o.title.toLowerCase());
        if (byTitle != 0) return byTitle;
        return Integer.compare(this.year, o.year);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Movie)) return false;
        Movie m = (Movie) obj;
        return this.year == m.year && this.title.equalsIgnoreCase(m.title);
    }

    @Override
    public int hashCode() { //
        return (title == null ? 0 : title.toLowerCase().hashCode()) * 31 + year;
    }

    @Override
    public String toString() {
        return title + " (" + year + ") rating=" + rating + " votes=" + votes;
    }
}

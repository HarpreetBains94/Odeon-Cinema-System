
/**
 * Write a description of class Movie here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Movie {
    private String movieId;
    private static int movieIdCount = 0;
    private String title;
    private static int reviewCount=0;
    private HashMap<String, Integer> reviews = new HashMap<>();
    private ArrayList<Showing> showings;

    /**
     *Creates new Movie object.
     *
     * @param title String, title of movie.
     */
    public Movie(String title) {
        this.title = title.toLowerCase();
        movieIdCount++;
        this.movieId = String.valueOf(movieIdCount);
        this.showings = new ArrayList<>();
    }

    public String getTitle(){
        return this.title;
    }


    public void addShowing(Showing showing){
        showings.add(showing);
    }

    public ArrayList<Showing> getShowings(){
        return showings;
    }

    /**
     * Adds review to review list.
     *
     * @param review
     */


    /**
     * Adds review and rating to reviews HashMap.
     *
     * @param review
     * @param rating
     */
    public void addReviewRating(String review, int rating){
        //to make same review different. This way HashMap wont over write existing key/value pair.
        review = review+reviewCount;
        reviews.put(review, rating);
    }


    /**
     * Calculates average rating from reviews HashMap and returns it.
     *
     * @return
     */
    public double returnAverageRating(){
        double sum=0.00;
        int count = 0;
        for(Integer i:reviews.values()){
            sum+=i;
            count++;
        }
        return (double) (sum/count);
    }

    public String returnReviewsAndRating(){
        DecimalFormat df = new DecimalFormat(".00");
        String string = "\n";
        string+="Title: "+this.title+", Average rating "+ df.format(returnAverageRating())+"\n\n";
        for(String review:reviews.keySet()){
            string+=reviews.get(review)+ " : " + review.substring(0,review.length()-1)+"\n";
        }
        return string;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Movie)){
            return false;
        }
        if(o==this){
            return true;
        }
        Movie movie = (Movie) o;
        if(title.compareToIgnoreCase(movie.getTitle())==0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        return Objects.hash(title);
    }
}

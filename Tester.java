
/**
 * Write a description of class Tester here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.time.LocalDate;

public class Tester {
    private Tester(){}
    public static void test(){
        //Creating Instance of Cinema
        Cinema odeon = new Cinema(6);

        //Creating 5 Movies
        Movie movie1 = new Movie("Shrek");
        Movie movie2 = new Movie("Casablanca");
        Movie movie3 = new Movie("The room");
        Movie movie4 = new Movie("Justice League");
        Movie movie5 = new Movie("Mars Attacks");


        //
        LocalDate dateHolder1 = LocalDate.now();
        LocalDate dateHolder2 = LocalDate.now();

        try {
            dateHolder1 = Cinema.convertStringToDate("01/01/2018");
            dateHolder2 = Cinema.convertStringToDate("01/02/2018");
        }catch (InvalidDateTimeException e){
            System.out.println(e.getMessage());
        }

                /*
        Adding 5 show objects. For this test each movie will have one show
        but I did design the system to handle multiple shows per movie.
         */

        try {
            odeon.addshowing(new Showing(movie1, "Evening", dateHolder1, 1));

            odeon.addshowing(new Showing(movie2, "Night1", dateHolder1, 2));

            odeon.addshowing(new Showing(movie3, "Night2", dateHolder1, 3));

            odeon.addshowing(new Showing(movie4, "Afternoon", dateHolder2, 3));

            odeon.addshowing(new Showing(movie5, "Evening", dateHolder2, 4));
        }catch (InvalidDateTimeException e) {
            e.getMessage();
        }

        //Creating 15 customers
        Customer a = null;
        Customer b = null;
        Customer c = null;
        Customer d = null;
        Customer e = null;
        Customer f = null;
        Customer g = null;
        Customer h = null;
        Customer i = null;
        Customer j = null;
        Customer k = null;
        Customer l = null;
        Customer m = null;
        Customer n = null;
        Customer o = null;

        System.out.println("\nCUSTOMERS BOOKING SEATS:\n");

        /*
        Though showing the available seats was initially going to just be used for the normal system
        I thought it would be useful to also include it here to show visually that the seats are being
        booked. As it shows the available seats it is not evident that a seat has been booked until
        another customer tries to book a seat for the same show.
         */
        try {
            a = new Customer(odeon.getShowing(movie1, "Evening", dateHolder1), "A10",true);
            System.out.println(" ");
            b = new Customer(odeon.getShowing(movie1, "Evening", dateHolder1), "B04",true);
            System.out.println(" ");
            c = new Customer(odeon.getShowing(movie1, "Evening", dateHolder1), "C01",false);
            System.out.println(" ");
            d = new Customer(odeon.getShowing(movie1, "Evening", dateHolder1), "C02",true);
            System.out.println(" ");
            e = new Customer(odeon.getShowing(movie1, "Evening", dateHolder1), "E09",true);
            System.out.println(" ");
            f = new Customer(odeon.getShowing(movie2, "Night1", dateHolder1), "E01",false);
            System.out.println(" ");
            g = new Customer(odeon.getShowing(movie2, "Night1", dateHolder1), "E02",false);
            System.out.println(" ");
            h = new Customer(odeon.getShowing(movie2, "Night1", dateHolder1), "D07",true);
            System.out.println(" ");
            i = new Customer(odeon.getShowing(movie3, "Night2", dateHolder1), "E05",true);
            System.out.println(" ");
            j = new Customer(odeon.getShowing(movie3, "Night2", dateHolder1), "E06",false);
            System.out.println(" ");
            k = new Customer(odeon.getShowing(movie3, "Night2", dateHolder1), "E07",true);
            System.out.println(" ");
            l = new Customer(odeon.getShowing(movie3, "Night2", dateHolder1), "E08",false);
            System.out.println(" ");
            m = new Customer(odeon.getShowing(movie3, "Night2", dateHolder1), "E04",true);
            System.out.println(" ");
            n = new Customer(odeon.getShowing(movie4, "Afternoon", dateHolder2), "C05",false);
            System.out.println(" ");
            o = new Customer(odeon.getShowing(movie5, "Evening", dateHolder2), "C06",true);
        }
        catch (NonValidOptionException ex){
            System.out.println(ex.getMessage());
            System.exit(0);
        }

        //just to separate parts of test.
        System.out.println("\n**************************************************\n");
        System.out.println("CUSTOMER CHANGING BOOKED SEAT:\n");
        try {
            //Changing seat
            System.out.println("a's original seat: " + a.getSeat());
            try {
                a.changeBooking(odeon.getShowing(movie1, "Evening", dateHolder1), "A02");
            } catch (SeatBookError | NonValidOptionException ex) {
                ex.printStackTrace();
            }
            System.out.println("\na's new seat: " + a.getSeat() + "\n");

            //just to separate parts of test.
            System.out.println("\n**************************************************\n");

            //Giving reviews+rating
            movie1.addReviewRating("Good.", 4);
            movie1.addReviewRating("Ok.", 3);
            movie1.addReviewRating("Amazing.", 5);
            movie1.addReviewRating("Shrek is love, Shrek is life.", 4);
            movie1.addReviewRating("Meh.", 2);
            movie2.addReviewRating("Great.", 5);
            movie2.addReviewRating("Decent to a strong 4.", 4);
            movie2.addReviewRating("Pretty good.", 4);
            movie3.addReviewRating("I want the last 2 hours of my life back.", 1);
            movie3.addReviewRating("Bad. Just bad.", 1);
            movie3.addReviewRating("Not that bad.", 3);
            movie3.addReviewRating("So bad its good.", 4);
            movie3.addReviewRating("Is this supposed to be a joke?", 1);
            movie4.addReviewRating("Theres a reason I was the only one in that showing.", 1);
            movie5.addReviewRating("Hot garbage.", 1);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }

        //Printing out results of reports (Ratings and highest grossing)
        System.out.println("REPORTS:\n");
        System.out.println("*Spectators and average rating per movie:\n");
        System.out.println(odeon.returnViewNumbersAndRating());
        System.out.println("*Highest grossing movie:\n");
        System.out.println(odeon.returnHighestGrossingMovie());
        System.out.println("\n*Reviews and ratings per movie:");
        System.out.println(odeon.returnReviewsAndRatings());
    }
}

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

public class CinemaSystem {

    public CinemaSystem(){
    }

    public static void main(String[] args){
        welcome();
    }

    /**
     * Initial startup. User can:
     * - Start normally: How system is likely to be used (String inputs from user rather than hard coded variables).
     * - Start Test: Launches test class. Test class creates a Cinema object with 6 screens, 5 Movie objects, 15 Customer
     * objects. Then changes the seat booking for 1 customer and records 10 reviews. Finally prints reports for
     * top grossing movie, amount grossed and average rating per movie.
     * - Quit: Quits system.
     */
    public static void welcome(){
        Scanner scanner = new Scanner(System.in);
        boolean somethingSelected = false;
        while(!somethingSelected) {
            System.out.println("Welcome to the Odeon Cinema System. Please enter one of the following options:\n" +
                    "'Normal' - Start system normally.\n'Test' - Load test data from test class.\n" +
                    "'Quit' - Quit system.\n...");
            String input = scanner.next().toLowerCase();
            String[] words = input.split(" ");
            if (Arrays.asList(words).contains("normal")) {
                somethingSelected = true;
                normal();
            } else if (Arrays.asList(words).contains("test")) {
                somethingSelected = true;
                Tester.test();
            } else if (Arrays.asList(words).contains("quit")) {
                somethingSelected = true;
                System.exit(0);
            } else {
                System.out.println("Input not recognised. Please try again.");
            }
        }
    }


    /**
     * Normal system use. Initially creates a Cinema object and adds a first showing.
     * After lists options for further actions which can be taken:
     * - Add a new Showing.
     * - Add new Customer.
     * - Add a review for a movie.
     * - Print reports.
     * - Changing a booking.
     * - Quit.
     */
    private static void normal(){
        boolean quit = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the number of screens available...");
        Integer screenNo=null;
        while(screenNo==null||screenNo<=0){
            if(scanner.hasNextInt()){
                screenNo = scanner.nextInt();
            }
            else{
                System.out.println("Please enter a number.");
                scanner.next();
            }
        }
        Cinema cinema = new Cinema(screenNo);
        System.out.println("Now to add first showing.");
        if(!addShowing(cinema)){
            return;
        }
        while(!quit){
            int action = listOptions();
            switch (action){
                case 1:
                    try {
                        cinema.addShowing();
                    } catch (NonValidOptionException e) {
                        System.out.println(e.getMessage());
                    } catch (InvalidDateTimeException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        cinema.newCustomer();
                    } catch (CustomerError | InvalidDateTimeException | SeatBookError | NonValidOptionException e) {
                        System.out.println(e.getMessage());
                        quit = quit();
                    }
                    break;
                case 3:
                    addReview(cinema);
                    break;
                case 4:
                    System.out.println("\n*Highest grossing movie:\n");
                    System.out.println(cinema.returnHighestGrossingMovie());
                    System.out.println("\n*Spectators and average rating per movie:\n");
                    System.out.println(cinema.returnViewNumbersAndRating());
                    System.out.println("*Reviews and ratings per movie:");
                    System.out.println(cinema.returnReviewsAndRatings());
                    break;
                case 5:
                    changeBooking(cinema);
                    break;
                case 6:
                    try {
                        cinema.removeMovie();
                    } catch (NonValidOptionException e) {
                        e.getMessage();
                        quit = quit();
                    }
                    break;
                case 7:
                    quit = true;
                    break;
            }
        }
    }


    /**
     * Creates a showing based on user input.
     *
     * @param cinema
     * @return Bool. True, showing added successfully. False, showing not added successfully.
     */
    private static boolean addShowing(Cinema cinema){
        boolean quit = false;
        while (!quit) {
            try {
                cinema.addShowing();
                return true;
            } catch (NonValidOptionException e) {
                System.out.println(e.getMessage());
                quit = quit();
            } catch (InvalidDateTimeException e) {
                System.out.println(e.getMessage());
                quit = quit();
            }
        }return false;
    }


    /**
     * Asks user if they would like to quit.
     *
     * @return Bool. True, user wants to quit. False, user does not want to quit.
     */
    private static boolean quit(){
        System.out.println("If you would like to quit enter 'Quit', otherwise just enter anything else to continue...");
        Scanner scanner = new Scanner(System.in);
        String[] words = scanner.next().split(" ");
        if (Arrays.asList(words).contains("quit")) {
            return true;
        }
        else{
            return false;
        }
    }


    /**
     * Lists the available options and prompts user to select one.
     *
     * @return int - action chosen.
     */
    private static int listOptions(){
        int i=0;
        Scanner scanner = new Scanner(System.in);
            while((i==0)||(i<1)||(i>7)){
                System.out.println("Please enter the number for the action you wish to take:");
                System.out.println("1. Add a new showing.");
                System.out.println("2. Add a new Customer.");
                System.out.println("3. Add a review for a movie.");
                System.out.println("4. Print reports.");
                System.out.println("5. Change Booking.");
                System.out.println("6. Remove a movie from currently showing list.");
                System.out.println("7. Quit.");
                if(scanner.hasNextInt()){
                    i = scanner.nextInt();
                }
                else{
                    System.out.println("Please enter a number between 1 and 7.");
                    scanner.next();
                }
            }
        return i;
    }


    /**
     * Adds a review and rating (int between 1-5).
     * @param cinema
     */
    private static void addReview(Cinema cinema){
        boolean quit = false;
        //just to get rid of a variable may have not been initialize error.
        Movie movie = new Movie("Dummy Variable");
        getmovie:
        while (!quit) {
            try {
                movie = cinema.getMovie();
                break getmovie;
            } catch (NonValidOptionException e) {
                System.out.println(e.getMessage());
                quit = quit();
            }
        }
        if(quit){
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a review...");
        String review = scanner.next();
        Integer rating = 0;
        while ((rating<1)||(rating>5)) {
            System.out.println("Please enter a rating (1-5)");
            if(scanner.hasNextInt()){
                rating = scanner.nextInt();
            }
            else{
                scanner.next();
            }

        }
        movie.addReviewRating(review,rating);
    }


    /**
     * Changes booking. Gets showing and seat id from user. Unbooks given seat and
     * books a new seat.
     * @param cinema
     */
    private static void changeBooking(Cinema cinema) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your movie...");
        boolean quit = false;
        //just to get rid of variable might not be initialized error.
        Movie movie = new Movie("Dummy variable");
        getDate:
        while (!quit) {
            try {
                movie = cinema.getMovie();
                break getDate;
            } catch (NonValidOptionException e) {
                e.getMessage();
                quit = quit();
            }
        }
        if (quit) {
            return;
        }
        //Just to get rid of variable might not be initialized error
        Showing show = new Showing(movie,"Evening", LocalDate.now(),1);
        try {
            show = cinema.getDateChangeBooking(movie);
        } catch (InvalidDateTimeException e) {
            System.out.println(e.getMessage());
            return;
        }
        getseat:
        while (!quit) {
            boolean unbooked = false;
            while (!unbooked) {
                show.showAvailableSeats();
                System.out.println("Please enter your seat ID...");
                String seatId = scanner.next();
                unbooked = show.unbook(seatId);
                if(!unbooked) {
                    System.out.println("This seat has not been booked");
                    if (quit()) {
                        break getseat;
                    }
                }
            }

            try {
                cinema.newCustomer();
                return;
            } catch (CustomerError | InvalidDateTimeException | SeatBookError | NonValidOptionException e) {
                System.out.println(e.getMessage());
                quit = quit();
            }
        }
    }
}



/**
 * Write a description of class Cinema here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


public class Cinema {
    private ArrayList<Movie> movies = new ArrayList<>();
    private int screens;


    /**
     * Initializes a new Cinema object with the number of screens provided.
     *
     * @param screenCount int.
     */
    public Cinema(int screenCount){
        this.screens = screenCount;
    }


    /**
     * Creates a new Showing object and adds it to the showings hashmap.
     * mainly used for testing purposes.
     *
     * @param showing Showing to be added.
     * @throws InvalidDateTimeException
     */
    public void addshowing(Showing showing) throws InvalidDateTimeException{
        Movie movie = null;
        boolean movieExists = false;
        for(Movie movie1:movies){
            if(showing.getMovie().getTitle().compareToIgnoreCase(movie1.getTitle())==0){
                movieExists = true;
                movie = movie1;

            }
        }
        if(movieExists){
            for(Showing show: movie.getShowings()){
                if(show.compareTimes(showing)){
                    throw new InvalidDateTimeException("This screen is not available at this time.");
                }
            }
            movie.addShowing(showing);
        }else{
            showing.getMovie().addShowing(showing);
            movies.add(showing.getMovie());
        }
    }


    /**
     * Creates new Showing object where the data for the showing is input by the user
     * instead of passing in a Showing object.
     */
    public void addShowing() throws NonValidOptionException, InvalidDateTimeException {
        LocalDate date = getDateForAddShowing();
        String time = getTimeForAddShowing();
        int screenNo = getScreenNoForAddShowing(date, time);
        Movie movie = getMovieForAddShowing();
        Showing showing = new Showing(movie,time,date,screenNo);
        addshowing(showing);
    }


    /**
     * Goes through showings hashmap, totals the number of booked seats per showing
     * for each movie and returns the the highest grossing movie and how much it grossed.
     *
     * @return String - Highest grossing movie and amount grossed.
     */
    public String returnHighestGrossingMovie(){
        String topMovie=null;
        DecimalFormat df = new DecimalFormat(".00");
        Double topRevenue=0.00;
        for(Movie movie:movies){
            Double total = 0.00;
            for(Showing show: movie.getShowings()){
                if(show.getDate().getMonthValue()==LocalDate.now().getMonthValue()) {
                    total += show.returnRevenue();
                }
            }
            if(total>topRevenue){
                topRevenue = total;
                topMovie = movie.getTitle();
            }
        }
        return ("The top grossing movie was: "+topMovie+", grossing: £"+df.format(topRevenue));
    }



    public String returnReviewsAndRatings(){
        String string = "";
        for (Movie movie:movies){
            string+=movie.returnReviewsAndRating();
        }
        return string;
    }


    /**
     * Goes through showings hashmap and finds the number of spectators and average
     * rating per movie.
     *
     * @return String - List of movies combined with their viewer count and average rating.
     */
    public String returnViewNumbersAndRating(){
        String report="";
        for(Movie movie:movies){
            int viewCount = 0;
            for(Showing show:movie.getShowings()){
                if(show.getDate().getMonthValue()==LocalDate.now().getMonthValue()) {
                    viewCount += show.returnViewNo();
                }
            }
            report+="Movie: "+movie.getTitle()+", Spectators: "+viewCount+", Rating: "+String.format("%.2f",movie.returnAverageRating())+"\n";
        }
        return report;
    }


    /**
     * Converts String entered (if formatted as dd/mm/yyyy) as a LocalDate object.
     *
     * @param dateInput String - Date in format dd/mm/yyyy.
     * @return LocalDate for the date given by String.
     * @throws InvalidDateTimeException
     */
    public static LocalDate convertStringToDate(String dateInput) throws InvalidDateTimeException {
            try {
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                LocalDate date = df.parse(dateInput).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if(date.compareTo(LocalDate.now())<0){
                    throw new InvalidDateTimeException("Date cannot be in the past");
                }else{
                    return date;
                }
            } catch (java.text.ParseException e) {
                throw new InvalidDateTimeException("Date must be a string in the format dd/mm/yyyy");
            }
    }


    /**
     * Returns a showing if one exists based on the movie, time, and date supplied.
     * mainly used for testing purposes.
     *
     * @param movie Movie - movie you wish to find a showing for.
     * @param time String - time you wish to find a showing for.
     * @param date LocalDate - date you wish to find a showing for.
     * @return Showing
     * @throws NonValidOptionException
     */
    public Showing getShowing(Movie movie, String time, LocalDate date) throws NonValidOptionException{
        for(Showing show:movie.getShowings()){
            if((show.getTime().compareToIgnoreCase(time)==0)&&(show.getDate().compareTo(date)==0)){
                return show;
            }
        }
        throw new NonValidOptionException("Showing does not exist.");
    }


    /**
     * Creates and returns a new Movie object based on user input.
     *
     * @return Movie
     */
    private Movie getMovieForAddShowing(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the title of the movie you wish to add a showing for...");
        String title = scanner.next();
        Movie movieToBeReturned=null;
        for(Movie movie:movies){
            if(movie.getTitle().compareToIgnoreCase(title)==0){
                movieToBeReturned = movie;
            }
        }
        if(movieToBeReturned==null){
            movieToBeReturned = new Movie(title);
        }
        return movieToBeReturned;
    }


    /**
     * Lists the available screens at a given time and date. User can then select a screen number
     * which will be returned.
     *
     * @param date LocalDate - date you wish to check screen availability for.
     * @param time String = time you wish to check screen availability for.
     * @return int - screen number selected.
     * @throws NonValidOptionException
     */
    private int getScreenNoForAddShowing(LocalDate date, String time)throws NonValidOptionException{
        ArrayList<Integer> screensList = new ArrayList();
        for(int i =1;i<=this.screens;i++){
            screensList.add(i);
        }
        //Removes a screen if there is a booking at the given date/time.
        for(Movie movie:movies){
            for (Showing show:movie.getShowings()){
                screensList.remove(show.compareTimes(date, time));
            }
        }
        if(screensList.size()==0){
            throw new NonValidOptionException("No screens are available at this time.\n");
        }
        System.out.println("The following screens are available at this time:\n");
        for(Integer i:screensList){
            System.out.print(i+" ");
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nWhich screen would you like to add a showing to...");
        Integer screen = null;
        while(screen==null){
            if(scanner.hasNextInt()){
                screen = scanner.nextInt();
            }
            else{
                System.out.println("Please enter a number.");
                scanner.next();
            }
        }
        if (screensList.contains(screen)){
            return screen;
        }
        else{
            throw new NonValidOptionException("This screen is not available at this time.");
        }
    }


    /**
     * Returns a LocalDate object based on user input.
     *
     * @return LocalDate - date selected.
     * @throws InvalidDateTimeException
     */
    private LocalDate getDateForAddShowing()throws InvalidDateTimeException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the date of the showing (dd/mm/yyyy)...");
        String dateInput = scanner.next();
        LocalDate date = convertStringToDate(dateInput);
        return date;
    }


    /**
     * Returns a time from a list of pre set times based on user input.
     * @return String - time selected.
     * @throws NonValidOptionException
     */
    private String getTimeForAddShowing() throws NonValidOptionException{
        Scanner scanner = new Scanner(System.in);
        String[] times = {"Evening", "Afternoon", "Night1", "Night2"};
        System.out.println("Please enter a time from the following:\n"+
        "Afternoon, Evening, Night1, Night2");
        String timeInput = scanner.next();
        for(String time:times){
            if(timeInput.compareToIgnoreCase(time)==0){
                return timeInput;
            }
        }throw new NonValidOptionException("Time entered is not listed");
    }


    /**
     *Creates a new customer object based on user input.
     *
     * @throws CustomerError
     * @throws InvalidDateTimeException
     * @throws SeatBookError
     * @throws NonValidOptionException
     */
    public void newCustomer() throws CustomerError, InvalidDateTimeException, SeatBookError, NonValidOptionException {
        Movie movie = getMovieForNumCustomer();
        Showing show = getDateForNewCustomer(movie);
        boolean paymentType = getPaymentType();
        Customer customer = new Customer(show,paymentType);
        show.book(customer);
        customer.setSeatId(show.getCustomerSeatId(customer));
    }


    /**
     * Prints out available movies and prompts the user to select one.
     * Onces on is selected it is returned.
     *
     * @return Movie
     * @throws CustomerError
     */
    private Movie getMovieForNumCustomer() throws CustomerError{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Here is a list of movies currently playing at this cinema: ");
        for(Movie movie:movies){
            System.out.print(movie.getTitle()+"\n");
        }
        System.out.println("Please enter the name of the movie you would like to see...");
        String movieTtile = scanner.next();
        Movie movie = null;
        for(Movie movies:movies){
            if(movies.getTitle().compareToIgnoreCase(movieTtile)==0){
                movie = movies;
            }
        }
        if(movie==null){
            throw new CustomerError("Chosen movie not currently showing");
        }
        else{
            return movie;
        }
    }




    /**
     * Prints out available dates/times for movie passed in. Then prompts user to select
     * a date/time and returns the corresponding Showing.
     *
     * @param movie movie selected.
     * @return Showing - showing selected based on input movie and selected date and time.
     * @throws InvalidDateTimeException
     */
    private Showing getDateForNewCustomer(Movie movie) throws InvalidDateTimeException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("You have selected " + movie.getTitle() + "." +
                "\nHere are the date and times " + movie.getTitle() + " is showing:");
        for (Showing show : movie.getShowings()) {
            System.out.print(show.getDateString() + " : ");
            System.out.print(show.getTime()+" : ");
            System.out.print("Screen number "+show.getScreenNo()+"\n");
        }
        System.out.println("Please enter the date you would like to book (dd/mm/yyyy)...");
        String dateInput = scanner.next();
        System.out.println("Please enter the time of the showing you would like to book...");
        String timeInput = scanner.next();
        int screenNo = 0;
        while (screenNo==0) {
            System.out.println("Please enter the screen number of the showing you wish to book...");
            if(scanner.hasNextInt()){
                screenNo = scanner.nextInt();
            }
            else{
                scanner.next();
            }

        }
        Showing showPicked=null;
        for (Showing show : movie.getShowings()) {
            if ((dateInput.compareToIgnoreCase(show.getDateString()) == 0) && (timeInput.compareToIgnoreCase(show.getTime()) == 0)&&(screenNo==show.getScreenNo())) {
                showPicked = show;
            }
        }
        if(!(showPicked==null)){return showPicked;}
        else{
            throw new InvalidDateTimeException("No showing exists for the date/time/screen number entered.");
        }
    }


    /**
     * Finds showing based on input movie and date/time of showing.
     *
     * @param movie movie customer wishes to change date for.
     * @return Showing - customers current showing.
     * @throws InvalidDateTimeException
     */
    public Showing getDateChangeBooking(Movie movie) throws InvalidDateTimeException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("You have selected " + movie.getTitle() + "." +
                "\nHere are the date and times " + movie.getTitle() + " is showing:");
        for (Showing show : movie.getShowings()) {
            System.out.print("\n" + show.getDateString() + " : ");
            System.out.print(show.getTime() + "\n");
        }
        System.out.println("Please enter the date of your current booking (dd/mm/yyyy)...");
        String dateInput = scanner.next();
        System.out.println("Please enter the time of your current booking...");
        String timeInput = scanner.next();
        Showing showPicked=null;
        for (Showing show : movie.getShowings()) {
            if ((dateInput.compareToIgnoreCase(show.getDateString()) == 0) && (timeInput.compareToIgnoreCase(show.getTime()) == 0)) {
                showPicked = show;
            }
        }
        if(!(showPicked==null)){return showPicked;}
        else{
            throw new InvalidDateTimeException("No showing exists for the date/time entered.");
        }
    }


    /**
     * Lists available movies and prompts user to select one.
     * Seleted movie is then returned.
     *
     * @return Movie - selected movie.
     * @throws NonValidOptionException
     */
    public Movie getMovie()throws NonValidOptionException{
        System.out.println("Current movies playing: \n");
        for(Movie movie:movies){
            System.out.print(movie.getTitle() + ", ");
        }
        Scanner scanner = new Scanner(System.in);
        String movieTitle = scanner.next();
        for(Movie movie: movies){
            if (movie.getTitle().compareToIgnoreCase(movieTitle)==0){
                return movie;
            }
        }
        throw new NonValidOptionException("Movie not in list of currently showing movies");
    }


    /**
     * Prompts user to enter either card or cash as payment for customer.
     *
     * @return boolean. True - card, False - cash.
     * @throws NonValidOptionException
     */
    private boolean getPaymentType()throws NonValidOptionException{
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        System.out.println("Please enter 'Cash' or 'Card' as a payment type...");
            String[] input = scanner.next().split(" ");
            for(String word:input){
                if(word.compareToIgnoreCase("card")==0){
                    return true;
                }
                else if (word.compareToIgnoreCase("cash")==0){
                    return false;
                }

        }throw new NonValidOptionException("Payment type must be either card or cash.");
    }


    /**
     * Prompts user to select movie from movies currently playing, and removes it.
     * @throws NonValidOptionException
     */
    public void removeMovie()throws NonValidOptionException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Here are the current movies playing:\n");
        for (Movie movie: movies){
            System.out.println(movie.getTitle());
        }
        System.out.println("\nPlease enter the movie you wish to remove...");
        Movie movieToBeRemoved = null;
        String movieTitle = scanner.next();
        boolean hasFutureShowings=false;
        for (Movie movie:movies){
            if (movie.getTitle().compareToIgnoreCase(movieTitle)==0){
                movieToBeRemoved=movie;
            }
        }if (movieToBeRemoved!=null){
            for (Showing show:movieToBeRemoved.getShowings()){
                if(show.getDate().isAfter(LocalDate.now())){
                    hasFutureShowings=true;
                }
            }
        }else{
            throw new NonValidOptionException("Movie entered is not currently playing");
        }
        if (hasFutureShowings){
            System.out.println("This movie still has showings coming up. Are you sure you wish to delete it?");
            String input = "";
            while ((input.compareToIgnoreCase("no")!=0)&&(input.compareToIgnoreCase("yes")!=0)){
                System.out.println("Please enter either 'Yes' or 'No'...");
                input = scanner.next();
            }if (input.compareToIgnoreCase("yes")==0){
                movies.remove(movieToBeRemoved);
                System.out.println(movieToBeRemoved.getTitle()+" has successfully been removed.");
            }
        }else{
            movies.remove(movieToBeRemoved);
            System.out.println(movieToBeRemoved.getTitle()+" has successfully been removed.");
        }
    }
}

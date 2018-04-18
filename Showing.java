
/**
 * Write a description of class Showing here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class Showing {
    private HashMap<Seat, Boolean> seats = new HashMap<>();
    private Movie movie;
    private String time = null;
    private LocalDate date;
    private HashMap<String, Double> prices = new HashMap<>();
    private static int showingIdCount = 0;
    private int showingId;
    private int screenNo;
    private HashMap<Customer, Seat> customers = new HashMap<>();


    /**
     *Creates a new showing object.
     *
     * @param movie Movie to be shown at given screen.
     * @param time String, either Afternoon, Evening, Night 1, Night 2.
     * @param date Date fo showing.
     */
    public Showing(Movie movie, String time, LocalDate date, int screenNo){
        // I know this only creates a shallow copy, but only the bool values will be changed not the seat objects
        seats =(HashMap<Seat, Boolean>) Seat.generateSeats().clone();
        this.date = date;
        this.movie = movie;
        generatePrices();
        this.showingId = ++showingIdCount;
        this.screenNo = screenNo;
        this.time = time;
    }


    private void generatePrices(){
        prices.put("afternoon", -2.00);
        prices.put("evening", 0.00);
        prices.put("night1", +2.00);
        prices.put("night2", 0.00);
    }

    /**
     * Prints out available seats. Unavailable seats are represented as '***'
     */
    public void showAvailableSeats(){
        List<String> seatIds = new ArrayList<String>();
        for(Seat s: seats.keySet()){
            seatIds.add(s.getId()+" ");
        }
        Collections.sort(seatIds);
        int[] indexes = {10,21,32,43,54};
        for(int i:indexes){
            seatIds.add(i, "\n");
        }
        for (Seat seat: seats.keySet()){
            if(seats.get(seat)){
                int index = seatIds.indexOf((seat.getId()+" "));
                seatIds.set(index, "*** ");
            }
        }
        seatIds.add(44,"v-----------------VIP------------------v\n");
        seatIds.add(0,"=================SCREEN================\n");
        seatIds.add(0,this.movie.getTitle()+" : "+this.getTime()+" "+this.getDateString()+" : Screen Number "+this.screenNo+"\n");
        for (String s:seatIds){
            System.out.print(s);
        }
    }


    public String getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }

    /**
     * Shows available seats and prompts user to select a seat id. Seat is then
     * booked (value in seats hashmap set to true).
     *
     * @return Seat object which has been booked.
     * @return Null if seat was not booked.
     */
    public void book(Customer customer) throws SeatBookError{
        showAvailableSeats();
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPlease enter the seat you wish to book....");
        String seatId = scanner.next();
        for(Seat seat:seats.keySet()){
            if(seatId.compareToIgnoreCase(seat.getId()) == 0){
                if(seats.get(seat)){
                    throw new SeatBookError("Seat already booked");
                }
                else{
                    seats.put(seat, true);
                    customers.put(customer, seat);
                    customer.setSeatId(seat.getId());
                    System.out.println("\n"+seat.getId()+" booked successfully.");
                    System.out.println("Base seat price: £ 10.00.");
                    if(seat.isVip()){
                        System.out.println("VIP seat selected: £ +2.00.");
                    }
                    if(this.time.compareToIgnoreCase("afternoon")==0){
                        System.out.println("Afternoon show: £ -2.00.");
                    }
                    else if(this.time.compareToIgnoreCase("night1")==0){
                        System.out.println("Night 1 showing: £ +2.00.");
                    }
                    DecimalFormat df = new DecimalFormat(".00");
                    double price = seat.returnPrice()+this.prices.get(this.time.toLowerCase());
                    System.out.println("Total price: £ "+ df.format(price)+".\n");
                    return;
                }
            }
        }
        throw new SeatBookError("Seat with given Id does not exist.");
    }


    /**
     * Same as book(Customer) but gets seat id from customer.getSeat() instead of
     * prompting user to get seat id.
     *
     * @param customer
     * @throws SeatBookError
     */
    public void bookFromField(Customer customer) throws SeatBookError{
        showAvailableSeats();
        String seatId = customer.getSeat();
        for(Seat seat:seats.keySet()){
            if(seatId.compareToIgnoreCase(seat.getId()) == 0){
                if(seats.get(seat)){
                    throw new SeatBookError("Seat already booked");
                }
                else{
                    seats.put(seat, true);
                    customers.put(customer, seat);
                    customer.setSeatId(seat.getId());
                    System.out.println("\n"+seat.getId()+" booked successfully.");
                    System.out.println("Base seat price: £ 10.00.");
                    if(seat.isVip()){
                        System.out.println("VIP seat selected: £ +2.00.");
                    }
                    if(this.time.compareToIgnoreCase("afternoon")==0){
                        System.out.println("Afternoon show: £ -2.00.");
                    }
                    else if(this.time.compareToIgnoreCase("night1")==0){
                        System.out.println("Night 1 showing: £ +2.00.");
                    }
                    DecimalFormat df = new DecimalFormat(".00");
                    double price = seat.returnPrice()+this.prices.get(this.time.toLowerCase());
                    System.out.println("Total price: £ "+ df.format(price)+".\n");
                    return;
                }
            }
        }
        throw new SeatBookError("Seat with given Id does not exist.");
    }


    public int getScreenNo(){
        return this.screenNo;
    }


    /**
     * Returns true if dates and time of day match, false otherwise.
     *
     * @param showing
     * @return Boolean
     */
    public boolean compareTimes(Showing showing){
        if ((this.date.compareTo(showing.date)==0)&&(this.time.compareToIgnoreCase(showing.getTime())==0)&&(this.screenNo==showing.getScreenNo())){
            return true;
        }
        else{
            return false;
        }
    }


    /**
     * Compares date and time. If they match the screen number of of the showing.
     *
     * @param dateInput
     * @param timeInput
     * @return
     */
    public Integer compareTimes(LocalDate dateInput, String timeInput){
        if ((this.date.compareTo(dateInput)==0)&&(this.time.compareToIgnoreCase(timeInput)==0)){
            return this.screenNo;
        }
        else{
            return null;
        }
    }

    public Movie getMovie() {
        return movie;
    }


    /**
     * Uses number of seats (Vip and non Vip) and time of showing to
     * return the revenue.
     *
     * @return double - revenue.
     */
    public double returnRevenue(){
        double revenue = 0.00;
        for (Seat seat:seats.keySet()){
            if(seats.get(seat)){
                revenue+=seat.returnPrice();
                revenue+=this.prices.get(this.time.toLowerCase());
            }
        }
        return revenue;
    }


    /**
     * Uses number of booked seats to return the number of viewers.
     *
     * @return int - Number of viewers.
     */
    public int returnViewNo(){
        int count=0;
        for (Seat seat:seats.keySet()){
            if(seats.get(seat)){
                count++;
            }
        }
        return count;
    }


    /**
     * Returns the date as a string in the format dd/mm/yyyy.
     *
     * @return String - date (dd/mm/yyyy)
     */
    public String getDateString(){
        return String.format("%02d",date.getDayOfMonth()) + "/" + String.format("%02d",date.getMonthValue()) + "/" + String.format("%02d",date.getYear());
    }


    /**
     * Unbooks seat seat booked by customer passed in.
     * mainly used for testing.
     * @param customer
     * @throws SeatBookError
     */
    public void unBook(Customer customer)throws SeatBookError{
        if(customers.containsKey(customer)) {
            if (!seats.get(customers.get(customer))) {
                throw new SeatBookError("Seat was not initially booked");
            } else {
                seats.put(customers.get(customer), false);
                System.out.println("Seat unbooked.\n");
                return;
            }
        }throw new SeatBookError("This customer does not have a seat booked for this showing.");
    }


    /**
     * Returns the seat id of seat booked by customer passed in
     * @param customer
     * @return String - seat id
     * @throws NonValidOptionException
     */
    public String getCustomerSeatId(Customer customer)throws NonValidOptionException{
        if(customers.containsKey(customer)){
            return customers.get(customer).getId();
        }
        throw new NonValidOptionException("This customer does not have a seat booked for this showing.");
    }


    /**
     * Unbooks seat (sets its value in seats hashmap to false.) based on seat id passed in.
     * @param seatId
     * @return
     */
    public boolean unbook(String seatId){
        for(Seat seat:customers.values()){
            if(seat.getId().compareToIgnoreCase(seatId)==0){
                seats.put(seat,false);
                return true;
            }
        }return false;
    }
}

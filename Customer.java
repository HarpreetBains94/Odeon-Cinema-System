
/**
 * Write a description of class Customer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

public class Customer {
    private String customerId;
    private Showing showing;
    private int customerIdCount;
    private String seatId;
    private boolean paymentTypeCard;

    /**
     * Creates new Customer with seat. Mainly used for testing.
     * @param show
     * @param seatId
     */
    public Customer(Showing show, String seatId,boolean paymentTypeCard){
        customerIdCount++;
        this.customerId = String.valueOf(customerIdCount);
        this.showing = show;
        this.seatId = seatId;
        try {
            show.bookFromField(this);
        } catch (SeatBookError seatBookError) {
            seatBookError.printStackTrace();
        }
        this.paymentTypeCard = paymentTypeCard;
    }

    /**
     * Creates new Customer.
     * @param show
     */
    public Customer(Showing show, boolean paymentTypeCard) {
        this.paymentTypeCard = paymentTypeCard;
        customerIdCount++;
        this.customerId = String.valueOf(customerIdCount);
        this.showing = show;
    }

    /**
     * Unbooks current seat and books passed in seat if available,
     *
     * @param newShow
     * @param seatId
     * @throws SeatBookError
     */
    public void changeBooking(Showing newShow, String seatId) throws SeatBookError {
        try {
            this.showing.unBook(this);
        } catch (SeatBookError seatBookError) {
            System.out.println(seatBookError.getMessage());
        }
        try {
            this.seatId = seatId;
            newShow.bookFromField(this);
        } catch (SeatBookError ex) {
            System.out.println(ex.getMessage());
        }
    }


    public String getSeat() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }
}




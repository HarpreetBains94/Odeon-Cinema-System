
/**
 * Write a description of class Seat here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.HashMap;

public class Seat implements Comparable<Seat>{
    private boolean isVip;
    private String id;
    private double price;

    private Seat(boolean isVip, String id){
        this.isVip = isVip;
        this.id = id;
        this.price = 10.00;
    }

    public String getId() {
        return id;
    }

    /**
     * Generates a Hashmap of 50 seat objects organised in to rows and columns.
     * Rows: A-E were E is a row of VIP seats.
     * Columns: 1-10.
     * @return Hashmap(Seat, Boolean). Boolean, true = Booked, false = Available.
     */
    public static HashMap<Seat, Boolean> generateSeats(){
        HashMap<Seat, Boolean> seatsInitial = new HashMap<>();
        for(int i = 65; i<=69; i++){
            //Loops through Characters A to E for the rows of seatsInitial
            Character alphaId =(char) i;
            if(i==69){
                //Seperate VIP seatsInitial (row E) and creates 10 seatsInitial per row
                for(int j = 1; j<=10; j++){
                    seatsInitial.put(new Seat(true,(alphaId.toString() + String.format("%02d",j))), false);
                }
            }else{
                //creates 10 seatsInitial per row
                for(int j = 1; j<=10; j++){
                    seatsInitial.put(new Seat(false,(alphaId.toString() + String.format("%02d",j))), false);
                }
            }
        }
        return seatsInitial;
    }

    public boolean isVip() {
        return isVip;
    }

    /**
     * Returns price of seat.
     * Vip: 12.00
     * Non Vip: 10.00
     * @return
     */
    public double returnPrice(){
        if (this.isVip){
            return this.price+2.00;
        }
        else{
            return this.price;
        }
    }

    @Override
    public int compareTo(Seat seat){
        return this.id.compareToIgnoreCase(seat.getId());
    }
}

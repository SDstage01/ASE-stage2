import java.util.Comparator;
public class PassengerComparator implements Comparator<Passenger> {
	
	public int compare(Passenger p1, Passenger p2) {
		if (p1.getBookingReference() != p2.getBookingReference()) {
			return p1.getBookingReference() - p2.getBookingReference();
		}	
		else return p1.getPassengerName().compareTo(p2.getPassengerName());
	}
}
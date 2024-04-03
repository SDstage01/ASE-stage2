import java.util.Comparator;

public class FlightCodeComparator {
	
	public int compare(Flight f1, Flight f2) {
		return f1.getFlightCode().compareTo(f2.getFlightCode());

	}
}

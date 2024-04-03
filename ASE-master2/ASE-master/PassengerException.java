public class PassengerException extends Exception {	
	public PassengerException(String e){
		super("Passenger already on flight: " + e);
	}
}
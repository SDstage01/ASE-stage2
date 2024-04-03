public class CheckInManager {
	
	private PassengerList allPassengers;
	private FlightList allFlights;
	public CheckInManager() {
		allPassengers = new PassengerList();
		allFlights = new FlightList(allPassengers);
	}
    
	public void run() throws PassengerException {
		
		allFlights.readFile(Config.getInstance().getFlightFileName());
		allPassengers.readFile(Config.getInstance().getPassengerFileName());

		CheckInGUI gui = new CheckInGUI(allFlights, allPassengers);
        	gui.setVisible(true);
	}
}

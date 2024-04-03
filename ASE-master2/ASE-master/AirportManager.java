

public class AirportManager {
	
	private PassengerList allPassengers;
	private FlightList allFlights;
	private Airport airport;
	private Log logwrite;
	
	public AirportManager() {
		allPassengers = new PassengerList();
		allFlights = new FlightList(allPassengers);
		airport = new Airport(allFlights);
		logwrite = Log.getInstance();
		//Vanesa added:
//		AirportDisplay queueDisplay = new AirportDisplay(airport);
//		DeskDisplay deskDisplay = new DeskDisplay(airport);
//		FlightDisplay flightDisplay = new FlightDisplay(airport);
		AirportGUI airportGUI = new AirportGUI(airport, allFlights);
//		airport.addObserver(airportGUI);
    	airportGUI.setVisible(true);
 	}
    
	public void run() {
		allFlights.readFile(Config.getInstance().getFlightFileName());
		try {
			allPassengers.readFile(Config.getInstance().getPassengerFileName());
		} catch (PassengerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Passenger passenger : PassengerList.getPassengerList()) {
			passenger.setRandomWeight();
			passenger.setRandomBagVolume();
			passenger.countFee();
		}
		airport.passengerIntoQueue();
		airport.deskStartCheckIn();	
	}
	
}

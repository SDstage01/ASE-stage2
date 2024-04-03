import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JUnitTests {
	PassengerList pList = new PassengerList();
	FlightList fList = new FlightList(pList);

	@Before
	public void setUp() throws PassengerException {

		Boolean ci1 = Boolean.parseBoolean("TRUE");
		int bref1 = 44850;
		Name name1 = new Name("Millie Villages");
		String fc1 = "EI3259";
		Passenger p1 = new Passenger(bref1, name1, fc1, ci1, 0, 0.10, 5);
		pList.add(p1);

		Boolean ci2 = Boolean.parseBoolean("FALSE");
		int bref2 = 84629;
		Name name2 = new Name("Alison Brown");
		String fc2 = "GH6494";
		Passenger p2 = new Passenger(bref2, name2, fc2, ci2, 0,0,0);
		pList.add(p2);
		
		int bref3 = 48634;
		Name name3 = new Name("Mark McDonald");
		Passenger p3 = new Passenger(bref3, name3, fc1, ci1, 0, 0.50, 3);
		pList.add(p3);

		String d1 = "Dublin";
		String c1 = "British Airways";
		int pCap1 = 70;
		double bwCap1 = 300;
		double bvCap1 = 30;
		Flight f1 = new Flight(fc1, d1, c1, pCap1, bwCap1, bvCap1);
		fList.add(f1);

		String d2 = "Belfast";
		String c2 = "Rayenair";
		int pCap2 = 50;
		double bwCap2 = 200;
		double bvCap2 = 25;
		Flight f2 = new Flight(fc2, d2, c2, pCap2, bwCap2, bvCap2);
		fList.add(f2);
	}

		@After
		public void tearDown() {
			PassengerList pList = new PassengerList();
			FlightList fList = new FlightList(pList);
		}

	@Test		//Test Passenger name can be found and converted.
	public void testGetPassengerName() {
		Boolean tci = Boolean.parseBoolean("FALSE");
		int tbref = 12345;
		Name tname = new Name("Jimmy McBobby");
		String tfc = "AB1234";
		Passenger tp = new Passenger(tbref, tname, tfc, tci, 0,0,0);
		String expected1 = "Jimmy";
		String message1 = "Get name method failed";
		Name name1 = tp.getPassengerName();
		String actual1 = name1.getFirstName();		
		assertEquals(message1, expected1, actual1);
	}

	@Test		//Test if Passenger search works. (name method already tested)
	public void testFindPassenger() {
		String tbref1 = "84629";
		String tname1 = "Brown";
		String expected1 = "Alison";
		String message1 = "Find existing Passenger method failed";		 
		Passenger tp1 = pList.findPassenger(tbref1, tname1);		 
		String actual1 = tp1.getPassengerName().getFirstName();
		assertEquals(message1, expected1, actual1);

		String tbref2 = "00000";
		String tname2 = "IncorrectName";
		String message2 = "Finding fake Passenger method failed";		 
		Passenger actual2 =  pList.findPassenger(tbref2, tname2);	
		assertEquals(message2, null, actual2);
	}	

	@Test		//Test if Flight search works. (getFlightCode only simply return method)
	public void testFindByFlightCode() {
		String tfc1 = "GH6494";
		String expected1 = "GH6494";
		String message1 = "Find existing Flight method failed";		 
		Flight tf1 = fList.findByFlightCode(tfc1);		 
		String actual1 = tf1.getFlightCode();
		assertEquals(message1, expected1, actual1);

		String tfc2 = "00000";
		String message2 = "Finding fake Flight code method failed";
		Flight actual2 = fList.findByFlightCode(tfc2);
		assertEquals(message2, null, actual2);
	}	

	@Test
	public void testGetWeightByFlight() {
		String message1 = "Finding the passenger list of flight method failed";		 
		String tfc1 = "EI3259";
		double actual1 = pList.getWeightByFlight(tfc1);		
		double expected1 = 0.6;
		boolean test = expected1 == actual1 ;
		assertEquals(message1,  Boolean.parseBoolean("TRUE"),  test);
	}
	
} 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FlightList {
	
	private PassengerList allPassengers;
	//instantiate a TreeSet with Flight
	TreeSet<Flight> flightList = new TreeSet<Flight>();
	
	public FlightList(PassengerList passengerList) {
		allPassengers = passengerList;
	}
	
	public void add(Flight f) {
		flightList.add(f);
	}
	
	public void readFile (String filename) {
		  try {
			  char compType = filename.charAt(0);
			  File f = new File(filename);
			  Scanner scanner = new Scanner(f);
			  while (scanner.hasNextLine()) {
				  String inputLine = scanner.nextLine();
				  if (inputLine.length() != 0) { 
					  processLine(inputLine, String.valueOf(compType));
				  }					 
			  }
			  scanner.close();
		 }
				/**
				 * if the file is not found, stop with system exit
				 */					

		   catch (FileNotFoundException fnf){
					System.out.println( filename + " not found " + System.getProperty("user.dir"));
					System.exit(0);
		  }
	  }
	
	private void processLine(String line, String type) {
		try {
			String parts [] = line.split(",");
			String flightCode = parts[0];
			String distination = parts[1];
			String carrier = parts[2];
			int maxPCap = Integer.parseInt(parts[3]);
			double maxWeight = Double.valueOf(parts[4]);
			double maxVolume = Double.valueOf(parts[5]);
			
			Flight f = new Flight(flightCode, distination, carrier, maxPCap, maxWeight, maxVolume);
			this.add(f);
				  
		}
			catch (ArrayIndexOutOfBoundsException air) {
				String error = "Not enough items in  : '" + line
				                        + "' index position : " + air.getMessage();
				System.out.println(error);
			}
			catch (NumberFormatException nfe) {
				String error = "Number conversion error in '" + line + "'  - " 
				                  + nfe.getMessage();
				System.out.println(error);
			}
		}
	
	public Flight findByFlightCode (String fcode) {
		for (Flight f: flightList) {
			if(fcode.equals(f.getFlightCode()))
			{return f;}
		}	
	return null;
	}
	
	public void writeFlightReport(String filename) {
		 FileWriter fw;
		 try {
		    fw = new FileWriter(filename);
		    fw.write("All Flight details:\n");
		    fw.write(getFlightDetails());
		 	fw.close();
		 }

		 
		 catch (FileNotFoundException fnf){
			 System.out.println(filename + " not found ");
			 System.exit(0);
		 }

		 catch (IOException ioe){
		    ioe.printStackTrace();
		    System.exit(1);
		 }
	}
	
	public String getFlightDetails() {
		String result= "Flight     Passengers Checked In     Total Baggage Volume     Total Baggage Weight     Total Fees Collected      Observations\n";
		result += System.lineSeparator();
		for (Flight f : flightList) {
			double totalVolume= allPassengers.getVolumetByFlight(f.getFlightCode());
			double totalWeight = allPassengers.getWeightByFlight(f.getFlightCode());
			double totalFee = allPassengers.getFeesByFlight(f.getFlightCode());
			String observations;
			
			result += String.format("%-20s", f.getFlightCode());
			result += String.format("%-26s", allPassengers.countCheckedInByFlight(f.getFlightCode()));
			result += String.format("%-26s", totalVolume);
			result += String.format("%-23s", totalWeight);
			result += String.format("%-13s", totalFee);

			if (totalVolume > f.getBagVolCap() && totalWeight > f.getBagWeightCap()) {
				observations = "Maximum volume and weight exceeded!";
			}
			else if (totalVolume <= f.getBagVolCap() && totalWeight > f.getBagWeightCap()){
				observations = "Maximum baggage weight exceeded!";
			}
			else if (totalVolume > f.getBagVolCap() && totalWeight <= f.getBagWeightCap()) {
				observations = "Maximum baggage volume exceeded!";
			}
			else {
				observations = "";
			}
			result += String.format("%-1s", observations);
			result += System.lineSeparator();
			
		}
		return result;
	}
	
	public TreeSet<Flight> getFlightList() {
		return flightList;	
	}
		
}

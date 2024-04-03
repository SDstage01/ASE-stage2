import java.util.TreeSet;
import java.util.concurrent.Semaphore;
import javax.sound.midi.Soundbank;
import java.io.ObjectInputStream.GetField;
import java.util.*; 


public class Airport extends Observable {
	
	int numberOfDesks = Config.getInstance().getNumDesk();
	
	private PassengerList passengerQueue = new PassengerList();
	
	boolean lastCheckinComplete = false;
	
	public volatile String[] deskStatus = new String[numberOfDesks];
	
	private Log superlogwiter;
	
	private double speed = Config.getInstance().getSpeed();
	
	private int interval = Config.getInstance().getInterval();
	
	private static volatile boolean running = true;
	
	private Log log;
	
	// the queue of waiting passengers
	private Queue<Passenger> queue;
	
	private FlightList flightList;
	
	public Airport(FlightList flightList) {
		for(int i = 0; i < numberOfDesks; i++) {
			if(running) {deskStatus[i] = "Waiting for passengers.";}
		}
		queue = new LinkedList<Passenger>();
		prepareToDepart();
		this.flightList = flightList;
		log = Log.getInstance();
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
		interval = (int) (interval / speed);
	}
	
	public Log returnLoger() {
		// checks if the last check-in has finished before returning log
		while(!lastCheckinComplete) {
			// if not true wait a little then try again
			try {Thread.sleep(100);} 
			catch (InterruptedException e) {e.printStackTrace();}
		} 
		return this.superlogwiter;
	}

	// Defines a Passenger thread
	class PassengerIntoQueue implements Runnable {
		public void run(){		
			TreeSet<Passenger> passengerList = PassengerList.getPassengerList();
			
			for (Passenger passenger: passengerList) {
				while(!running) { }
				synchronized (queue) {
					queue.add(passenger);
				}
				String message = passenger.getPassengerName().getFullName()+" joins the queue.";
				log.log(LogType.PASSENGER_JOIN_QUEUE, message);
				changeAndNotify();
//				System.out.println(passenger.getPassengerName().getFullName()+" joined queue.");
				try {
					Thread.sleep(getRandomInterval());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
	}
	
	class Desk implements Runnable {
		
		private Passenger passenger;
		
		private int currentDesk;
		
		private int deskNo;
		
		public Desk(int deskNo) {
			this.deskNo = deskNo;
		}
	
		
		public void dropBaggage() {
			String threadName = Thread.currentThread().getName();
			int deskNum = threadName.charAt(threadName.length() - 1) - '1';
			if(running) {
				deskStatus[deskNum] = passenger.getPassengerName().getFullName()+" is dropping off a bag of "+
						String.format("%.2f", passenger.getBagWeight())+" kg.";
			}
			changeAndNotify();
			randomSleep();
		}
		
		public void chargeForFee() {
			if(passenger.getFee() > 0) {
				String threadName = Thread.currentThread().getName();
				int deskNum = threadName.charAt(threadName.length() - 1) - '1';
				if(running) {
					deskStatus[deskNum] = "Charging "+passenger.getPassengerName().getFullName()+" for £"+
							String.format("%.2f.", passenger.getFee());
				}
				changeAndNotify();
				randomSleep();
			}
		}
		
		public void checkIn() {
			String threadName = Thread.currentThread().getName();
			int deskNum = threadName.charAt(threadName.length() - 1) - '1';
			String message = passenger.getPassengerName().getFullName()+" is checking in.";
			if(running) { deskStatus[deskNum] = message; }
			passenger.checkIn();
			changeAndNotify();
			log.log(LogType.PASSENGER_CHECKING_IN, message);
			randomSleep();
			board();
			changeAndNotify();
			message = passenger.getPassengerName().getFullName()+" is boarding.";
			log.log(LogType.PASSENGER_BOARDING, message);
		}
		
		private void board() {
			addPassenger(passenger);
			addWeight(passenger);
		}
		
		public void waitForNext() {
			String threadName = Thread.currentThread().getName();
			int deskNum = threadName.charAt(threadName.length() - 1) - '1';
			if(running) {deskStatus[deskNum] = "Waiting for next passenger.";}
			randomSleep();
		}
		
		public void process() {
			dropBaggage();
			chargeForFee();
			checkIn();
			waitForNext();
		}
		
		public void run() {
			while (running) {
				while (passenger == null) {
					synchronized (queue) {
						passenger = queue.poll();
					}
				}
				process();
				passenger = null;
				randomSleep();
			}
		}
	}

	public void passengerIntoQueue(){
		Thread t = new Thread(new PassengerIntoQueue());
		t.setName("PassengerIntoQueue");
		t.start();
		
	}
	
	public void deskStartCheckIn(){
		for(int i = 1; i <= numberOfDesks; i++) {
			Thread t = new Thread(new Desk(i));
			t.setName("Desk"+i);
			t.start();
			changeAndNotify();
		}
	}
	
	public String getDeskStatuses(int deskNo) {
		return deskStatus[deskNo-1];
	}
	
	public PassengerList getPassengerQueue() {
		return passengerQueue;
	}
	
	public Queue<Passenger> getQueue() {
		return queue;
	}
	
	public void pause() {
		running = false;
	}
	
	public void start() {
		running = true;
	}
	
	public int getRandomInterval() {
		Random random = new Random();
        return random.nextInt(interval / 2) + interval / 2;
	}
	
	public void prepareToDepart() {
		Random random = new Random();
        int randomNumber =  random.nextInt(interval * 20) + interval * 20;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				pause();
				for(int i = 0; i < deskStatus.length; i++) {
					deskStatus[i]= "Closed";
				}
				changeAndNotify();
				String message = "Planes depart, desks are closed.";
				log.log(LogType.PLANE_DEPARTURE, message);
//				System.out.println("Planes depart, desks are closed.");
			}
		};
		timer.schedule(task, randomNumber);
	}
	
	public int getNumberOfDesks() {
		return numberOfDesks;
	}
	
	public void changeAndNotify() {
		setChanged();
		notifyObservers();
	}
	
	public void addPassenger(Passenger passenger) {
		String flightCode = passenger.getFlightCode();
		flightList.findByFlightCode(flightCode).addPassenger();
	}
	
	public void addWeight(Passenger passenger) {
		String flightCode = passenger.getFlightCode();
		flightList.findByFlightCode(flightCode).addWeight(passenger.getBagWeight());
	}
	
	public void randomSleep() {
		try {
			Thread.sleep(getRandomInterval());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

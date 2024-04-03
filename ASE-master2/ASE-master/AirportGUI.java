import javax.swing.*;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class AirportGUI extends JFrame implements Observer {
    private Airport airport;
    private FlightList flightList;
    private JButton continueButton;
    private JButton pauseButton;
    private JComboBox<String> speedComboBox;
    private JLabel speedLabel;

    private JTextArea flightInfoTextArea;
    private JTextArea deskStatusTextArea;
    private JTextArea passengerQueueTextArea;
    

    public AirportGUI(Airport airport, FlightList flightList) {
        super("Airport Simulation");

        this.airport = airport;
        this.flightList = flightList;
        
        airport.addObserver(this);;

        setLayout(new BorderLayout());
        
        // Information of planes
        flightInfoTextArea = new JTextArea(20, 30);
        flightInfoTextArea.setEditable(false);
        JScrollPane flightInfoScrollPane = new JScrollPane(flightInfoTextArea);
        add(flightInfoScrollPane, BorderLayout.WEST);

        // Information of desks
        deskStatusTextArea = new JTextArea(20, 20);
        deskStatusTextArea.setEditable(false);
        JScrollPane deskStatusScrollPane = new JScrollPane(deskStatusTextArea);
        add(deskStatusScrollPane, BorderLayout.CENTER);
        
        
        
        // Information of passengers
        passengerQueueTextArea = new JTextArea(20, 30);
        passengerQueueTextArea.setEditable(false);
        JScrollPane passengerQueueScrollPane = new JScrollPane(passengerQueueTextArea);
        add(passengerQueueScrollPane, BorderLayout.EAST);
        
        // Create buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        speedLabel = new JLabel("Speed:");
        speedLabel.setFont(new Font("Arial", 0, 16));
        buttonPanel.add(speedLabel);
        
        speedComboBox = new JComboBox<>(new String[]{"0.5x", "0.75x", "1x", "1.5x", "2x"});
        speedComboBox.setFont(new Font("Arial", 0, 16));
        speedComboBox.setSelectedItem("1x");
        speedComboBox.addActionListener(new SpeedComboBoxListener());
        buttonPanel.add(speedComboBox);
        
        // Add a blank space
        JLabel filler = new JLabel();
        filler.setPreferredSize(new Dimension(30, 0));
        buttonPanel.add(filler);
        
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new PauseButtonListener());
        pauseButton.setFont(new Font("Arial", 0, 16));
        // set button size
        Dimension buttonSize = new Dimension(90, 40); 
        pauseButton.setPreferredSize(buttonSize);
        buttonPanel.add(pauseButton);
        
        continueButton = new JButton("Continue");
        continueButton.addActionListener(new ContinueButtonListener());
        continueButton.setFont(new Font("Arial", 0, 16));
        
        continueButton.setPreferredSize(buttonSize);
        buttonPanel.add(continueButton);
      
        pauseButton = new JButton("Exit");
        pauseButton.addActionListener(new ExitButtonListener());
        pauseButton.setFont(new Font("Arial", 0, 16));
        pauseButton.setPreferredSize(buttonSize);
        buttonPanel.add(pauseButton);
        
        add(buttonPanel, BorderLayout.SOUTH);

        // Set window properties
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
		// Thead-safe
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	TreeSet<Flight> flights = flightList.getFlightList();
		        StringBuilder flightInfo = new StringBuilder();
		        flightInfo.append("Flights waiting to depart:\n");
		        if (airport.deskStatus[0] == "Closed") {
		        	for (Flight flight : flights) {
		                flightInfo.append(String.format("%-10s  %d checked in of %-5d %3.2fkg  Departured\n",
		                        flight.getFlightCode(), flight.getNumPassenger(), flight.getPassCap(), flight.getWeight()));
		            }
		        } else {
		        	for (Flight flight : flights) {
		                flightInfo.append(String.format("%-10s  %d checked in of %-5d %3.2fkg\n",
		                        flight.getFlightCode(), flight.getNumPassenger(), flight.getPassCap(), flight.getWeight()));
		            }
		        }
		        flightInfoTextArea.setText(flightInfo.toString());

		        // Renew desks
		        StringBuilder deskStatusInfo = new StringBuilder();
		        for (int i = 1; i <= airport.getNumberOfDesks(); i++) {
		            deskStatusInfo.append("Desk ").append(i).append(": ").append(airport.getDeskStatuses(i)).append("\n");
		        }
		        deskStatusTextArea.setText(deskStatusInfo.toString());

		        // Renew the queue
		        Queue<Passenger> passengers = airport.getQueue();
		        StringBuilder passengerQueueInfo = new StringBuilder();
		        passengerQueueInfo.append("Passengers in queue:\n");
		        for (Passenger passenger : passengers) {
		        	passengerQueueInfo.append(String.format("%-8s  %-18s  %3.2fkg\n",
		        			passenger.getFlightCode(),passenger.getPassengerName().getFullName(),
		        			passenger.getBagWeight()));
		        }
		        passengerQueueTextArea.setText(passengerQueueInfo.toString());
		    }
		});
	}
	
    private class ContinueButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            airport.start();
        }
    }
    
    private class ExitButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	airport.pause();
        	JOptionPane.showMessageDialog(null, 
   				 "Simulation closed. The log file is "+Log.getInstance().getFileName());
   		    System.exit(0);
        }
    }

    private class PauseButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            airport.pause();
        }
    }
    
    private class SpeedComboBoxListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JComboBox cb = (JComboBox)e.getSource();
            String selectedSpeed = (String)cb.getSelectedItem();
            double speed = 1.0;
            switch (selectedSpeed) {
                case "0.5x":
                    speed = 0.5;
                    break;
                case "0.75x":
                    speed = 0.75;
                    break;
                case "1x":
                    speed = 1.0;
                    break;
                case "1.5x":
                    speed = 1.5;
                    break;
                case "2x":
                    speed = 2.0;
                    break;
            }
            airport.setSpeed(speed);
        }
    }
}

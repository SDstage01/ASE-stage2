import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiFrame extends JFrame {
	
    public GuiFrame( ) {
        super("Welcome to Airport Simulation");

        setLayout(new BorderLayout());

        // create welcome text
        JLabel welcomeLabel = new JLabel("Welcome to Airport Simulation");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        // create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10)); 
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); 

        // create Stage1 button
        JButton stage1Button = new JButton("Stage 1");
        stage1Button.setFont(new Font("Arial", Font.BOLD, 16));
        stage1Button.addActionListener(new Stage1ButtonListener());
        buttonPanel.add(stage1Button);

        // create Stage2 button
        JButton stage2Button = new JButton("Stage 2");
        stage2Button.setFont(new Font("Arial", Font.BOLD, 16));
        stage2Button.addActionListener(new Stage2ButtonListener());
        buttonPanel.add(stage2Button);

        add(buttonPanel, BorderLayout.CENTER);

        // set window properties
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Stage 1 button listener
    private class Stage1ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	// close the welcome frame
            dispose();
            // open the frame of Stage 1
            startCheckInSimulation();
        }
    }

    // Stage 2 button listener
    private class Stage2ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // close the welcome frame
            dispose();
            // open the frame of Stage 2
            startAirportSimulation();
        }
    }
    
    private void startAirportSimulation() {
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
            	AirportManager manager = new AirportManager();
        		manager.run();
                return null;
            }
        };
        worker.execute();
    }
    
    private void startCheckInSimulation() {
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
            	CheckInManager manager = new CheckInManager();
        		manager.run();
                return null;
            }
        };
        worker.execute();
    }
}

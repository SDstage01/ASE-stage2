import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.sound.midi.Soundbank;

public class Config {

	private String path = "ASE-master/config.properties";
    private static volatile Config instance;
    private Properties properties;
    
    private String numDesk = "numDesk";
    private String speed = "speed";
    private String interval = "interval";
    private String flightFileName = "flightFileName";
    private String passengerFileName = "passengerFileName";

    private Config() {
        properties = new Properties();
        loadConfig();
    }

    public static Config getInstance() {
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config();
                }
            }
        }
        return instance;
    }

    private void loadConfig() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public int getNumDesk() {
    	return Integer.parseInt(properties.getProperty(numDesk));
    }
    
    public double getSpeed() {
    	return Double.parseDouble(properties.getProperty(speed));
    }
    
    public int getInterval() {
    	return Integer.parseInt(properties.getProperty(interval));
    }
    
    public String getFlightFileName() {
    	return properties.getProperty(flightFileName);
    }
    
    public String getPassengerFileName() {
    	return properties.getProperty(passengerFileName);
    }
}

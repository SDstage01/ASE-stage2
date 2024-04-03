import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

// Different types of log
enum LogType {
    PASSENGER_JOIN_QUEUE,
    PASSENGER_CHECKING_IN,
    PASSENGER_BOARDING,
    PLANE_DEPARTURE
}

public class Log {
    private static Log instance;
    private Logger logger;
    private FileHandler fileHandler;
    private String fileName;

    // private constructor, make it singleton
    private Log() {
    	
    	fileName = "Airport_Log.txt";
    	
    	// delete old file
    	File oldLogFile = new File(fileName);
    	
        if (oldLogFile.exists()) {
        	oldLogFile.delete();
        }
        
        logger = Logger.getLogger(fileName);
        try {
            fileHandler = new FileHandler(fileName);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // get singleton instance
    public static synchronized Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    // record log
    public synchronized void log(LogType logType, String message) {
        switch (logType) {
            case PASSENGER_JOIN_QUEUE:
                logger.log(Level.INFO, "[USER JOIN QUEUE] " + message);
                break;
            case PASSENGER_CHECKING_IN:
                logger.log(Level.INFO, "[USER CHECKING IN] " + message);
                break;
            case PASSENGER_BOARDING:
                logger.log(Level.INFO, "[USER BOARDING] " + message);
                break;
            case PLANE_DEPARTURE:
                logger.log(Level.INFO, "[PLANE DEPARTURE] " + message);
                break;
            default:
                logger.log(Level.INFO, "[UNKNOWN] " + message);
                break;
        }
    }
    
    public String getFileName() {
    	return fileName;
    }
}

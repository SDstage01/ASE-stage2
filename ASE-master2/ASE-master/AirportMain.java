
public class AirportMain {

	public static void main(String[] args){
		
    	System.setProperty("java.util.logging.config.file", "logging.properties");
    	// Set log in English
    	System.setProperty("user.language", "en");
    	// Set time zone for log
    	System.setProperty("user.timezone", "GMT");
		
		GuiFrame gui = new GuiFrame();
		gui.setVisible(true);
	}
}

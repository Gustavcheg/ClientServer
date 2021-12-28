package gusel;

import java.io.*;
import java.util.Properties;

/**
 * Class Application config
 * Class contains some constants
 */
public class ApplicationConfig {
    private static final String PROPERTIES_FILE = "clientserver.properties";

    public static int PORT;
    public static String IP;
    public static int HISTORY_LENGTH;

    static {
        // Get properties from prop file
        Properties properties = new Properties();
        File file = new File(PROPERTIES_FILE);
        FileInputStream readPropertiesFile = null;
        try {
            readPropertiesFile = new FileInputStream(file.getAbsoluteFile());
            properties.load(readPropertiesFile);

            // Get constants
            PORT = Integer.parseInt(properties.getProperty("PORT"));
            IP = properties.getProperty("IP");
            HISTORY_LENGTH = Integer.parseInt(properties.getProperty("HISTORY_LENGTH"));

        } catch (IOException ex){
            System.err.println("Error while finding properties file!");
            ex.printStackTrace();
        }
    }

}

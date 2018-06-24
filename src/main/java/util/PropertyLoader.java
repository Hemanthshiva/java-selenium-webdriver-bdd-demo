package util;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertyLoader {

    private static final Logger LOGGER = Logger.getLogger(PropertyLoader.class.getName());
    private static final String CONFIG_FIlE = "/config.properties";

    public static String loadProperty(String name) {
        Properties props = new Properties();
        try {
            props.load(PropertyLoader.class.getResourceAsStream(CONFIG_FIlE));
            LOGGER.info("Loading properties from " + CONFIG_FIlE);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info("Unable to load properties from " + CONFIG_FIlE);
        }

        String value = "";

        if (name != null) {
            value = props.getProperty(name);
        }
        return value;
    }
}

package hr.djajcevic.spc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author djajcevic | 09.08.2015.
 */
public class Configuration {

    private static Properties properties;

    private static final FileInputStream reader;
    private static final FileOutputStream writer;

    static {
        properties = new Properties();
        try {
            String configurationFileName = "configuration.properties";
            String statusFileName = "status.properties";
            URL configurationFile = Configuration.class.getClassLoader().getResource(configurationFileName);
            URL resourceLocation = Configuration.class.getClassLoader().getResource("");
            assert configurationFile != null;
            String configurationFilePath = configurationFile.getFile();

            assert resourceLocation != null;
            String statusFilePath = resourceLocation.getFile() + statusFileName;
            File statusFile = new File(statusFilePath);
            if (!statusFile.exists()) {
                statusFile.createNewFile();
            }
            reader = new FileInputStream(configurationFilePath);

            writer = new FileOutputStream(statusFile);
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException("configuration.properties could not be found", e);
        }
    }

    public static String getConfig(String name) {
        String property = properties.getProperty(name);
        if (property == null) {
            throw new RuntimeException("Configuration property '" + name + "' not found!");
        }
        return property;
    }

    public static int getConfigInt(String name) {
        return Integer.parseInt(getConfig(name));
    }

    public static float getConfigFloat(String name) {
        return Float.parseFloat(getConfig(name));
    }

    public static void saveStatus(String name, Object value) {
        String string = value != null ? value.toString() : "";
        properties.setProperty(name, string);
        try {
            properties.store(writer, null);
        } catch (IOException e) {
            throw new RuntimeException("Error while saving configuration '" + name + "' with value '" + value + "'");
        }
    }

}

package hr.djajcevic.spc.util;

import hr.djajcevic.spc.ioio.looper.compas.CompassData;
import hr.djajcevic.spc.ioio.looper.gps.GPSData;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * @author djajcevic | 09.08.2015.
 */
public class Configuration {

    public static final String GPS_LATITUDE = "gps.latitude";
    public static final String GPS_LATITUDE_DIRECTION = "gps.latitude.direction";
    public static final String GPS_LONGITUDE = "gps.longitude";
    public static final String GPS_LONGITUDE_DIRECTION = "gps.longitude.direction";
    public static final String GPS_ALTITUDE = "gps.altitude";
    public static final String SERVO_X_CURRENT_STEP = "servo.X.currentStep";
    public static final String SERVO_Y_CURRENT_STEP = "servo.Y.currentStep";
    public static final String SERVO_X_STEP_DEGREE = "servo.X.stepDegree";
    public static final String SERVO_Y_STEP_DEGREE = "servo.Y.stepDegree";

    private static final FileInputStream reader;
    private static final File statusPropertiesFile;
    private static final FileOutputStream writer;
    public static final String COMPASS_HEADING = "compass.heading";
    public static final String COMPASS_HEADING_DEGREES = "compass.headingDegrees";
    public static final String COMPASS_X = "compass.x";
    public static final String COMPASS_Y = "compass.y";
    public static final String COMPASS_Z = "compass.z";
    private static Properties properties;
    private static Properties statusProperties;

    static {
        properties = new Properties();
        statusProperties = new Properties();
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
            statusPropertiesFile = statusFile;
            if (!statusFile.exists()) {
                statusFile.createNewFile();
            }
            FileReader inStream = new FileReader(statusFile);
            statusProperties.load(inStream);

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

    public static boolean getConfigBoolean(final String name) {
       return Boolean.valueOf(getConfig(name));
    }

    public static void setStatus(String name, Object value, final boolean save) {
        String string = value != null ? value.toString() : "";
        statusProperties.setProperty(name, string);
        if (save) {
            storeStatusProperties();
        }
    }

    private static synchronized void storeStatusProperties() {
        try {
            statusProperties.store(new FileWriter(statusPropertiesFile, false), null);
        } catch (IOException e) {
            throw new RuntimeException("Error while saving status properties", e);
        }
    }

    public static synchronized void saveCurrentXStep(int currentStep) {
        setStatus(SERVO_X_CURRENT_STEP, "" + currentStep, true);
    }

    public static synchronized void saveCurrentYStep(int currentStep) {
        setStatus(SERVO_Y_CURRENT_STEP, "" + currentStep, true);
    }

    public static void saveGPSData(GPSData data) {
        setStatus(GPS_LATITUDE, "" + data.getLatitude(), false);
        setStatus(GPS_LATITUDE_DIRECTION, "" + data.getLatitudeDirection(), false);
        setStatus(GPS_LONGITUDE, "" + data.getLongitude(), false);
        setStatus(GPS_LONGITUDE_DIRECTION, "" + data.getLongitudeDirection(), false);
        setStatus(GPS_ALTITUDE, "" + data.getAltitude(), false);
        storeStatusProperties();
    }

    public static void loadGPSData(GPSData data) {
        data.setLatitude(getStatusDouble(GPS_LATITUDE));
        data.setLatitudeDirection(getStatus(GPS_LATITUDE_DIRECTION));
        data.setLongitude(getStatusDouble(GPS_LONGITUDE));
        data.setLongitudeDirection(getStatus(GPS_LONGITUDE_DIRECTION));
        data.setAltitude(getStatusDouble(GPS_ALTITUDE));
        storeStatusProperties();
    }

    public static void saveCompassData(CompassData data) {
        setStatus(COMPASS_HEADING, "" + data.getHeading(), false);
        setStatus(COMPASS_HEADING_DEGREES, "" + data.getHeadingDegrees(), false);
        setStatus(COMPASS_X, "" + data.getX(), false);
        setStatus(COMPASS_Y, "" + data.getY(), false);
        setStatus(COMPASS_Z, "" + data.getZ(), false);
        storeStatusProperties();
    }

    public static void loadCompassData(CompassData data) {
        data.setHeading(getStatusDouble(COMPASS_HEADING));
        data.setHeadingDegrees(getStatusDouble(COMPASS_HEADING_DEGREES));
        data.setX(getStatusDouble(COMPASS_X));
        data.setY(getStatusDouble(COMPASS_Y));
        data.setZ(getStatusDouble(COMPASS_Z));
    }

    public static boolean hasText(String value) {
        return value != null && value.length() > 0;
    }

    public static String getStatus(String name) {
        return statusProperties.getProperty(name);
    }

    public static Boolean getStatusBoolean(String name, final Boolean defaultValue) {
        String value = statusProperties.getProperty(name);
        if (hasText(value)) {
            return Boolean.valueOf(value);
        }
        return defaultValue;
    }

    public static Integer getStatusInteger(String name, final Integer defaultValue) {
        String value = statusProperties.getProperty(name);
        if (hasText(value)) {
            return Integer.parseInt(value);
        }
        return defaultValue;
    }

    public static Double getStatusDouble(String name) {
        String value = statusProperties.getProperty(name);
        if (hasText(value)) {
            return Double.parseDouble(value);
        }
        return null;
    }


}

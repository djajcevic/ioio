package hr.djajcevic.spc;

import hr.djajcevic.spc.ioio.looper.Util;
import hr.djajcevic.spc.ioio.looper.gps.GPSData;
import hr.djajcevic.spc.ioio.looper.gps.GPSReader;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author djajcevic | 11.08.2015.
 */
public class GPSReaderTests {

    @Test
    public void parse() {
        String data[] = new String[]{
                "$GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,0000*47",
                "$GPGGA,162555.373,,,,,0,00,,,M,0.0,M,,0000*51",
                "$GPGGA,162556.385,,,,,0,00,,,M,0.0,M,,0000*5B",
                "$GPGGA,235317.000,4003.9039,N,10512.5793,W,1,08,1.6,1577.9,M,-20.7,M,,0000*5F"
        };

        Object assertData[][] = new Object[][]{
                {Util.extractCurrentCalendar("123519"), 48.07037999999999, "N", 11.31, "E", 545.4},
                {Util.extractCurrentCalendar("162555"), 0.0, "", 0.0, "", 0.0},
                {Util.extractCurrentCalendar("162556"), 0.0, "", 0.0, "", 0.0},
                {Util.extractCurrentCalendar("235317"), 40.039038999999995, "N", 105.12579299999999, "W", 1577.9},
        };

        for (int i = 0; i < data.length; i++) {
            GPSData gpsData = GPSReader.extractGpsData(data[i].split(","));
            System.out.println(gpsData);
            Assert.assertEquals(assertData[i][0], gpsData.getTime());
            Assert.assertEquals(assertData[i][1], gpsData.getLatitude());
            Assert.assertEquals(assertData[i][2], gpsData.getLatitudeDirection());
            Assert.assertEquals(assertData[i][3], gpsData.getLongitude());
            Assert.assertEquals(assertData[i][4], gpsData.getLongitudeDirection());
            Assert.assertEquals(assertData[i][5], gpsData.getAltitude());
        }
    }
}

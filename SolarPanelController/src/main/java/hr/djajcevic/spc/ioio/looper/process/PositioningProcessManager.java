package hr.djajcevic.spc.ioio.looper.process;

import hr.djajcevic.spc.calculator.SunPositionData;
import hr.djajcevic.spc.ioio.looper.AxisController;
import hr.djajcevic.spc.util.Configuration;
import ioio.lib.api.exception.ConnectionLostException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author djajcevic | 11.08.2015.
 */
public class PositioningProcessManager extends AbstractProcessManager {

    private AxisController xAxisController;
    private AxisController yAxisController;

    private SunPositionData sunPositionData;

    private int xAxisStepDegree = 1;
    private int yAxisStepDegree = 1;

    @Override
    public void initialize() throws ConnectionLostException {
        xAxisController = managerRepository.getXAxisController();
        yAxisController = managerRepository.getYAxisController();
        sunPositionData = managerRepository.getSunPositionData();
        xAxisStepDegree = Configuration.getConfigInt(Configuration.SERVO_X_STEP_DEGREE);
        yAxisStepDegree = Configuration.getConfigInt(Configuration.SERVO_Y_STEP_DEGREE);
    }

    @Override
    public void performManagementActions() throws ConnectionLostException, InterruptedException {
        Double headingDegrees = managerRepository.getCompassData().getHeadingDegrees();
        Double azimuth = sunPositionData.azimuth;

        SunPositionData sunPositionData = managerRepository.getSunPositionData();

        Calendar calendar = GregorianCalendar.getInstance();
        TimeZone aDefault = TimeZone.getDefault();
        boolean b = aDefault.inDaylightTime(new Date());
        int correction = b ? 1 : 0;
        calendar.setTimeZone(aDefault);

        double min = 60.0 * (sunPositionData.sunrise - (int) (sunPositionData.sunrise));
        double sec = 60.0 * (min - (int) min);
        calendar.set(Calendar.HOUR_OF_DAY, (int) sunPositionData.sunrise + correction);
        calendar.set(Calendar.MINUTE, (int) min);
        calendar.set(Calendar.SECOND, (int) sec);
        Calendar sunrise = Calendar.getInstance();
        sunrise.setTime(calendar.getTime());
        System.out.println("Sunrise: " + calendar.getTime());
        System.out.println("Sunrise: " + sunrise.getTime());

        min = 60.0 * (sunPositionData.sunset - (int) (sunPositionData.sunset));
        sec = 60.0 * (min - (int) min);
        calendar.set(Calendar.HOUR_OF_DAY, (int) sunPositionData.sunset + correction);
        calendar.set(Calendar.MINUTE, (int) min);
        calendar.set(Calendar.SECOND, (int) sec);
        Calendar sunset = Calendar.getInstance();
        sunset.setTime(calendar.getTime());
        System.out.println("Sunset: " + calendar.getTime());
        System.out.println("Sunset: " + sunset.getTime());

        Calendar gpsTime = managerRepository.getGpsData().getTime();

        System.out.println("Current time: " + gpsTime.getTime());

        if (gpsTime.after(sunset)) {
            System.out.printf("passed sunset");
        } else if (gpsTime.before(sunrise)) {
            System.out.printf("before sunrise");
            return;
        }

        if (azimuth > 180) {
            azimuth = azimuth - 360;
        }

        if (headingDegrees > azimuth) {
            headingDegrees = 360 - headingDegrees + 90;
        }

        System.out.println("Real azimuth: " + azimuth);
        int nextPositionAngle = (int) (headingDegrees + azimuth);

        if (nextPositionAngle > 360) {
            nextPositionAngle = 360 - nextPositionAngle;
        }

        System.out.println("Next position angle: " + nextPositionAngle);

        int nextPositionStepCount = nextPositionAngle / xAxisStepDegree;

        int maxSteps = xAxisController.getMaxSteps();
        int currentStep = xAxisController.getCurrentStep();
        int finalStepCount = nextPositionStepCount;

        int nextPositionStepCountAbs = Math.abs(nextPositionStepCount);
        if (nextPositionStepCountAbs > 0) {

            if (nextPositionStepCount > 0) {
                if ((currentStep + nextPositionStepCount) > maxSteps) {
                    finalStepCount = -((360 - nextPositionAngle) / xAxisStepDegree);
                } else {
                    finalStepCount = nextPositionStepCount;
                }
            } else {
                if ((currentStep - nextPositionStepCountAbs) < 0) {
                    finalStepCount = Math.abs((360 - nextPositionStepCountAbs) / xAxisStepDegree);
                } else {
                    finalStepCount = nextPositionStepCount;
                }
            }
        }

        xAxisController.move(finalStepCount > 0, finalStepCount);
        Configuration.saveCurrentXStep(xAxisController.getCurrentStep());
    }


}

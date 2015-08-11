package hr.djajcevic.spc.ioio.looper;

import java.util.Calendar;

/**
 * @author djajcevic | 11.08.2015.
 */
public class Util {

    public static <T> T safeNumberValue(String value, Class<T> returnType) {
        if (value != null && value.length() > 0) {
            if (Long.class.isAssignableFrom(returnType)) {
                return returnType.cast(Long.parseLong(value));
            } else if (Double.class.isAssignableFrom(returnType)) {
                return returnType.cast(Double.parseDouble(value));
            }
        }
        try {
            return returnType.getConstructor(String.class).newInstance("0");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Extracts Calendar with current date and sets time to value from parameter
     * @param time expected format in `hhmmss`
     * @return Calendar set to provided time
     */
    public static Calendar extractCurrentCalendar(final String time) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0,2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(2,4)));
        calendar.set(Calendar.SECOND, Integer.parseInt(time.substring(4,6)));
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

}

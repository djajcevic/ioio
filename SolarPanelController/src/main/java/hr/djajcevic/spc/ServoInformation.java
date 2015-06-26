package hr.djajcevic.spc;

import lombok.Data;

/**
 * @author djajcevic | 25.06.2015.
 */
@Data
public class ServoInformation {

    /**
     * X axis servo motor position (min 0, max ~360)
     */
    private int xServoPosition;

    /**
     * Y axis servo motor position (min 0, max ~135)
     */
    private int yServoPosition;

    /**
     * X axis servo motor North position
     */
    private int xNorthPosition;


}

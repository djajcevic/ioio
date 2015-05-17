package hr.djajcevic.aee;

/**
 * @author djajcevic | 17.05.2015.
 */
public class GrayCodeUtil {

    /**
     * g = gray, b = binary
     * <br/>
     * g = b xor (b logically right shifted 1 time)
     *
     * @param natural
     * @return
     */
    public static long grayEncode(long natural) {
        long code = natural ^ (natural >>> 1);
        return code;
    }

    /**
     * @param natural the number to encode
     * @param resolution the byte number
     * @return boolean[0] = LSB
     */
    public static boolean[] grayEncode(long natural, int resolution) {
        long code = grayEncode(natural);
        String[] binaryString = new StringBuilder(Long.toBinaryString(code)).reverse().toString().split("");
        boolean[] result = new boolean[resolution];
        int binaryResultLength = binaryString.length;
        for (int i = 0; i < resolution; i++) {
            if (i < binaryResultLength) {
                String value = binaryString[i];
                result[i] = value.equals("1");
            }
        }
        return result;
    }

    /**
     * MSB is bit 0, b is binary, g is Gray code
     * <br/>
     * b[0] = g[0]
     * <br/>
     * for other bits:
     * b[i] = g[i] xor b[i-1]
     *
     * @param code
     * @return long natural number of a gray code
     */
    public static long grayDecode(long code) {
        long p = code;
        while ((code >>>= 1) != 0)
            p ^= code;
        return p;
    }

    /**
     * Using value[0] as LSB
     *
     * @param value gray code
     * @return the natural value
     */
    public static long grayDecode(boolean[] value) {
        String codeString = fromMSPtoLSB(value);
        long code = Long.valueOf(codeString, 2);
        return grayDecode(code);
    }

    /**
     * @param value boolean array
     * @return "0011" like string
     */
    public static String fromBooleanArray(boolean[] value) {
        String resultString = "";
        for (final boolean aValue : value) {
            resultString += aValue ? "1" : "0";
        }
        return resultString;
    }

    /**
     * Same as fromBooleanArray but reversed.
     * @param value boolean array
     * @return "1100" like string
     * @see #fromBooleanArray(boolean[])
     */
    public static String fromMSPtoLSB(boolean[] value) {
        String resultString = fromBooleanArray(value);
        return new StringBuilder(resultString).reverse().toString();
    }
}

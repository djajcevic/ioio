package hr.djajcevic.aee;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author djajcevic | 17.05.2015.
 */
public class GrayCodeUtilTest {

    private Map<Long, String[]> table = new HashMap<Long, String[]>() {
        private static final long serialVersionUID = -4051863669843591644L;

        {
            put(0L, new String[]{"00000", "00000"});
            put(1L, new String[]{"00001", "00001"});
            put(2L, new String[]{"00010", "00011"});
            put(3L, new String[]{"00011", "00010"});
            put(4L, new String[]{"00100", "00110"});
            put(5L, new String[]{"00101", "00111"});
            put(6L, new String[]{"00110", "00101"});
            put(7L, new String[]{"00111", "00100"});
            put(8L, new String[]{"01000", "01100"});
            put(9L, new String[]{"01001", "01101"});
            put(10L, new String[]{"01010", "01111"});
            put(11L, new String[]{"01011", "01110"});
            put(12L, new String[]{"01100", "01010"});
            put(13L, new String[]{"01101", "01011"});
            put(14L, new String[]{"01110", "01001"});
            put(15L, new String[]{"01111", "01000"});
            put(16L, new String[]{"10000", "11000"});
            put(17L, new String[]{"10001", "11001"});
            put(18L, new String[]{"10010", "11011"});
            put(19L, new String[]{"10011", "11010"});
            put(20L, new String[]{"10100", "11110"});
            put(21L, new String[]{"10101", "11111"});
            put(22L, new String[]{"10110", "11101"});
            put(23L, new String[]{"10111", "11100"});
            put(24L, new String[]{"11000", "10100"});
            put(25L, new String[]{"11001", "10101"});
            put(26L, new String[]{"11010", "10111"});
            put(27L, new String[]{"11011", "10110"});
            put(28L, new String[]{"11100", "10010"});
            put(29L, new String[]{"11101", "10011"});
            put(30L, new String[]{"11110", "10001"});
            put(31L, new String[]{"11111", "10000"});
        }
    };

    @Test
    public void testEncode() {
        for (Map.Entry<Long, String[]> entry : table.entrySet()) {
            long natural = entry.getKey();
            String[] values = entry.getValue();
            String binary = values[0];
            String gray = values[1];
            boolean[] encodeResult = GrayCodeUtil.grayEncode(natural, 5);
            System.out.println("Testing (" + natural + " = b[" + binary + "], g[" + gray + "]) -> " + Arrays.toString(encodeResult));

            String encodeResultString = GrayCodeUtil.fromMSPtoLSB(encodeResult);
            Assert.assertEquals("Encoding " + natural, gray, encodeResultString);


            long decodeResultLong = GrayCodeUtil.grayDecode(encodeResult); // natural
            Assert.assertEquals("Decoding " + encodeResultString, natural, decodeResultLong);

        }
    }
}

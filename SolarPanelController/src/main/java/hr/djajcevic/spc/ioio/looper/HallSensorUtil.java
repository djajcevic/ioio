package hr.djajcevic.spc.ioio.looper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author djajcevic | 16.08.2015.
 */
public class HallSensorUtil {

    public static final List<boolean[]> POSSIBLE_COMBINATIONS_BOOLEAN = new ArrayList<boolean[]>() {{
        add(new boolean[]{true, false, false});   // 100
        add(new boolean[]{true, false, true});    // 101
        add(new boolean[]{false, false, true});   // 001
        add(new boolean[]{false, true, true});    // 011
        add(new boolean[]{false, true, false});   // 010
        add(new boolean[]{true, true, false});    // 110
    }};

    public static final List<int[]> POSSIBLE_COMBINATIONS_INT = new ArrayList<int[]>() {{
        add(new int[]{1, 0, 0});
        add(new int[]{1, 0, 1});
        add(new int[]{0, 0, 1});
        add(new int[]{0, 1, 1});
        add(new int[]{0, 1, 0});
        add(new int[]{1, 1, 0});
    }};

    public static final LinkedHashMap<String, int[]> POSSIBLE_COMBINATIONS_MAP = new LinkedHashMap<String, int[]>() {{
        put("100", POSSIBLE_COMBINATIONS_INT.get(0));
        put("101", POSSIBLE_COMBINATIONS_INT.get(1));
        put("001", POSSIBLE_COMBINATIONS_INT.get(2));
        put("011", POSSIBLE_COMBINATIONS_INT.get(3));
        put("010", POSSIBLE_COMBINATIONS_INT.get(4));
        put("110", POSSIBLE_COMBINATIONS_INT.get(5));
    }};
}

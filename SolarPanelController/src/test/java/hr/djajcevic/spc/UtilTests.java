package hr.djajcevic.spc;

import hr.djajcevic.spc.util.Configuration;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author djajcevic | 12.08.2015.
 */
public class UtilTests {

    @Test
    public void saveAndLoadStatusProperties() {
        String test = "test";
        String status = Configuration.getStatus(test);

        Configuration.setStatus(test, "hello", true);

        status = Configuration.getStatus(test);

        Configuration.setStatus(test, "hello2", false);
        Configuration.setStatus(test, "hello3", true);

        status = Configuration.getStatus(test);

        Assert.assertEquals("hello3", status);

        String firstRun = "firstRun";
        Configuration.setStatus(firstRun, "true", true);

        Boolean statusBoolean = Configuration.getStatusBoolean(firstRun);
        Assert.assertNotNull(statusBoolean);
        Assert.assertTrue(statusBoolean);

    }
}

package org.jfree.chart.axis;

import java.util.TimeZone;
import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuarterDateFormatTestTest4 {

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        QuarterDateFormat qf1 = new QuarterDateFormat(TimeZone.getTimeZone("GMT"), new String[] { "1", "2", "3", "4" });
        QuarterDateFormat qf2 = TestUtils.serialised(qf1);
        assertEquals(qf1, qf2);
    }
}

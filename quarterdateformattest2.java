package org.jfree.chart.axis;

import java.util.TimeZone;
import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuarterDateFormatTestTest2 {

    /**
     * Two objects that are equal are required to return the same hashCode.
     */
    @Test
    public void testHashCode() {
        QuarterDateFormat qf1 = new QuarterDateFormat(TimeZone.getTimeZone("GMT"), new String[] { "1", "2", "3", "4" });
        QuarterDateFormat qf2 = new QuarterDateFormat(TimeZone.getTimeZone("GMT"), new String[] { "1", "2", "3", "4" });
        assertEquals(qf1, qf2);
        int h1 = qf1.hashCode();
        int h2 = qf2.hashCode();
        assertEquals(h1, h2);
    }
}

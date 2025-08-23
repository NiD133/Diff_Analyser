package org.jfree.chart.axis;

import java.util.TimeZone;
import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuarterDateFormatTestTest1 {

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        QuarterDateFormat qf1 = new QuarterDateFormat(TimeZone.getTimeZone("GMT"), new String[] { "1", "2", "3", "4" });
        QuarterDateFormat qf2 = new QuarterDateFormat(TimeZone.getTimeZone("GMT"), new String[] { "1", "2", "3", "4" });
        assertEquals(qf1, qf2);
        assertEquals(qf2, qf1);
        qf1 = new QuarterDateFormat(TimeZone.getTimeZone("PST"), new String[] { "1", "2", "3", "4" });
        assertNotEquals(qf1, qf2);
        qf2 = new QuarterDateFormat(TimeZone.getTimeZone("PST"), new String[] { "1", "2", "3", "4" });
        assertEquals(qf1, qf2);
        qf1 = new QuarterDateFormat(TimeZone.getTimeZone("PST"), new String[] { "A", "2", "3", "4" });
        assertNotEquals(qf1, qf2);
        qf2 = new QuarterDateFormat(TimeZone.getTimeZone("PST"), new String[] { "A", "2", "3", "4" });
        assertEquals(qf1, qf2);
        qf1 = new QuarterDateFormat(TimeZone.getTimeZone("PST"), new String[] { "A", "2", "3", "4" }, true);
        assertNotEquals(qf1, qf2);
        qf2 = new QuarterDateFormat(TimeZone.getTimeZone("PST"), new String[] { "A", "2", "3", "4" }, true);
        assertEquals(qf1, qf2);
    }
}

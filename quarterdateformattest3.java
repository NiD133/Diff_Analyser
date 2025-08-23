package org.jfree.chart.axis;

import java.util.TimeZone;
import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuarterDateFormatTestTest3 {

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() {
        QuarterDateFormat qf1 = new QuarterDateFormat(TimeZone.getTimeZone("GMT"), new String[] { "1", "2", "3", "4" });
        QuarterDateFormat qf2 = null;
        qf2 = (QuarterDateFormat) qf1.clone();
        assertNotSame(qf1, qf2);
        assertSame(qf1.getClass(), qf2.getClass());
        assertEquals(qf1, qf2);
    }
}

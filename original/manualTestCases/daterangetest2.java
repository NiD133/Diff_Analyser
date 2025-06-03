package org.jfree.data.time;

import java.util.Date;
import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeneratedTestCase {

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        DateRange r1 = new DateRange(new Date(1000L), new Date(2000L));
        DateRange r2 = TestUtils.serialised(r1);
        assertEquals(r1, r2);
    }
}

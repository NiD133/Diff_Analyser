package org.jfree.data.time;

import java.util.Date;
import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeneratedTestCase {

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        DateRange r1 = new DateRange(new Date(1000L), new Date(2000L));
        DateRange r2 = new DateRange(new Date(1000L), new Date(2000L));
        assertEquals(r1, r2);
        assertEquals(r2, r1);
        r1 = new DateRange(new Date(1111L), new Date(2000L));
        assertNotEquals(r1, r2);
        r2 = new DateRange(new Date(1111L), new Date(2000L));
        assertEquals(r1, r2);
        r1 = new DateRange(new Date(1111L), new Date(2222L));
        assertNotEquals(r1, r2);
        r2 = new DateRange(new Date(1111L), new Date(2222L));
        assertEquals(r1, r2);
    }
}

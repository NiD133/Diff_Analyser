package org.jfree.data.time;

import java.util.Date;
import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeneratedTestCase {

    /**
     * Confirm that a DateRange is immutable.
     */
    @Test
    public void testImmutable() {
        Date d1 = new Date(10L);
        Date d2 = new Date(20L);
        DateRange r = new DateRange(d1, d2);
        d1.setTime(11L);
        assertEquals(new Date(10L), r.getLowerDate());
        r.getUpperDate().setTime(22L);
        assertEquals(new Date(20L), r.getUpperDate());
    }
}

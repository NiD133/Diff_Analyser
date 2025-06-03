package org.jfree.data.time;

import java.util.Date;
import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeneratedTestCase {

    /**
     * The {@link DateRange} class is immutable, so it doesn't need to
     * be cloneable.
     */
    @Test
    public void testClone() {
        DateRange r1 = new DateRange(new Date(1000L), new Date(2000L));
        assertFalse(r1 instanceof Cloneable);
    }
}

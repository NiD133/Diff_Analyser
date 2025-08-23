package org.jfree.data.xy;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class XYIntervalTestTest3 {

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        XYInterval i1 = new XYInterval(1.0, 2.0, 3.0, 2.5, 3.5);
        XYInterval i2 = TestUtils.serialised(i1);
        assertEquals(i1, i2);
    }
}

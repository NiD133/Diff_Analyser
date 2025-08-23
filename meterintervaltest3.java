package org.jfree.chart.plot;

import java.awt.BasicStroke;
import java.awt.Color;
import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MeterIntervalTestTest3 {

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        MeterInterval m1 = new MeterInterval("X", new Range(1.0, 2.0));
        MeterInterval m2 = TestUtils.serialised(m1);
        assertEquals(m1, m2);
    }
}

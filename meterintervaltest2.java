package org.jfree.chart.plot;

import java.awt.BasicStroke;
import java.awt.Color;
import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MeterIntervalTestTest2 {

    /**
     * This class is immutable so cloning isn't required.
     */
    @Test
    public void testCloning() {
        MeterInterval m1 = new MeterInterval("X", new Range(1.0, 2.0));
        assertFalse(m1 instanceof Cloneable);
    }
}

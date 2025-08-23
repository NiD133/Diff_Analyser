package org.jfree.data.xy;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class XYIntervalTestTest2 {

    /**
     * This class is immutable.
     */
    @Test
    public void testCloning() {
        XYInterval i1 = new XYInterval(1.0, 2.0, 3.0, 2.5, 3.5);
        assertFalse(i1 instanceof Cloneable);
    }
}

package org.jfree.chart.renderer.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;

public class StandardBarPainterTestTest4 {

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        StandardBarPainter p1 = new StandardBarPainter();
        StandardBarPainter p2 = TestUtils.serialised(p1);
        assertEquals(p1, p2);
    }
}

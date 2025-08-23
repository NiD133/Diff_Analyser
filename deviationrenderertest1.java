package org.jfree.chart.renderer.xy;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeviationRendererTestTest1 {

    /**
     * Test that the equals() method distinguishes all fields.
     */
    @Test
    public void testEquals() {
        // default instances
        DeviationRenderer r1 = new DeviationRenderer();
        DeviationRenderer r2 = new DeviationRenderer();
        assertEquals(r1, r2);
        assertEquals(r2, r1);
        r1.setAlpha(0.1f);
        assertNotEquals(r1, r2);
        r2.setAlpha(0.1f);
        assertEquals(r1, r2);
    }
}

package org.jfree.chart.renderer.xy;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeviationRendererTestTest5 {

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        DeviationRenderer r1 = new DeviationRenderer();
        DeviationRenderer r2 = TestUtils.serialised(r1);
        assertEquals(r1, r2);
    }
}

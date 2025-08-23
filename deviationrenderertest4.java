package org.jfree.chart.renderer.xy;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeviationRendererTestTest4 {

    /**
     * Verify that this class implements {@link PublicCloneable}.
     */
    @Test
    public void testPublicCloneable() {
        DeviationRenderer r1 = new DeviationRenderer();
        assertTrue(r1 instanceof PublicCloneable);
    }
}

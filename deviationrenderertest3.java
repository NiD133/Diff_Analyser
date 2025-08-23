package org.jfree.chart.renderer.xy;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeviationRendererTestTest3 {

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        DeviationRenderer r1 = new DeviationRenderer();
        DeviationRenderer r2 = (DeviationRenderer) r1.clone();
        assertNotSame(r1, r2);
        assertSame(r1.getClass(), r2.getClass());
        assertEquals(r1, r2);
    }
}

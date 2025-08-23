package org.jfree.chart.renderer.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;

public class StandardBarPainterTestTest3 {

    /**
     * Confirm that cloning isn't implemented (it isn't required, because
     * instances of the class are immutable).
     */
    @Test
    public void testCloning() {
        StandardBarPainter p1 = new StandardBarPainter();
        assertFalse(p1 instanceof Cloneable);
        assertFalse(p1 instanceof PublicCloneable);
    }
}

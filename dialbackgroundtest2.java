package org.jfree.chart.plot.dial;

import java.awt.Color;
import java.awt.GradientPaint;
import org.jfree.chart.TestUtils;
import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DialBackgroundTestTest2 {

    /**
     * Two objects that are equal are required to return the same hashCode.
     */
    @Test
    public void testHashCode() {
        DialBackground b1 = new DialBackground(Color.RED);
        DialBackground b2 = new DialBackground(Color.RED);
        assertEquals(b1, b2);
        int h1 = b1.hashCode();
        int h2 = b2.hashCode();
        assertEquals(h1, h2);
    }
}

package org.jfree.chart.plot.dial;

import java.awt.Color;
import java.awt.GradientPaint;
import org.jfree.chart.TestUtils;
import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DialBackgroundTestTest4 {

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        // test a default instance
        DialBackground b1 = new DialBackground();
        DialBackground b2 = TestUtils.serialised(b1);
        assertEquals(b1, b2);
        // test a customised instance
        b1 = new DialBackground();
        b1.setPaint(new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.GREEN));
        b1.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));
        b2 = TestUtils.serialised(b1);
        assertEquals(b1, b2);
    }
}

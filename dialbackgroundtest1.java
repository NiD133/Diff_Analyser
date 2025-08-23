package org.jfree.chart.plot.dial;

import java.awt.Color;
import java.awt.GradientPaint;
import org.jfree.chart.TestUtils;
import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DialBackgroundTestTest1 {

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        DialBackground b1 = new DialBackground();
        DialBackground b2 = new DialBackground();
        assertEquals(b1, b2);
        // paint
        b1.setPaint(new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.YELLOW));
        assertNotEquals(b1, b2);
        b2.setPaint(new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.YELLOW));
        assertEquals(b1, b2);
        // gradient paint transformer
        b1.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));
        assertNotEquals(b1, b2);
        b2.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));
        assertEquals(b1, b2);
        // check an inherited attribute
        b1.setVisible(false);
        assertNotEquals(b1, b2);
        b2.setVisible(false);
        assertEquals(b1, b2);
    }
}

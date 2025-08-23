package org.jfree.chart.annotations;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Stroke;
import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class XYLineAnnotationTestTest7 {

    private static final double EPSILON = 0.000000001;

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        Stroke stroke = new BasicStroke(2.0f);
        XYLineAnnotation a1 = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, stroke, Color.BLUE);
        XYLineAnnotation a2 = TestUtils.serialised(a1);
        assertEquals(a1, a2);
    }
}

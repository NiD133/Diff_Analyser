package org.jfree.chart.annotations;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Stroke;
import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class XYLineAnnotationTestTest3 {

    private static final double EPSILON = 0.000000001;

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        Stroke stroke = new BasicStroke(2.0f);
        XYLineAnnotation a1 = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, stroke, Color.BLUE);
        XYLineAnnotation a2 = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, stroke, Color.BLUE);
        assertEquals(a1, a2);
        assertEquals(a2, a1);
        a1 = new XYLineAnnotation(11.0, 20.0, 100.0, 200.0, stroke, Color.BLUE);
        assertNotEquals(a1, a2);
        a2 = new XYLineAnnotation(11.0, 20.0, 100.0, 200.0, stroke, Color.BLUE);
        assertEquals(a1, a2);
        a1 = new XYLineAnnotation(11.0, 21.0, 100.0, 200.0, stroke, Color.BLUE);
        assertNotEquals(a1, a2);
        a2 = new XYLineAnnotation(11.0, 21.0, 100.0, 200.0, stroke, Color.BLUE);
        assertEquals(a1, a2);
        a1 = new XYLineAnnotation(11.0, 21.0, 101.0, 200.0, stroke, Color.BLUE);
        assertNotEquals(a1, a2);
        a2 = new XYLineAnnotation(11.0, 21.0, 101.0, 200.0, stroke, Color.BLUE);
        assertEquals(a1, a2);
        a1 = new XYLineAnnotation(11.0, 21.0, 101.0, 201.0, stroke, Color.BLUE);
        assertNotEquals(a1, a2);
        a2 = new XYLineAnnotation(11.0, 21.0, 101.0, 201.0, stroke, Color.BLUE);
        assertEquals(a1, a2);
        Stroke stroke2 = new BasicStroke(0.99f);
        a1 = new XYLineAnnotation(11.0, 21.0, 101.0, 200.0, stroke2, Color.BLUE);
        assertNotEquals(a1, a2);
        a2 = new XYLineAnnotation(11.0, 21.0, 101.0, 200.0, stroke2, Color.BLUE);
        assertEquals(a1, a2);
        GradientPaint g1 = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.WHITE);
        GradientPaint g2 = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.WHITE);
        a1 = new XYLineAnnotation(11.0, 21.0, 101.0, 200.0, stroke2, g1);
        assertNotEquals(a1, a2);
        a2 = new XYLineAnnotation(11.0, 21.0, 101.0, 200.0, stroke2, g2);
        assertEquals(a1, a2);
    }
}

package org.jfree.chart.annotations;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Stroke;
import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class XYLineAnnotationTestTest2 {

    private static final double EPSILON = 0.000000001;

    @Test
    public void testConstructorExceptions() {
        Stroke stroke = new BasicStroke(2.0f);
        assertThrows(IllegalArgumentException.class, () -> {
            XYLineAnnotation a1 = new XYLineAnnotation(Double.NaN, 20.0, 100.0, 200.0, stroke, Color.BLUE);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            XYLineAnnotation a1 = new XYLineAnnotation(10.0, Double.NaN, 100.0, 200.0, stroke, Color.BLUE);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            XYLineAnnotation a1 = new XYLineAnnotation(10.0, 20.0, Double.NaN, 200.0, stroke, Color.BLUE);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            XYLineAnnotation a1 = new XYLineAnnotation(10.0, 20.0, 100.0, Double.NaN, stroke, Color.BLUE);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            XYLineAnnotation a1 = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, null, Color.BLUE);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            XYLineAnnotation a1 = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, stroke, null);
        });
    }
}

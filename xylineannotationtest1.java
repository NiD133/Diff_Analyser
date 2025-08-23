package org.jfree.chart.annotations;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Stroke;
import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class XYLineAnnotationTestTest1 {

    private static final double EPSILON = 0.000000001;

    @Test
    public void testConstructor() {
        Stroke stroke = new BasicStroke(2.0f);
        XYLineAnnotation a1 = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, stroke, Color.BLUE);
        assertEquals(10.0, a1.getX1(), EPSILON);
        assertEquals(20.0, a1.getY1(), EPSILON);
        assertEquals(100.0, a1.getX2(), EPSILON);
        assertEquals(200.0, a1.getY2(), EPSILON);
        assertEquals(stroke, a1.getStroke());
        assertEquals(Color.BLUE, a1.getPaint());
    }
}

package org.jfree.chart.annotations;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Stroke;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link XYLineAnnotation} class.
 */
public class XYLineAnnotationTest {

    private static final double EPSILON = 1e-9; // Tolerance for floating-point comparisons

    /**
     * Tests the constructor and the getters.
     */
    @Test
    public void testConstructorAndGetters() {
        Stroke stroke = new BasicStroke(2.0f);
        XYLineAnnotation annotation = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, stroke, Color.BLUE);

        assertEquals(10.0, annotation.getX1(), EPSILON);
        assertEquals(20.0, annotation.getY1(), EPSILON);
        assertEquals(100.0, annotation.getX2(), EPSILON);
        assertEquals(200.0, annotation.getY2(), EPSILON);
        assertEquals(stroke, annotation.getStroke());
        assertEquals(Color.BLUE, annotation.getPaint());
    }

    /**
     * Tests that the constructor throws IllegalArgumentException for invalid arguments.
     */
    @Test
    public void testConstructorExceptions() {
        Stroke stroke = new BasicStroke(2.0f);

        assertThrows(IllegalArgumentException.class, () -> new XYLineAnnotation(Double.NaN, 20.0, 100.0, 200.0, stroke, Color.BLUE));
        assertThrows(IllegalArgumentException.class, () -> new XYLineAnnotation(10.0, Double.NaN, 100.0, 200.0, stroke, Color.BLUE));
        assertThrows(IllegalArgumentException.class, () -> new XYLineAnnotation(10.0, 20.0, Double.NaN, 200.0, stroke, Color.BLUE));
        assertThrows(IllegalArgumentException.class, () -> new XYLineAnnotation(10.0, 20.0, 100.0, Double.NaN, stroke, Color.BLUE));
        assertThrows(IllegalArgumentException.class, () -> new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, null, Color.BLUE));
        assertThrows(IllegalArgumentException.class, () -> new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, stroke, null));
    }

    /**
     * Tests the equals method.
     */
    @Test
    public void testEquals() {
        Stroke stroke = new BasicStroke(2.0f);
        XYLineAnnotation annotation1 = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, stroke, Color.BLUE);
        XYLineAnnotation annotation2 = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, stroke, Color.BLUE);

        assertEquals(annotation1, annotation2);
        assertEquals(annotation2, annotation1);

        // Test each field for inequality
        annotation1 = new XYLineAnnotation(11.0, 20.0, 100.0, 200.0, stroke, Color.BLUE);
        assertNotEquals(annotation1, annotation2);
        annotation2 = new XYLineAnnotation(11.0, 20.0, 100.0, 200.0, stroke, Color.BLUE);
        assertEquals(annotation1, annotation2);

        annotation1 = new XYLineAnnotation(11.0, 21.0, 100.0, 200.0, stroke, Color.BLUE);
        assertNotEquals(annotation1, annotation2);
        annotation2 = new XYLineAnnotation(11.0, 21.0, 100.0, 200.0, stroke, Color.BLUE);
        assertEquals(annotation1, annotation2);

        annotation1 = new XYLineAnnotation(11.0, 21.0, 101.0, 200.0, stroke, Color.BLUE);
        assertNotEquals(annotation1, annotation2);
        annotation2 = new XYLineAnnotation(11.0, 21.0, 101.0, 200.0, stroke, Color.BLUE);
        assertEquals(annotation1, annotation2);

        annotation1 = new XYLineAnnotation(11.0, 21.0, 101.0, 201.0, stroke, Color.BLUE);
        assertNotEquals(annotation1, annotation2);
        annotation2 = new XYLineAnnotation(11.0, 21.0, 101.0, 201.0, stroke, Color.BLUE);
        assertEquals(annotation1, annotation2);

        Stroke differentStroke = new BasicStroke(0.99f);
        annotation1 = new XYLineAnnotation(11.0, 21.0, 101.0, 200.0, differentStroke, Color.BLUE);
        assertNotEquals(annotation1, annotation2);
        annotation2 = new XYLineAnnotation(11.0, 21.0, 101.0, 200.0, differentStroke, Color.BLUE);
        assertEquals(annotation1, annotation2);

        GradientPaint gradientPaint1 = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.WHITE);
        GradientPaint gradientPaint2 = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.WHITE);
        annotation1 = new XYLineAnnotation(11.0, 21.0, 101.0, 200.0, differentStroke, gradientPaint1);
        assertNotEquals(annotation1, annotation2);
        annotation2 = new XYLineAnnotation(11.0, 21.0, 101.0, 200.0, differentStroke, gradientPaint2);
        assertEquals(annotation1, annotation2);
    }

    /**
     * Tests that equal objects have the same hash code.
     */
    @Test
    public void testHashCode() {
        Stroke stroke = new BasicStroke(2.0f);
        XYLineAnnotation annotation1 = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, stroke, Color.BLUE);
        XYLineAnnotation annotation2 = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, stroke, Color.BLUE);

        assertEquals(annotation1, annotation2);
        assertEquals(annotation1.hashCode(), annotation2.hashCode());
    }

    /**
     * Tests the clone method.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        Stroke stroke = new BasicStroke(2.0f);
        XYLineAnnotation original = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, stroke, Color.BLUE);
        XYLineAnnotation clone = (XYLineAnnotation) original.clone();

        assertNotSame(original, clone);
        assertSame(original.getClass(), clone.getClass());
        assertEquals(original, clone);
    }

    /**
     * Tests that the class implements PublicCloneable.
     */
    @Test
    public void testPublicCloneable() {
        Stroke stroke = new BasicStroke(2.0f);
        XYLineAnnotation annotation = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, stroke, Color.BLUE);

        assertTrue(annotation instanceof PublicCloneable);
    }

    /**
     * Tests serialization and deserialization.
     */
    @Test
    public void testSerialization() {
        Stroke stroke = new BasicStroke(2.0f);
        XYLineAnnotation original = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, stroke, Color.BLUE);
        XYLineAnnotation deserialized = TestUtils.serialised(original);

        assertEquals(original, deserialized);
    }
}
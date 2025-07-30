package org.jfree.chart.annotations;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import org.jfree.chart.TestUtils;
import org.jfree.chart.Drawable;
import org.jfree.chart.api.PublicCloneable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link XYDrawableAnnotation} class.
 */
public class XYDrawableAnnotationTest {

    /**
     * A simple implementation of the Drawable interface for testing purposes.
     */
    static class TestDrawable implements Drawable, Cloneable, Serializable {
        @Override
        public void draw(Graphics2D g2, Rectangle2D area) {
            // Intentionally left blank for testing
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof TestDrawable;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    /**
     * Tests the equality of {@link XYDrawableAnnotation} instances.
     */
    @Test
    public void testEquals() {
        XYDrawableAnnotation annotation1 = createAnnotation(10.0, 20.0, 100.0, 200.0);
        XYDrawableAnnotation annotation2 = createAnnotation(10.0, 20.0, 100.0, 200.0);
        assertEquals(annotation1, annotation2);

        annotation1 = createAnnotation(11.0, 20.0, 100.0, 200.0);
        assertNotEquals(annotation1, annotation2);
        
        annotation2 = createAnnotation(11.0, 20.0, 100.0, 200.0);
        assertEquals(annotation1, annotation2);

        annotation1 = createAnnotation(11.0, 22.0, 100.0, 200.0);
        assertNotEquals(annotation1, annotation2);
        
        annotation2 = createAnnotation(11.0, 22.0, 100.0, 200.0);
        assertEquals(annotation1, annotation2);

        annotation1 = createAnnotation(11.0, 22.0, 101.0, 200.0);
        assertNotEquals(annotation1, annotation2);
        
        annotation2 = createAnnotation(11.0, 22.0, 101.0, 200.0);
        assertEquals(annotation1, annotation2);

        annotation1 = createAnnotation(11.0, 22.0, 101.0, 202.0);
        assertNotEquals(annotation1, annotation2);
        
        annotation2 = createAnnotation(11.0, 22.0, 101.0, 202.0);
        assertEquals(annotation1, annotation2);

        annotation1 = createAnnotation(11.0, 22.0, 101.0, 202.0, 2.0);
        assertNotEquals(annotation1, annotation2);
        
        annotation2 = createAnnotation(11.0, 22.0, 101.0, 202.0, 2.0);
        assertEquals(annotation1, annotation2);
    }

    /**
     * Tests that equal objects have the same hash code.
     */
    @Test
    public void testHashCode() {
        XYDrawableAnnotation annotation1 = createAnnotation(10.0, 20.0, 100.0, 200.0);
        XYDrawableAnnotation annotation2 = createAnnotation(10.0, 20.0, 100.0, 200.0);
        assertEquals(annotation1.hashCode(), annotation2.hashCode());
    }

    /**
     * Tests the cloning functionality of {@link XYDrawableAnnotation}.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        XYDrawableAnnotation original = createAnnotation(10.0, 20.0, 100.0, 200.0);
        XYDrawableAnnotation clone = (XYDrawableAnnotation) original.clone();
        assertNotSame(original, clone);
        assertEquals(original, clone);
    }

    /**
     * Tests if the class implements {@link PublicCloneable}.
     */
    @Test
    public void testPublicCloneable() {
        XYDrawableAnnotation annotation = createAnnotation(10.0, 20.0, 100.0, 200.0);
        assertTrue(annotation instanceof PublicCloneable);
    }

    /**
     * Tests the serialization and deserialization of {@link XYDrawableAnnotation}.
     */
    @Test
    public void testSerialization() {
        XYDrawableAnnotation original = createAnnotation(10.0, 20.0, 100.0, 200.0);
        XYDrawableAnnotation deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized);
    }

    /**
     * Helper method to create an {@link XYDrawableAnnotation} with default scale factor.
     */
    private XYDrawableAnnotation createAnnotation(double x, double y, double width, double height) {
        return new XYDrawableAnnotation(x, y, width, height, new TestDrawable());
    }

    /**
     * Helper method to create an {@link XYDrawableAnnotation} with specified scale factor.
     */
    private XYDrawableAnnotation createAnnotation(double x, double y, double width, double height, double scaleFactor) {
        return new XYDrawableAnnotation(x, y, width, height, scaleFactor, new TestDrawable());
    }
}
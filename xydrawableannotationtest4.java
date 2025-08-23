package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the {@link XYDrawableAnnotation} class.
 */
class XYDrawableAnnotationTest {

    /**
     * A mock {@link Drawable} for testing purposes. It is cloneable and serializable
     * to satisfy the requirements of annotations that might be cloned or serialized.
     */
    private static class MockDrawable implements Drawable, Cloneable, Serializable {

        @Override
        public void draw(Graphics2D g2, Rectangle2D area) {
            // This mock implementation does nothing, as drawing is not relevant for these tests.
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj instanceof MockDrawable;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    /**
     * Verifies that XYDrawableAnnotation implements the PublicCloneable interface.
     * This test serves as a regression check to ensure the public cloneability
     * contract is maintained.
     */
    @Test
    void verifyImplementsPublicCloneable() {
        // Arrange: Create an instance of the annotation with a mock drawable.
        // The specific constructor arguments are arbitrary and do not affect this test.
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                10.0, 20.0, 100.0, 200.0, new MockDrawable()
        );

        // Act & Assert: Check if the created instance is of type PublicCloneable.
        assertTrue(annotation instanceof PublicCloneable,
                "XYDrawableAnnotation must implement the PublicCloneable interface.");
    }
}
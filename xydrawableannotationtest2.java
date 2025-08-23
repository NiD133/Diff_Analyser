package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.junit.jupiter.api.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link XYDrawableAnnotation} class, focusing on its object contracts.
 */
class XYDrawableAnnotationTest {

    /**
     * A mock {@link Drawable} for testing purposes.
     * <p>
     * Its {@code equals()} method is intentionally lenient, returning true for any two instances
     * of this class. This is crucial for testing the {@code equals()} and {@code hashCode()}
     * contracts of classes that contain a {@code Drawable}, like {@link XYDrawableAnnotation}.
     * The {@code hashCode()} is implemented to be consistent with this behavior.
     */
    private static class MockDrawable implements Drawable, Cloneable, Serializable {

        /**
         * Considers any two {@code MockDrawable} instances to be equal.
         *
         * @param obj the object to test against.
         * @return {@code true} if obj is an instance of {@code MockDrawable}, {@code false} otherwise.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            // All instances of MockDrawable are considered equal for this test's purpose.
            return obj instanceof MockDrawable;
        }

        /**
         * Returns a constant hash code, which is consistent with the {@code equals()} method
         * where all instances are considered equal.
         *
         * @return A constant hash code.
         */
        @Override
        public int hashCode() {
            // A constant value is required to fulfill the contract with the overridden equals().
            return 42;
        }

        @Override
        public void draw(Graphics2D g2, Rectangle2D area) {
            // No operation needed for this test.
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    /**
     * Verifies that two equal XYDrawableAnnotation instances have the same hash code,
     * fulfilling the Object.hashCode() contract.
     */
    @Test
    void hashCode_forEqualObjects_shouldBeEqual() {
        // Arrange: Create two XYDrawableAnnotation objects that are identical in state.
        // The MockDrawable's equals() method ensures the drawable components are also considered equal.
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(10.0, 20.0, 100.0, 200.0, new MockDrawable());
        XYDrawableAnnotation annotation2 = new XYDrawableAnnotation(10.0, 20.0, 100.0, 200.0, new MockDrawable());

        // Assert: First, confirm the objects are indeed equal. This is a precondition for the
        // main test of the hashCode contract.
        assertEquals(annotation1, annotation2, "Annotations with identical properties should be equal.");

        // Act & Assert: Then, verify that their hash codes are also equal.
        assertEquals(annotation1.hashCode(), annotation2.hashCode(), "Equal objects must have equal hash codes.");
    }
}
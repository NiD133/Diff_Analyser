package org.jfree.chart.annotations;

import org.jfree.chart.TestUtils;
import org.jfree.chart.Drawable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link XYDrawableAnnotation} class, focusing on serialization.
 */
class XYDrawableAnnotationTest {

    /**
     * A simple, serializable mock Drawable for testing purposes.
     * All instances of this class are considered equal to each other.
     */
    private static class MockDrawable implements Drawable, Cloneable, Serializable {
        @Override
        public void draw(Graphics2D g2, Rectangle2D area) {
            // This mock implementation does not need to perform any drawing.
        }

        /**
         * Returns true if the object is an instance of MockDrawable, false otherwise.
         */
        @Override
        public boolean equals(Object obj) {
            return obj instanceof MockDrawable;
        }

        /**
         * Returns a consistent hash code for all instances, adhering to the
         * equals/hashCode contract.
         */
        @Override
        public int hashCode() {
            // A constant hash code is appropriate since all instances are equal.
            return 123;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    @Test
    @DisplayName("An XYDrawableAnnotation instance should be correctly serialized and deserialized")
    void serialization_shouldPreserveAnnotationEquality() {
        // Arrange: Create an annotation with specific properties.
        final double x = 10.0;
        final double y = 20.0;
        final double width = 100.0;
        final double height = 200.0;
        final Drawable drawable = new MockDrawable();

        XYDrawableAnnotation originalAnnotation = new XYDrawableAnnotation(x, y, width, height, drawable);

        // Act: Serialize and then deserialize the annotation.
        XYDrawableAnnotation deserializedAnnotation = TestUtils.serialised(originalAnnotation);

        // Assert: The deserialized annotation should be equal to the original.
        assertEquals(originalAnnotation, deserializedAnnotation);
    }
}
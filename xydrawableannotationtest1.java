package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for the {@link XYDrawableAnnotation} class, focusing on its contract and behavior.
 */
@DisplayName("XYDrawableAnnotation")
class XYDrawableAnnotationTest {

    /**
     * A mock implementation of Drawable for testing purposes. It includes an 'id'
     * to allow for meaningful equality checks.
     */
    static class TestDrawable implements Drawable, Cloneable, Serializable {
        private final String id;

        TestDrawable(String id) {
            this.id = id;
        }

        @Override
        public void draw(Graphics2D g2, Rectangle2D area) {
            // No-op for this test
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TestDrawable that = (TestDrawable) obj;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    @Nested
    @DisplayName("equals() contract")
    class EqualsContract {

        private XYDrawableAnnotation baseAnnotation;
        private final Drawable baseDrawable = new TestDrawable("drawable-1");

        @BeforeEach
        void setUp() {
            // Arrange a baseline annotation for comparison in each test.
            // This instance uses the constructor that defaults drawScaleFactor to 1.0.
            baseAnnotation = new XYDrawableAnnotation(10.0, 20.0, 100.0, 200.0, baseDrawable);
        }

        @Test
        @DisplayName("should be equal to itself")
        void shouldBeEqualToItself() {
            // Assert
            assertEquals(baseAnnotation, baseAnnotation);
        }

        @Test
        @DisplayName("should be equal to an identical instance")
        void shouldBeEqualToIdenticalInstance() {
            // Arrange
            XYDrawableAnnotation identicalAnnotation = new XYDrawableAnnotation(10.0, 20.0, 100.0, 200.0, baseDrawable);

            // Assert
            assertEquals(baseAnnotation, identicalAnnotation);
        }

        @Test
        @DisplayName("should not be equal to null")
        void shouldNotBeEqualToNull() {
            // Assert
            assertNotEquals(null, baseAnnotation);
        }

        @Test
        @DisplayName("should not be equal to an object of a different type")
        void shouldNotBeEqualToDifferentType() {
            // Assert
            assertNotEquals(baseAnnotation, new Object());
        }

        @Test
        @DisplayName("should not be equal when x coordinate differs")
        void shouldNotBeEqualWhenXDiffers() {
            // Arrange
            XYDrawableAnnotation changedAnnotation = new XYDrawableAnnotation(11.0, 20.0, 100.0, 200.0, baseDrawable);

            // Assert
            assertNotEquals(baseAnnotation, changedAnnotation);
        }

        @Test
        @DisplayName("should not be equal when y coordinate differs")
        void shouldNotBeEqualWhenYDiffers() {
            // Arrange
            XYDrawableAnnotation changedAnnotation = new XYDrawableAnnotation(10.0, 22.0, 100.0, 200.0, baseDrawable);

            // Assert
            assertNotEquals(baseAnnotation, changedAnnotation);
        }

        @Test
        @DisplayName("should not be equal when display width differs")
        void shouldNotBeEqualWhenWidthDiffers() {
            // Arrange
            XYDrawableAnnotation changedAnnotation = new XYDrawableAnnotation(10.0, 20.0, 101.0, 200.0, baseDrawable);

            // Assert
            assertNotEquals(baseAnnotation, changedAnnotation);
        }

        @Test
        @DisplayName("should not be equal when display height differs")
        void shouldNotBeEqualWhenHeightDiffers() {
            // Arrange
            XYDrawableAnnotation changedAnnotation = new XYDrawableAnnotation(10.0, 20.0, 100.0, 202.0, baseDrawable);

            // Assert
            assertNotEquals(baseAnnotation, changedAnnotation);
        }

        @Test
        @DisplayName("should not be equal when draw scale factor differs")
        void shouldNotBeEqualWhenScaleFactorDiffers() {
            // Arrange: baseAnnotation has a default scale factor of 1.0
            XYDrawableAnnotation changedAnnotation = new XYDrawableAnnotation(10.0, 20.0, 100.0, 200.0, 2.0, baseDrawable);

            // Assert
            assertNotEquals(baseAnnotation, changedAnnotation);
        }

        @Test
        @DisplayName("should not be equal when drawable differs")
        void shouldNotBeEqualWhenDrawableDiffers() {
            // Arrange
            Drawable differentDrawable = new TestDrawable("drawable-2");
            XYDrawableAnnotation changedAnnotation = new XYDrawableAnnotation(10.0, 20.0, 100.0, 200.0, differentDrawable);

            // Assert
            assertNotEquals(baseAnnotation, changedAnnotation);
        }
    }
}
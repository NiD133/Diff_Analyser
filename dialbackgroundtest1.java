package org.jfree.chart.plot.dial;

import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for the {@link DialBackground} class, focusing on its behavior and contracts.
 */
@DisplayName("DialBackground")
class DialBackgroundTest {

    @Nested
    @DisplayName("equals() contract")
    class EqualsContract {

        private DialBackground background1;
        private DialBackground background2;

        @BeforeEach
        void setUp() {
            background1 = new DialBackground();
            background2 = new DialBackground();
        }

        @Test
        @DisplayName("should be true for two default instances")
        void testEqualsForDefaultInstances() {
            assertEquals(background1, background2);
        }

        @Test
        @DisplayName("should correctly compare the 'paint' property")
        void testEqualsWithPaint() {
            // Arrange
            Paint paint = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.YELLOW);
            
            // Act: Change paint on one instance
            background1.setPaint(paint);
            
            // Assert: Instances should no longer be equal
            assertNotEquals(background1, background2);

            // Act: Change paint on the second instance to match
            background2.setPaint(paint);

            // Assert: Instances should be equal again
            assertEquals(background1, background2);
        }

        @Test
        @DisplayName("should correctly compare the 'gradientPaintTransformer' property")
        void testEqualsWithGradientPaintTransformer() {
            // Arrange
            StandardGradientPaintTransformer transformer = new StandardGradientPaintTransformer(
                    GradientPaintTransformType.CENTER_VERTICAL);

            // Act: Change transformer on one instance
            background1.setGradientPaintTransformer(transformer);

            // Assert: Instances should no longer be equal
            assertNotEquals(background1, background2);

            // Act: Change transformer on the second instance to match
            background2.setGradientPaintTransformer(transformer);

            // Assert: Instances should be equal again
            assertEquals(background1, background2);
        }

        @Test
        @DisplayName("should correctly compare the inherited 'visible' property")
        void testEqualsWithVisibility() {
            // Act: Change visibility on one instance
            background1.setVisible(false);

            // Assert: Instances should no longer be equal
            assertNotEquals(background1, background2);

            // Act: Change visibility on the second instance to match
            background2.setVisible(false);

            // Assert: Instances should be equal again
            assertEquals(background1, background2);
        }
    }
}
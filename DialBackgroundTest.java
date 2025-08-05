/* ======================================================
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 *
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 *
 * Project Info:  https://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * -----------------------
 * DialBackgroundTest.java
 * -----------------------
 * (C) Copyright 2006-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.plot.dial;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.GradientPaint;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DialBackground} class.
 */
@DisplayName("DialBackground")
class DialBackgroundTest {

    private DialBackground b1;
    private DialBackground b2;

    @BeforeEach
    void setUp() {
        b1 = new DialBackground();
        b2 = new DialBackground();
    }

    @Nested
    @DisplayName("equals() and hashCode() contract")
    class EqualsAndHashCodeContract {

        @Test
        @DisplayName("should be equal for two default instances")
        void twoDefaultInstancesShouldBeEqual() {
            // Assert
            assertEquals(b1, b2);
            assertEquals(b1.hashCode(), b2.hashCode());
        }

        @Test
        @DisplayName("should be equal for two instances with the same properties")
        void instancesWithSamePropertiesShouldBeEqual() {
            // Arrange
            b1.setPaint(new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.YELLOW));
            b1.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));
            b1.setVisible(false);

            b2.setPaint(new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.YELLOW));
            b2.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));
            b2.setVisible(false);

            // Assert
            assertEquals(b1, b2);
            assertEquals(b1.hashCode(), b2.hashCode());
        }

        @Test
        @DisplayName("should not be equal if paint is different")
        void shouldNotBeEqualWhenPaintIsDifferent() {
            // Arrange
            b1.setPaint(new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.YELLOW));
            // b2 has default paint

            // Assert
            assertNotEquals(b1, b2);
        }

        @Test
        @DisplayName("should not be equal if gradient paint transformer is different")
        void shouldNotBeEqualWhenGradientPaintTransformerIsDifferent() {
            // Arrange
            b1.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));
            // b2 has default transformer

            // Assert
            assertNotEquals(b1, b2);
        }

        @Test
        @DisplayName("should not be equal if visibility (inherited property) is different")
        void shouldNotBeEqualWhenVisibilityIsDifferent() {
            // Arrange
            b1.setVisible(false);
            // b2 has default visibility (true)

            // Assert
            assertNotEquals(b1, b2);
        }
    }

    @Nested
    @DisplayName("Cloning")
    class CloningTests {

        @Test
        @DisplayName("clone() of a default instance should produce an equal and independent object")
        void cloneOfDefaultInstanceShouldBeEqualAndIndependent() throws CloneNotSupportedException {
            // Act
            DialBackground clone = (DialBackground) b1.clone();

            // Assert
            assertNotSame(b1, clone);
            assertEquals(b1, clone);
        }

        @Test
        @DisplayName("clone() of a customized instance should produce an equal and independent object")
        void cloneOfCustomizedInstanceShouldBeEqualAndIndependent() throws CloneNotSupportedException {
            // Arrange
            b1.setPaint(new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.GREEN));
            b1.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));
            MyDialLayerChangeListener listener = new MyDialLayerChangeListener();
            b1.addChangeListener(listener);

            // Act
            DialBackground clone = (DialBackground) b1.clone();

            // Assert
            assertNotSame(b1, clone, "Clone should be a different object instance.");
            assertEquals(b1, clone, "Clone should be equal to the original.");

            // Verify that the clone is independent by checking its listener list
            assertTrue(b1.hasListener(listener), "Original should still have the listener.");
            assertFalse(clone.hasListener(listener), "Clone should not have the listener.");
        }
    }

    @Nested
    @DisplayName("Serialization")
    class SerializationTests {

        @Test
        @DisplayName("a default instance should be serializable")
        void serializationOfDefaultInstance() {
            // Act
            DialBackground deserialized = TestUtils.serialised(b1);

            // Assert
            assertEquals(b1, deserialized);
        }

        @Test
        @DisplayName("a customized instance should be serializable")
        void serializationOfCustomizedInstance() {
            // Arrange
            b1.setPaint(new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.GREEN));
            b1.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));

            // Act
            DialBackground deserialized = TestUtils.serialised(b1);

            // Assert
            assertEquals(b1, deserialized);
        }
    }
}
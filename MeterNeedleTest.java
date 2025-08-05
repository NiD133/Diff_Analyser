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
 * --------------------
 * MeterNeedleTest.java
 * --------------------
 * (C) Copyright 2005-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.plot.compass;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Stroke;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for the {@link MeterNeedle} class.
 */
public class MeterNeedleTest {

    /**
     * A collection of tests for the equals() and hashCode() contract.
     */
    @Nested
    @DisplayName("The equals() and hashCode() contract")
    class EqualsAndHashCode {

        private MeterNeedle needle1;
        private MeterNeedle needle2;

        @BeforeEach
        void setUp() {
            // Use a concrete implementation to test the abstract class's logic
            this.needle1 = new LineNeedle();
            this.needle2 = new LineNeedle();
        }

        @Test
        @DisplayName("should hold for two default instances")
        void twoDefaultInstancesShouldBeEqual() {
            assertEquals(needle1, needle2, "Two default LineNeedle instances should be equal.");
            assertEquals(needle1.hashCode(), needle2.hashCode(), "Hash codes should be equal for equal objects.");
        }

        @Test
        @DisplayName("should be false when comparing to a different object type")
        void equalsShouldReturnFalseForDifferentType() {
            assertNotEquals("A string", needle1, "Equals should return false for different types.");
        }

        @Test
        @DisplayName("should differentiate based on fillPaint")
        void equalsShouldDifferentiateByFillPaint() {
            // Arrange
            needle1.setFillPaint(new GradientPaint(1f, 2f, Color.RED, 3f, 4f, Color.BLUE));

            // Act & Assert
            assertNotEquals(needle1, needle2);
            assertNotEquals(needle1.hashCode(), needle2.hashCode());

            // Arrange
            needle2.setFillPaint(new GradientPaint(1f, 2f, Color.RED, 3f, 4f, Color.BLUE));

            // Act & Assert
            assertEquals(needle1, needle2);
            assertEquals(needle1.hashCode(), needle2.hashCode());
        }

        @Test
        @DisplayName("should differentiate based on outlinePaint")
        void equalsShouldDifferentiateByOutlinePaint() {
            needle1.setOutlinePaint(new GradientPaint(5f, 6f, Color.YELLOW, 7f, 8f, Color.GREEN));
            assertNotEquals(needle1, needle2);
            assertNotEquals(needle1.hashCode(), needle2.hashCode());

            needle2.setOutlinePaint(new GradientPaint(5f, 6f, Color.YELLOW, 7f, 8f, Color.GREEN));
            assertEquals(needle1, needle2);
            assertEquals(needle1.hashCode(), needle2.hashCode());
        }

        @Test
        @DisplayName("should differentiate based on highlightPaint")
        void equalsShouldDifferentiateByHighlightPaint() {
            needle1.setHighlightPaint(new GradientPaint(9f, 0f, Color.ORANGE, 1f, 2f, Color.CYAN));
            assertNotEquals(needle1, needle2);
            assertNotEquals(needle1.hashCode(), needle2.hashCode());

            needle2.setHighlightPaint(new GradientPaint(9f, 0f, Color.ORANGE, 1f, 2f, Color.CYAN));
            assertEquals(needle1, needle2);
            assertEquals(needle1.hashCode(), needle2.hashCode());
        }

        @Test
        @DisplayName("should differentiate based on outlineStroke")
        void equalsShouldDifferentiateByOutlineStroke() {
            Stroke stroke = new BasicStroke(1.23f);
            needle1.setOutlineStroke(stroke);
            assertNotEquals(needle1, needle2);
            assertNotEquals(needle1.hashCode(), needle2.hashCode());

            needle2.setOutlineStroke(stroke);
            assertEquals(needle1, needle2);
            assertEquals(needle1.hashCode(), needle2.hashCode());
        }

        @Test
        @DisplayName("should differentiate based on rotateX")
        void equalsShouldDifferentiateByRotateX() {
            needle1.setRotateX(1.23);
            assertNotEquals(needle1, needle2);
            assertNotEquals(needle1.hashCode(), needle2.hashCode());

            needle2.setRotateX(1.23);
            assertEquals(needle1, needle2);
            assertEquals(needle1.hashCode(), needle2.hashCode());
        }

        @Test
        @DisplayName("should differentiate based on rotateY")
        void equalsShouldDifferentiateByRotateY() {
            needle1.setRotateY(4.56);
            assertNotEquals(needle1, needle2);
            assertNotEquals(needle1.hashCode(), needle2.hashCode());

            needle2.setRotateY(4.56);
            assertEquals(needle1, needle2);
            assertEquals(needle1.hashCode(), needle2.hashCode());
        }

        @Test
        @DisplayName("should differentiate based on size")
        void equalsShouldDifferentiateBySize() {
            needle1.setSize(11);
            assertNotEquals(needle1, needle2);
            assertNotEquals(needle1.hashCode(), needle2.hashCode());

            needle2.setSize(11);
            assertEquals(needle1, needle2);
            assertEquals(needle1.hashCode(), needle2.hashCode());
        }
    }
}
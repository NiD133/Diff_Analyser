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
 * ------------------------------
 * CategoryLabelPositionTest.java
 * ------------------------------
 * (C) Copyright 2004-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.axis;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.text.TextAnchor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for the {@link CategoryLabelPosition} class.
 */
public class CategoryLabelPositionTest {

    /**
     * A non-default instance used as a base for creating variations in tests.
     */
    private static final CategoryLabelPosition BASE_POSITION = new CategoryLabelPosition(
            RectangleAnchor.BOTTOM_LEFT, TextBlockAnchor.CENTER_RIGHT,
            TextAnchor.BASELINE_LEFT, Math.PI / 4.0,
            CategoryLabelWidthType.RANGE, 0.44f);

    @Test
    @DisplayName("Serialization should restore an object to a state that is equal to the original")
    void testSerialization() {
        // Arrange
        var originalPosition = new CategoryLabelPosition(
                RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.CENTER,
                Math.PI / 6.0, CategoryLabelWidthType.CATEGORY, 0.55f);

        // Act
        var deserializedPosition = TestUtils.serialised(originalPosition);

        // Assert
        assertEquals(originalPosition, deserializedPosition);
    }

    @Nested
    @DisplayName("equals() and hashCode() contract")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("An object should be equal to itself")
        void isEqualToItself() {
            assertEquals(BASE_POSITION, BASE_POSITION);
        }

        @Test
        @DisplayName("An object should not be equal to null")
        void isNotEqualToNull() {
            assertNotEquals(null, BASE_POSITION);
        }

        @Test
        @DisplayName("An object should not be equal to an object of a different type")
        void isNotEqualToDifferentType() {
            assertNotEquals(BASE_POSITION, "A String");
        }

        @Test
        @DisplayName("Two instances with identical properties should be equal")
        void isEqualToIdenticalInstance() {
            var identicalPosition = new CategoryLabelPosition(
                    RectangleAnchor.BOTTOM_LEFT, TextBlockAnchor.CENTER_RIGHT,
                    TextAnchor.BASELINE_LEFT, Math.PI / 4.0,
                    CategoryLabelWidthType.RANGE, 0.44f);
            assertEquals(BASE_POSITION, identicalPosition);
        }

        @Test
        @DisplayName("Instances with different category anchors should not be equal")
        void isNotEqualWhenCategoryAnchorDiffers() {
            var differentPosition = new CategoryLabelPosition(
                    RectangleAnchor.TOP, BASE_POSITION.getLabelAnchor(),
                    BASE_POSITION.getRotationAnchor(), BASE_POSITION.getAngle(),
                    BASE_POSITION.getWidthType(), BASE_POSITION.getWidthRatio());
            assertNotEquals(BASE_POSITION, differentPosition);
        }

        @Test
        @DisplayName("Instances with different label anchors should not be equal")
        void isNotEqualWhenLabelAnchorDiffers() {
            var differentPosition = new CategoryLabelPosition(
                    BASE_POSITION.getCategoryAnchor(), TextBlockAnchor.CENTER,
                    BASE_POSITION.getRotationAnchor(), BASE_POSITION.getAngle(),
                    BASE_POSITION.getWidthType(), BASE_POSITION.getWidthRatio());
            assertNotEquals(BASE_POSITION, differentPosition);
        }

        @Test
        @DisplayName("Instances with different rotation anchors should not be equal")
        void isNotEqualWhenRotationAnchorDiffers() {
            var differentPosition = new CategoryLabelPosition(
                    BASE_POSITION.getCategoryAnchor(), BASE_POSITION.getLabelAnchor(),
                    TextAnchor.CENTER, BASE_POSITION.getAngle(),
                    BASE_POSITION.getWidthType(), BASE_POSITION.getWidthRatio());
            assertNotEquals(BASE_POSITION, differentPosition);
        }

        @Test
        @DisplayName("Instances with different angles should not be equal")
        void isNotEqualWhenAngleDiffers() {
            var differentPosition = new CategoryLabelPosition(
                    BASE_POSITION.getCategoryAnchor(), BASE_POSITION.getLabelAnchor(),
                    BASE_POSITION.getRotationAnchor(), Math.PI / 6.0,
                    BASE_POSITION.getWidthType(), BASE_POSITION.getWidthRatio());
            assertNotEquals(BASE_POSITION, differentPosition);
        }

        @Test
        @DisplayName("Instances with different width types should not be equal")
        void isNotEqualWhenWidthTypeDiffers() {
            var differentPosition = new CategoryLabelPosition(
                    BASE_POSITION.getCategoryAnchor(), BASE_POSITION.getLabelAnchor(),
                    BASE_POSITION.getRotationAnchor(), BASE_POSITION.getAngle(),
                    CategoryLabelWidthType.CATEGORY, BASE_POSITION.getWidthRatio());
            assertNotEquals(BASE_POSITION, differentPosition);
        }

        @Test
        @DisplayName("Instances with different width ratios should not be equal")
        void isNotEqualWhenWidthRatioDiffers() {
            var differentPosition = new CategoryLabelPosition(
                    BASE_POSITION.getCategoryAnchor(), BASE_POSITION.getLabelAnchor(),
                    BASE_POSITION.getRotationAnchor(), BASE_POSITION.getAngle(),
                    BASE_POSITION.getWidthType(), 0.55f);
            assertNotEquals(BASE_POSITION, differentPosition);
        }

        @Test
        @DisplayName("Equal objects should have the same hash code")
        void hashCodeIsSameForEqualInstances() {
            // Test with default instances
            var position1 = new CategoryLabelPosition();
            var position2 = new CategoryLabelPosition();
            assertEquals(position1.hashCode(), position2.hashCode());

            // Test with non-default instances
            var position3 = new CategoryLabelPosition(
                    RectangleAnchor.BOTTOM_LEFT, TextBlockAnchor.CENTER_RIGHT,
                    TextAnchor.BASELINE_LEFT, Math.PI / 4.0,
                    CategoryLabelWidthType.RANGE, 0.44f);
            assertEquals(BASE_POSITION.hashCode(), position3.hashCode());
        }
    }
}
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
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link CategoryLabelPosition} class.
 */
public class CategoryLabelPositionTest {

    /**
     * Creates a default position instance for testing.
     * 
     * @return A default CategoryLabelPosition instance.
     */
    private static CategoryLabelPosition createDefaultPosition() {
        return new CategoryLabelPosition(
            RectangleAnchor.BOTTOM_LEFT,
            TextBlockAnchor.CENTER_RIGHT,
            TextAnchor.BASELINE_LEFT,
            Math.PI / 4.0,
            CategoryLabelWidthType.RANGE,
            0.44f
        );
    }

    // EQUALITY TESTS =========================================================

    @Test
    public void equalsShouldReturnTrueForSameInstance() {
        CategoryLabelPosition position = createDefaultPosition();
        assertEquals(position, position);
    }

    @Test
    public void equalsShouldReturnTrueForEqualObjects() {
        CategoryLabelPosition p1 = createDefaultPosition();
        CategoryLabelPosition p2 = createDefaultPosition();
        assertEquals(p1, p2);
        assertEquals(p2, p1); // Test symmetry
    }

    @Test
    public void equalsShouldReturnFalseForNull() {
        CategoryLabelPosition position = createDefaultPosition();
        assertNotEquals(null, position);
    }

    @Test
    public void equalsShouldReturnFalseForDifferentClass() {
        CategoryLabelPosition position = createDefaultPosition();
        assertNotEquals(position, new Object());
    }

    @Test
    public void equalsShouldReturnFalseWhenCategoryAnchorDiffers() {
        CategoryLabelPosition p1 = createDefaultPosition();
        CategoryLabelPosition p2 = new CategoryLabelPosition(
            RectangleAnchor.TOP, // Different anchor
            p1.getLabelAnchor(),
            p1.getRotationAnchor(),
            p1.getAngle(),
            p1.getWidthType(),
            p1.getWidthRatio()
        );
        assertNotEquals(p1, p2);
    }

    @Test
    public void equalsShouldReturnFalseWhenLabelAnchorDiffers() {
        CategoryLabelPosition p1 = createDefaultPosition();
        CategoryLabelPosition p2 = new CategoryLabelPosition(
            p1.getCategoryAnchor(),
            TextBlockAnchor.CENTER, // Different anchor
            p1.getRotationAnchor(),
            p1.getAngle(),
            p1.getWidthType(),
            p1.getWidthRatio()
        );
        assertNotEquals(p1, p2);
    }

    @Test
    public void equalsShouldReturnFalseWhenRotationAnchorDiffers() {
        CategoryLabelPosition p1 = createDefaultPosition();
        CategoryLabelPosition p2 = new CategoryLabelPosition(
            p1.getCategoryAnchor(),
            p1.getLabelAnchor(),
            TextAnchor.CENTER, // Different anchor
            p1.getAngle(),
            p1.getWidthType(),
            p1.getWidthRatio()
        );
        assertNotEquals(p1, p2);
    }

    @Test
    public void equalsShouldReturnFalseWhenAngleDiffers() {
        CategoryLabelPosition p1 = createDefaultPosition();
        CategoryLabelPosition p2 = new CategoryLabelPosition(
            p1.getCategoryAnchor(),
            p1.getLabelAnchor(),
            p1.getRotationAnchor(),
            Math.PI / 6.0, // Different angle
            p1.getWidthType(),
            p1.getWidthRatio()
        );
        assertNotEquals(p1, p2);
    }

    @Test
    public void equalsShouldReturnFalseWhenWidthTypeDiffers() {
        CategoryLabelPosition p1 = createDefaultPosition();
        CategoryLabelPosition p2 = new CategoryLabelPosition(
            p1.getCategoryAnchor(),
            p1.getLabelAnchor(),
            p1.getRotationAnchor(),
            p1.getAngle(),
            CategoryLabelWidthType.CATEGORY, // Different width type
            p1.getWidthRatio()
        );
        assertNotEquals(p1, p2);
    }

    @Test
    public void equalsShouldReturnFalseWhenWidthRatioDiffers() {
        CategoryLabelPosition p1 = createDefaultPosition();
        CategoryLabelPosition p2 = new CategoryLabelPosition(
            p1.getCategoryAnchor(),
            p1.getLabelAnchor(),
            p1.getRotationAnchor(),
            p1.getAngle(),
            p1.getWidthType(),
            0.55f // Different ratio
        );
        assertNotEquals(p1, p2);
    }

    // HASHCODE TESTS =========================================================

    @Test
    public void hashCodeShouldBeEqualForEqualObjects() {
        CategoryLabelPosition p1 = createDefaultPosition();
        CategoryLabelPosition p2 = createDefaultPosition();
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    // SERIALIZATION TESTS ====================================================

    @Test
    public void serializationShouldPreserveEquality() {
        CategoryLabelPosition p1 = new CategoryLabelPosition();
        CategoryLabelPosition p2 = TestUtils.serialised(p1);
        assertEquals(p1, p2);
    }
}
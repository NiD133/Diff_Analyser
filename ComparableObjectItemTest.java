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
 * -----------------------------
 * ComparableObjectItemTest.java
 * -----------------------------
 * (C) Copyright 2006-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTest {

    // Test data constants for better readability and consistency
    private static final Integer X_VALUE_1 = 1;
    private static final Integer X_VALUE_2 = 2;
    private static final Integer X_VALUE_3 = 3;
    private static final String Y_VALUE_STRING = "XYZ";

    @Test
    public void testConstructor_WithNullXValue_ThrowsIllegalArgumentException() {
        // Given: null x-value and valid y-value
        Comparable nullXValue = null;
        String validYValue = Y_VALUE_STRING;

        // When & Then: constructor should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            new ComparableObjectItem(nullXValue, validYValue);
        }, "Constructor should reject null x-value");
    }

    @Test
    public void testEquals_WithIdenticalItems_ReturnsTrue() {
        // Given: two items with identical x and y values
        ComparableObjectItem firstItem = new ComparableObjectItem(X_VALUE_1, Y_VALUE_STRING);
        ComparableObjectItem secondItem = new ComparableObjectItem(X_VALUE_1, Y_VALUE_STRING);

        // When & Then: items should be equal
        assertEquals(firstItem, secondItem, "Items with identical values should be equal");
    }

    @Test
    public void testEquals_WithDifferentXValues_ReturnsFalse() {
        // Given: two items with different x values but same y value
        ComparableObjectItem itemWithX1 = new ComparableObjectItem(X_VALUE_1, Y_VALUE_STRING);
        ComparableObjectItem itemWithX2 = new ComparableObjectItem(X_VALUE_2, Y_VALUE_STRING);

        // When & Then: items should not be equal
        assertNotEquals(itemWithX1, itemWithX2, "Items with different x-values should not be equal");
    }

    @Test
    public void testEquals_WithDifferentYValues_ReturnsFalse() {
        // Given: two items with same x value but different y values
        ComparableObjectItem itemWithStringY = new ComparableObjectItem(X_VALUE_2, Y_VALUE_STRING);
        ComparableObjectItem itemWithNullY = new ComparableObjectItem(X_VALUE_2, null);

        // When & Then: items should not be equal
        assertNotEquals(itemWithStringY, itemWithNullY, "Items with different y-values should not be equal");
    }

    @Test
    public void testEquals_WithBothNullYValues_ReturnsTrue() {
        // Given: two items with same x value and both having null y values
        ComparableObjectItem firstItemWithNullY = new ComparableObjectItem(X_VALUE_2, null);
        ComparableObjectItem secondItemWithNullY = new ComparableObjectItem(X_VALUE_2, null);

        // When & Then: items should be equal
        assertEquals(firstItemWithNullY, secondItemWithNullY, 
                    "Items with same x-value and both null y-values should be equal");
    }

    @Test
    public void testCloning_CreatesEqualButSeparateInstance() throws CloneNotSupportedException {
        // Given: an original item
        ComparableObjectItem originalItem = new ComparableObjectItem(X_VALUE_1, Y_VALUE_STRING);

        // When: cloning the item
        ComparableObjectItem clonedItem = CloneUtils.clone(originalItem);

        // Then: clone should be a separate but equal instance
        assertNotSame(originalItem, clonedItem, "Clone should be a different object instance");
        assertSame(originalItem.getClass(), clonedItem.getClass(), "Clone should have same class");
        assertEquals(originalItem, clonedItem, "Clone should be equal to original");
    }

    @Test
    public void testSerialization_PreservesEquality() {
        // Given: an original item
        ComparableObjectItem originalItem = new ComparableObjectItem(X_VALUE_1, Y_VALUE_STRING);

        // When: serializing and deserializing the item
        ComparableObjectItem deserializedItem = TestUtils.serialised(originalItem);

        // Then: deserialized item should equal the original
        assertEquals(originalItem, deserializedItem, 
                    "Deserialized item should be equal to original");
    }

    @Test
    public void testCompareTo_WithSmallerXValue_ReturnsNegative() {
        // Given: items where first has smaller x-value than second
        ComparableObjectItem smallerItem = new ComparableObjectItem(X_VALUE_1, Y_VALUE_STRING);
        ComparableObjectItem largerItem = new ComparableObjectItem(X_VALUE_2, Y_VALUE_STRING);

        // When & Then: smaller item should compare as less than larger item
        assertTrue(smallerItem.compareTo(largerItem) < 0, 
                  "Item with smaller x-value should compare as less than item with larger x-value");
    }

    @Test
    public void testCompareTo_WithLargerXValue_ReturnsPositive() {
        // Given: items where first has larger x-value than second
        ComparableObjectItem smallerItem = new ComparableObjectItem(X_VALUE_1, Y_VALUE_STRING);
        ComparableObjectItem largerItem = new ComparableObjectItem(X_VALUE_2, Y_VALUE_STRING);

        // When & Then: larger item should compare as greater than smaller item
        assertTrue(largerItem.compareTo(smallerItem) > 0, 
                  "Item with larger x-value should compare as greater than item with smaller x-value");
    }

    @Test
    public void testCompareTo_WithEqualXValues_ReturnsZero() {
        // Given: two items with identical x-values
        ComparableObjectItem firstItem = new ComparableObjectItem(X_VALUE_1, Y_VALUE_STRING);
        ComparableObjectItem secondItem = new ComparableObjectItem(X_VALUE_1, Y_VALUE_STRING);

        // When & Then: items should compare as equal
        assertEquals(0, firstItem.compareTo(secondItem), 
                    "Items with equal x-values should compare as equal (return 0)");
    }

    @Test
    public void testCompareTo_WithMultipleComparisons_MaintainsConsistentOrdering() {
        // Given: three items with different x-values in ascending order
        ComparableObjectItem item1 = new ComparableObjectItem(X_VALUE_1, Y_VALUE_STRING);
        ComparableObjectItem item2 = new ComparableObjectItem(X_VALUE_2, Y_VALUE_STRING);
        ComparableObjectItem item3 = new ComparableObjectItem(X_VALUE_3, Y_VALUE_STRING);

        // When & Then: verify consistent ordering relationships
        assertTrue(item2.compareTo(item1) > 0, "item2 should be greater than item1");
        assertTrue(item3.compareTo(item1) > 0, "item3 should be greater than item1");
        assertTrue(item3.compareTo(item2) > 0, "item3 should be greater than item2");
    }
}
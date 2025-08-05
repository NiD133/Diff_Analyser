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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link ComparableObjectItem} class.
 */
@DisplayName("ComparableObjectItem")
class ComparableObjectItemTest {

    /**
     * The constructor should reject null 'comparable' arguments, as they are not permitted.
     */
    @Test
    @DisplayName("Constructor should throw exception for a null 'comparable' argument")
    void constructor_shouldThrowException_forNullComparable() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ComparableObjectItem(null, "ObjectValue");
        }, "Constructor should not accept a null 'comparable' argument.");
    }

    /**
     * The equals() method should correctly handle comparisons based on all fields.
     */
    @Test
    @DisplayName("equals() should correctly compare items based on state")
    void equals_shouldCompareBasedOnState() {
        // Arrange: Create a base item for comparison
        ComparableObjectItem item1 = new ComparableObjectItem(1, "XYZ");

        // Act & Assert: Test for equality with an identical item
        ComparableObjectItem item2 = new ComparableObjectItem(1, "XYZ");
        assertEquals(item1, item2, "Items with the same state should be equal.");
        assertEquals(item1.hashCode(), item2.hashCode(), "Hash codes for equal objects must be equal.");

        // Act & Assert: Test for inequality with a different 'comparable'
        ComparableObjectItem itemWithDifferentComparable = new ComparableObjectItem(2, "XYZ");
        assertNotEquals(item1, itemWithDifferentComparable, "Items with different 'comparable' values should not be equal.");

        // Act & Assert: Test for inequality with a different 'object'
        ComparableObjectItem itemWithDifferentObject = new ComparableObjectItem(1, "ABC");
        assertNotEquals(item1, itemWithDifferentObject, "Items with different 'object' values should not be equal.");
    }

    /**
     * The equals() method must correctly handle null values for the 'object' field.
     */
    @Test
    @DisplayName("equals() should handle null 'object' values correctly")
    void equals_shouldHandleNullObjectValues() {
        // Arrange
        ComparableObjectItem itemWithNullObj1 = new ComparableObjectItem(1, null);
        ComparableObjectItem itemWithNullObj2 = new ComparableObjectItem(1, null);
        ComparableObjectItem itemWithNonNullObj = new ComparableObjectItem(1, "XYZ");

        // Act & Assert
        assertEquals(itemWithNullObj1, itemWithNullObj2, "Two items with null objects should be equal.");
        assertNotEquals(itemWithNonNullObj, itemWithNullObj1, "An item with a non-null object should not be equal to one with a null object.");
        assertNotEquals(itemWithNullObj1, itemWithNonNullObj, "An item with a null object should not be equal to one with a non-null object.");
    }

    /**
     * A clone should be a separate instance but equal in value to the original.
     */
    @Test
    @DisplayName("clone() should create an independent and equal copy")
    void clone_shouldCreateIndependentCopy() throws CloneNotSupportedException {
        // Arrange
        ComparableObjectItem original = new ComparableObjectItem(1, "XYZ");

        // Act
        ComparableObjectItem clone = (ComparableObjectItem) original.clone();

        // Assert
        assertNotSame(original, clone, "Clone should be a different instance from the original.");
        assertEquals(original, clone, "Clone should be equal in value to the original.");
    }

    /**
     * An object's state should be fully preserved after serialization and deserialization.
     */
    @Test
    @DisplayName("Serialization should preserve the object's state")
    void serialization_shouldPreserveObjectState() {
        // Arrange
        ComparableObjectItem original = new ComparableObjectItem(1, "XYZ");

        // Act
        ComparableObjectItem deserialized = TestUtils.serialised(original);

        // Assert
        assertEquals(original, deserialized, "Deserialized item should be equal to the original.");
    }

    /**
     * The compareTo() method should order items based only on their 'comparable' field.
     */
    @Test
    @DisplayName("compareTo() should order items based on the 'comparable' field")
    void compareTo_shouldOrderBasedOnComparable() {
        // Arrange: The object values ('Z', 'Y', 'X') are different to confirm they are ignored.
        ComparableObjectItem item1 = new ComparableObjectItem(1, "Z");
        ComparableObjectItem item2 = new ComparableObjectItem(2, "Y");
        ComparableObjectItem anotherItem1 = new ComparableObjectItem(1, "X");

        // Act & Assert
        assertTrue(item1.compareTo(item2) < 0, "item1 (1) should be less than item2 (2).");
        assertTrue(item2.compareTo(item1) > 0, "item2 (2) should be greater than item1 (1).");
        assertEquals(0, item1.compareTo(anotherItem1), "Items with the same 'comparable' value should be considered equal in comparison.");
    }
}
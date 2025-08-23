package org.jfree.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for the equals() method in the {@link ComparableObjectItem} class.
 */
@DisplayName("ComparableObjectItem.equals()")
class ComparableObjectItemTest {

    @Test
    @DisplayName("An item should be equal to itself")
    void testEquals_Reflexive() {
        // Arrange
        ComparableObjectItem item = new ComparableObjectItem(1, "A");

        // Act & Assert
        assertEquals(item, item, "An item must be equal to itself.");
    }

    @Test
    @DisplayName("Two items with the same state should be equal")
    void testEquals_Symmetric_SameState() {
        // Arrange
        ComparableObjectItem item1 = new ComparableObjectItem(1, "A");
        ComparableObjectItem item2 = new ComparableObjectItem(1, "A");

        // Act & Assert
        assertEquals(item1, item2, "Items with the same state should be equal.");
        assertEquals(item2, item1, "Equality should be symmetric.");
    }

    @Test
    @DisplayName("Two items with the same state but null objects should be equal")
    void testEquals_Symmetric_SameStateWithNullObject() {
        // Arrange
        ComparableObjectItem item1 = new ComparableObjectItem(1, null);
        ComparableObjectItem item2 = new ComparableObjectItem(1, null);

        // Act & Assert
        assertEquals(item1, item2, "Items with the same state and null objects should be equal.");
        assertEquals(item2, item1, "Equality should be symmetric for items with null objects.");
    }

    @Test
    @DisplayName("Items with different comparable values should not be equal")
    void testEquals_DifferentComparable() {
        // Arrange
        ComparableObjectItem item1 = new ComparableObjectItem(1, "A");
        ComparableObjectItem item2 = new ComparableObjectItem(2, "A");

        // Act & Assert
        assertNotEquals(item1, item2, "Items with different 'comparable' values should not be equal.");
    }

    @Test
    @DisplayName("Items with different object values should not be equal")
    void testEquals_DifferentObject() {
        // Arrange
        ComparableObjectItem item1 = new ComparableObjectItem(1, "A");
        ComparableObjectItem item2 = new ComparableObjectItem(1, "B");

        // Act & Assert
        assertNotEquals(item1, item2, "Items with different 'object' values should not be equal.");
    }

    @Test
    @DisplayName("An item with a null object should not equal an item with a non-null object")
    void testEquals_OneNullObject() {
        // Arrange
        ComparableObjectItem itemWithObject = new ComparableObjectItem(1, "A");
        ComparableObjectItem itemWithNull = new ComparableObjectItem(1, null);

        // Act & Assert
        assertNotEquals(itemWithObject, itemWithNull, "An item with an object should not equal one with a null object.");
        assertNotEquals(itemWithNull, itemWithObject, "Equality check should be symmetric.");
    }

    @Test
    @DisplayName("An item should not be equal to null")
    void testEquals_NullReference() {
        // Arrange
        ComparableObjectItem item = new ComparableObjectItem(1, "A");

        // Act & Assert
        assertNotEquals(null, item, "An item should not be equal to a null reference.");
    }

    @Test
    @DisplayName("An item should not be equal to an object of a different type")
    void testEquals_DifferentType() {
        // Arrange
        ComparableObjectItem item = new ComparableObjectItem(1, "A");
        String otherType = "Not a ComparableObjectItem";

        // Act & Assert
        assertNotEquals(item, otherType, "An item should not be equal to an object of a different type.");
    }
}
package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A suite of tests for the {@link ComparableObjectItem} class.
 * This class focuses on verifying the correctness of the constructor,
 * data accessors, and core methods like compareTo, equals, hashCode, and clone.
 */
public class ComparableObjectItemTest {

    // --- Constructor Tests ---

    @Test(expected = IllegalArgumentException.class)
    public void constructor_shouldThrowException_whenComparableIsNull() {
        // Act & Assert
        new ComparableObjectItem(null, "someObject");
    }

    // --- Getter and Setter Tests ---

    @Test
    public void getComparable_shouldReturnTheAssignedComparable() {
        // Arrange
        String comparable = "TestKey";
        ComparableObjectItem item = new ComparableObjectItem(comparable, "TestValue");

        // Act
        Comparable<?> result = item.getComparable();

        // Assert
        assertEquals(comparable, result);
    }

    @Test
    public void getObject_shouldReturnTheAssignedObject() {
        // Arrange
        String value = "TestValue";
        ComparableObjectItem item = new ComparableObjectItem("TestKey", value);

        // Act
        Object result = item.getObject();

        // Assert
        assertEquals(value, result);
    }
    
    @Test
    public void getObject_shouldReturnNull_whenObjectIsNull() {
        // Arrange
        ComparableObjectItem item = new ComparableObjectItem("TestKey", null);

        // Act
        Object result = item.getObject();

        // Assert
        assertNull(result);
    }

    @Test
    public void setObject_shouldUpdateTheObjectValue() {
        // Arrange
        ComparableObjectItem item = new ComparableObjectItem("TestKey", "InitialValue");
        String newValue = "UpdatedValue";

        // Act
        item.setObject(newValue);

        // Assert
        assertEquals(newValue, item.getObject());
    }

    // --- compareTo() Tests ---

    @Test
    public void compareTo_shouldReturnZero_whenComparableValuesAreEqual() {
        // Arrange
        ComparableObjectItem item1 = new ComparableObjectItem(10, "A");
        ComparableObjectItem item2 = new ComparableObjectItem(10, "B");

        // Act
        int result = item1.compareTo(item2);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void compareTo_shouldReturnNegative_whenThisItemIsSmaller() {
        // Arrange
        ComparableObjectItem item1 = new ComparableObjectItem(10, "A");
        ComparableObjectItem item2 = new ComparableObjectItem(20, "B");

        // Act
        int result = item1.compareTo(item2);

        // Assert
        assertTrue("Result should be negative", result < 0);
    }

    @Test
    public void compareTo_shouldReturnPositive_whenThisItemIsGreater() {
        // Arrange
        ComparableObjectItem item1 = new ComparableObjectItem(20, "A");
        ComparableObjectItem item2 = new ComparableObjectItem(10, "B");

        // Act
        int result = item1.compareTo(item2);

        // Assert
        assertTrue("Result should be positive", result > 0);
    }
    
    @Test(expected = ClassCastException.class)
    public void compareTo_shouldThrowClassCastException_whenComparablesAreIncompatible() {
        // Arrange
        // Create items with incompatible Comparable types (Integer vs. String).
        ComparableObjectItem itemWithInteger = new ComparableObjectItem(1, "A");
        ComparableObjectItem itemWithString = new ComparableObjectItem("B", "B");

        // Act
        // This will internally call Integer.compareTo(String), causing a ClassCastException.
        itemWithInteger.compareTo(itemWithString);
    }

    // --- equals() Tests ---

    @Test
    public void equals_shouldReturnTrue_forSameInstance() {
        // Arrange
        ComparableObjectItem item = new ComparableObjectItem("A", 1);

        // Act & Assert
        assertTrue(item.equals(item));
    }

    @Test
    public void equals_shouldReturnTrue_whenItemsAreLogicallyEqual() {
        // Arrange
        ComparableObjectItem item1 = new ComparableObjectItem("A", 1);
        ComparableObjectItem item2 = new ComparableObjectItem("A", 1);

        // Act & Assert
        assertTrue(item1.equals(item2));
    }
    
    @Test
    public void equals_shouldReturnTrue_whenComparableIsEqualAndObjectsAreBothNull() {
        // Arrange
        ComparableObjectItem item1 = new ComparableObjectItem("A", null);
        ComparableObjectItem item2 = new ComparableObjectItem("A", null);

        // Act & Assert
        assertTrue(item1.equals(item2));
    }

    @Test
    public void equals_shouldReturnFalse_whenComparedWithNull() {
        // Arrange
        ComparableObjectItem item = new ComparableObjectItem("A", 1);

        // Act & Assert
        assertFalse(item.equals(null));
    }

    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentType() {
        // Arrange
        ComparableObjectItem item = new ComparableObjectItem("A", 1);
        String otherObject = "Not a ComparableObjectItem";

        // Act & Assert
        assertFalse(item.equals(otherObject));
    }

    @Test
    public void equals_shouldReturnFalse_whenComparablesAreDifferent() {
        // Arrange
        ComparableObjectItem item1 = new ComparableObjectItem("A", 1);
        ComparableObjectItem item2 = new ComparableObjectItem("B", 1);

        // Act & Assert
        assertFalse(item1.equals(item2));
    }

    @Test
    public void equals_shouldReturnFalse_whenObjectsAreDifferent() {
        // Arrange
        ComparableObjectItem item1 = new ComparableObjectItem("A", 1);
        ComparableObjectItem item2 = new ComparableObjectItem("A", 2);

        // Act & Assert
        assertFalse(item1.equals(item2));
    }
    
    @Test
    public void equals_shouldReturnFalse_whenOneObjectIsNull() {
        // Arrange
        ComparableObjectItem item1 = new ComparableObjectItem("A", 1);
        ComparableObjectItem item2 = new ComparableObjectItem("A", null);

        // Act & Assert
        assertFalse(item1.equals(item2));
        assertFalse(item2.equals(item1));
    }

    // --- hashCode() Tests ---

    @Test
    public void hashCode_shouldBeConsistentWithEquals() {
        // Arrange
        ComparableObjectItem item1 = new ComparableObjectItem("A", 1);
        ComparableObjectItem item2 = new ComparableObjectItem("A", 1);

        // Assert
        assertTrue("Equal objects must have equal hash codes", item1.equals(item2));
        assertEquals("Equal objects must have equal hash codes", item1.hashCode(), item2.hashCode());
    }

    @Test
    public void hashCode_shouldBeDifferentForUnequalObjects() {
        // Arrange
        ComparableObjectItem item1 = new ComparableObjectItem("A", 1);
        ComparableObjectItem item2 = new ComparableObjectItem("B", 2);

        // Assert
        assertFalse("Objects should be unequal for this test", item1.equals(item2));
        assertNotEquals("Unequal objects should ideally have different hash codes", item1.hashCode(), item2.hashCode());
    }

    // --- clone() Test ---

    @Test
    public void clone_shouldReturnEqualButNotSameInstance() throws CloneNotSupportedException {
        // Arrange
        ComparableObjectItem original = new ComparableObjectItem("A", 123);

        // Act
        ComparableObjectItem cloned = (ComparableObjectItem) original.clone();

        // Assert
        assertNotSame("Cloned object should be a different instance", original, cloned);
        assertEquals("Cloned object should be equal to the original", original, cloned);
    }
}
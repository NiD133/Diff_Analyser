package org.jfree.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the compareTo() method in the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTest {

    /**
     * Verifies that compareTo() returns a positive integer when the first item's
     * comparable is greater than the second's.
     */
    @Test
    public void compareTo_whenThisItemIsGreater_shouldReturnPositiveValue() {
        // Arrange
        ComparableObjectItem greaterItem = new ComparableObjectItem(2, "Item A");
        ComparableObjectItem smallerItem = new ComparableObjectItem(1, "Item B");

        // Act
        int result = greaterItem.compareTo(smallerItem);

        // Assert
        assertTrue(result > 0, "Expected a positive value when comparing a greater item to a smaller one.");
    }

    /**
     * Verifies that compareTo() returns a negative integer when the first item's
     * comparable is less than the second's.
     */
    @Test
    public void compareTo_whenThisItemIsLesser_shouldReturnNegativeValue() {
        // Arrange
        ComparableObjectItem smallerItem = new ComparableObjectItem(1, "Item A");
        ComparableObjectItem greaterItem = new ComparableObjectItem(2, "Item B");

        // Act
        int result = smallerItem.compareTo(greaterItem);

        // Assert
        assertTrue(result < 0, "Expected a negative value when comparing a smaller item to a greater one.");
    }

    /**
     * Verifies that compareTo() returns zero when the items' comparables are equal,
     * regardless of their object values.
     */
    @Test
    public void compareTo_whenItemsHaveEqualComparables_shouldReturnZero() {
        // Arrange
        // The object values ("A" and "B") are different to ensure they are ignored during comparison.
        ComparableObjectItem item1 = new ComparableObjectItem(1, "A");
        ComparableObjectItem item2 = new ComparableObjectItem(1, "B");

        // Act
        int result = item1.compareTo(item2);

        // Assert
        assertEquals(0, result, "Expected zero for items with equal comparables.");
    }
}
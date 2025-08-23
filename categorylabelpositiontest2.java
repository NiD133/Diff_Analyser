package org.jfree.chart.axis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link CategoryLabelPosition} class.
 */
class CategoryLabelPositionTest {

    @Test
    @DisplayName("Two equal objects must have the same hash code")
    void hashCode_shouldBeSameForEqualObjects() {
        // Arrange: Create two identical objects using the default constructor.
        // According to the CategoryLabelPosition implementation, these should be equal.
        CategoryLabelPosition position1 = new CategoryLabelPosition();
        CategoryLabelPosition position2 = new CategoryLabelPosition();

        // Assert: First, verify the precondition that the two objects are equal.
        assertEquals(position1, position2, "Two default-constructed objects should be equal.");

        // Act & Assert: Verify that their hash codes are also equal, fulfilling the Java Object contract.
        assertEquals(position1.hashCode(), position2.hashCode(), "Equal objects must have the same hash code.");
    }
}
package org.jfree.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * Tests for the {@link ComparableObjectItem} class, focusing on its cloning behavior.
 */
@DisplayName("ComparableObjectItem")
class ComparableObjectItemTest {

    /**
     * Verifies that the clone() method creates a new instance that is
     * logically equal to the original but is not the same object reference.
     * This fulfills the general contract for Object.clone().
     */
    @Test
    @DisplayName("clone() should create a distinct but equal instance")
    void clone_shouldCreateDistinctButEqualInstance() throws CloneNotSupportedException {
        // Arrange
        ComparableObjectItem originalItem = new ComparableObjectItem(1, "XYZ");

        // Act
        ComparableObjectItem clonedItem = (ComparableObjectItem) originalItem.clone();

        // Assert
        assertAll("A cloned item should be a distinct but equal copy of the original",
            () -> assertNotSame(originalItem, clonedItem, "Clone should be a new object instance."),
            () -> assertEquals(originalItem, clonedItem, "Clone should be logically equal to the original.")
        );
    }
}
package org.jfree.data.general;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DefaultKeyedValueDataset} class, focusing on the cloning behavior.
 */
class DefaultKeyedValueDatasetTest {

    @Test
    @DisplayName("clone() should create a deep and independent copy")
    void cloneShouldCreateDeepAndIndependentCopy() throws CloneNotSupportedException {
        // Arrange: Create an original dataset instance.
        var originalDataset = new DefaultKeyedValueDataset("Test Key", 45.5);

        // Act: Clone the original dataset.
        var clonedDataset = (DefaultKeyedValueDataset) originalDataset.clone();

        // Assert: Verify the standard clone contract.
        // The clone should be a different object instance but logically equal to the original.
        assertAll("Standard clone contract",
            () -> assertNotSame(originalDataset, clonedDataset,
                    "Clone should be a new object instance."),
            () -> assertEquals(originalDataset, clonedDataset,
                    "Cloned dataset should be logically equal to the original.")
        );

        // Act & Assert: To confirm a deep copy, modify the original and check the clone.
        originalDataset.updateValue(99.9);

        assertNotEquals(originalDataset.getValue(), clonedDataset.getValue(),
                "Modifying the original dataset should not affect the clone.");
        assertEquals(45.5, clonedDataset.getValue(),
                "The clone's value should remain unchanged after the original is modified.");
    }
}
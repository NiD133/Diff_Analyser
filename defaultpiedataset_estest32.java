package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Provides tests for the {@code equals()} method in the {@link DefaultPieDataset} class.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that the equals() method returns false when a DefaultPieDataset
     * is compared with an object of an incompatible type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithIncompatibleType() {
        // Arrange: Create an empty pie dataset and an object of a different type.
        DefaultPieDataset<String> pieDataset = new DefaultPieDataset<>();
        Object incompatibleObject = "a simple string";

        // Act: Compare the dataset with the incompatible object.
        boolean result = pieDataset.equals(incompatibleObject);

        // Assert: The result should be false, as the types are different.
        assertFalse("A DefaultPieDataset should not be equal to a String.", result);
    }
}
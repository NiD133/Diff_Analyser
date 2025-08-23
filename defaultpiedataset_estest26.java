package org.jfree.data.general;

import org.junit.Test;

/**
 * Unit tests for the {@link DefaultPieDataset} class.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that attempting to get a value from an empty dataset
     * throws an IndexOutOfBoundsException.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getValue_onEmptyDataset_shouldThrowIndexOutOfBoundsException() {
        // Arrange: Create an empty dataset.
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        // Act: Attempt to retrieve a value at index 0, which is out of bounds.
        dataset.getValue(0);

        // Assert: The test passes if an IndexOutOfBoundsException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}
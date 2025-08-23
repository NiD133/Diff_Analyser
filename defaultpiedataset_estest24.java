package org.jfree.data.general;

import org.junit.Test;

/**
 * Unit tests for the {@link DefaultPieDataset} class, focusing on exception handling
 * for invalid arguments.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that the getKey() method throws an IndexOutOfBoundsException when
     * called with a negative index on an empty dataset.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getKey_withNegativeIndex_shouldThrowIndexOutOfBoundsException() {
        // Arrange: Create an empty dataset.
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        // Act & Assert: Attempt to retrieve a key using a negative index.
        // The @Test(expected=...) annotation asserts that an IndexOutOfBoundsException is thrown.
        dataset.getKey(-1);
    }
}
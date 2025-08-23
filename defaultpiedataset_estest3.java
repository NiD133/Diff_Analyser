package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A collection of well-structured and understandable tests for the {@link DefaultPieDataset} class.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that getKey(0) returns the key of the first item added to the dataset.
     */
    @Test
    public void getKey_returnsCorrectKey_forFirstItem() {
        // Arrange: Create a dataset and add a single key-value pair.
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        final String expectedKey = "Category A";
        final double value = 10.0;
        dataset.setValue(expectedKey, value);

        // Act: Retrieve the key at the first index (0).
        String actualKey = dataset.getKey(0);

        // Assert: The retrieved key should match the key that was added.
        assertEquals(expectedKey, actualKey);
    }
}
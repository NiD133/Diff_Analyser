package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link DefaultKeyedValueDataset} class.
 */
public class DefaultKeyedValueDatasetTest {

    /**
     * Verifies that getKey() returns null for a dataset that has just been
     * instantiated and is empty.
     */
    @Test
    public void getKey_whenDatasetIsEmpty_returnsNull() {
        // Arrange: Create a new dataset using the default constructor.
        // This results in an empty dataset with no key or value.
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();

        // Act: Retrieve the key from the empty dataset.
        Comparable<?> key = dataset.getKey();

        // Assert: The key should be null, as no data has been set.
        assertNull("Expected getKey() to return null for a new, empty dataset.", key);
    }
}
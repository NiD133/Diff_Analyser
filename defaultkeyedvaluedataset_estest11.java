package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the hashCode() method in the {@link DefaultKeyedValueDataset} class.
 */
public class DefaultKeyedValueDatasetHashCodeTest {

    /**
     * Verifies that the hashCode for a newly created, empty dataset is zero.
     * The hashCode is derived from its internal data, which is null upon default construction.
     */
    @Test
    public void hashCode_forEmptyDataset_shouldBeZero() {
        // Arrange: Create an empty dataset.
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();

        // Act: Calculate the hash code.
        int actualHashCode = dataset.hashCode();

        // Assert: The hash code for an empty dataset should be 0.
        assertEquals(0, actualHashCode);
    }
}
package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Objects;

/**
 * A test suite for the hashCode() method of the {@link DefaultKeyedValueDataset} class.
 */
public class DefaultKeyedValueDatasetTest {

    /**
     * Verifies that the hashCode method correctly computes a hash code when the
     * dataset's internal data is another DefaultKeyedValueDataset instance.
     *
     * This test ensures that nesting datasets does not cause exceptions and that
     * the hash code is calculated as expected based on the nested object.
     */
    @Test
    public void hashCode_whenDataIsAnotherDataset_isCalculatedCorrectly() {
        // Arrange: Create an inner dataset and an outer dataset that contains it.
        // The inner dataset is empty (its internal data is null).
        DefaultKeyedValueDataset innerDataset = new DefaultKeyedValueDataset();
        DefaultKeyedValueDataset outerDataset = new DefaultKeyedValueDataset(innerDataset);

        // Act: Calculate the hash code of the outer dataset.
        int actualHashCode = outerDataset.hashCode();

        // Assert: The hash code should be based on the contained innerDataset object.
        // The source class uses Objects.hash(this.data) for its implementation.
        int expectedHashCode = Objects.hash(innerDataset);
        assertEquals(expectedHashCode, actualHashCode);
    }
}
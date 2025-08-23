package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the equals() method in the {@link DefaultPieDataset} class.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that the equals() method returns true when comparing a
     * DefaultPieDataset to a different KeyedValues implementation
     * (in this case, DefaultKeyedValuesDataset) that holds the same data.
     */
    @Test
    public void equals_shouldReturnTrue_whenComparedToEquivalentKeyedValuesDataset() {
        // Arrange: Create a source dataset and populate it with a sample value.
        DefaultKeyedValuesDataset<String> sourceDataset = new DefaultKeyedValuesDataset<>();
        sourceDataset.setValue("Category A", 100.0);

        // Create a DefaultPieDataset using the first dataset as a source.
        DefaultPieDataset<String> pieDataset = new DefaultPieDataset<>(sourceDataset);

        // Act & Assert: The pieDataset should be considered equal to the source
        // dataset because they contain identical data. This confirms that equality
        // is based on data content, not the specific class type.
        // We use assertEquals for a more informative failure message.
        assertEquals(pieDataset, sourceDataset);
    }
}
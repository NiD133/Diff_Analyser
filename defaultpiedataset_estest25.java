package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for the {@link DefaultPieDataset} class.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that the item count of a newly instantiated DefaultPieDataset is zero.
     */
    @Test
    public void getItemCount_shouldReturnZero_whenDatasetIsNew() {
        // Arrange: Create a new, empty pie dataset.
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        // Act: Get the number of items in the dataset.
        int itemCount = dataset.getItemCount();

        // Assert: The item count should be 0, as nothing has been added.
        assertEquals(0, itemCount);
    }
}
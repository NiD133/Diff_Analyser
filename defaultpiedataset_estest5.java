package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the DefaultPieDataset class, focusing on the remove() method.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that the remove() method correctly deletes an existing item
     * from the dataset, resulting in a decrease in the item count.
     */
    @Test
    public void remove_whenKeyExists_shouldRemoveTheCorrespondingItem() {
        // Arrange: Create a dataset and add one item to it.
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        String key = "Firefox";
        dataset.setValue(key, 45.0);

        // Pre-condition check: Ensure the item was added successfully.
        assertEquals("Dataset should contain one item before removal.", 1, dataset.getItemCount());

        // Act: Remove the item using its key.
        dataset.remove(key);

        // Assert: The dataset should now be empty.
        assertEquals("Dataset should be empty after removing the only item.", 0, dataset.getItemCount());
    }
}
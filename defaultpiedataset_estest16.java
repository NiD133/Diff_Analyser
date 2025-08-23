package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link DefaultPieDataset} class, focusing on edge cases of the insertValue method.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that insertValue() throws an IndexOutOfBoundsException when trying to
     * move an existing key to a position that is out of bounds *after* the key has been
     * internally removed.
     */
    @Test
    public void insertValue_forExistingKeyAtInvalidPosition_throwsIndexOutOfBoundsException() {
        // Arrange: Create a dataset and add one entry, making its size 1.
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        final String KEY = "Category A";
        dataset.setValue(KEY, 100.0);
        assertEquals("Dataset should have one item after setup", 1, dataset.getItemCount());

        // Act & Assert:
        // Attempt to "move" the existing key to position 1. The implementation of
        // insertValue() first removes the existing entry for the key, which reduces
        // the dataset's size to 0. Then, it tries to add the new entry at the
        // requested position 1, which is now invalid for an empty dataset.
        try {
            dataset.insertValue(1, KEY, 200.0);
            fail("Expected an IndexOutOfBoundsException but none was thrown.");
        } catch (IndexOutOfBoundsException e) {
            // Verify the exception message to confirm it's the expected error.
            // The message "Index: 1, Size: 0" is characteristic of the underlying ArrayList.
            String message = e.getMessage();
            assertNotNull("Exception message should not be null", message);
            assertTrue("Exception message should contain 'Index: 1'", message.contains("Index: 1"));
            assertTrue("Exception message should contain 'Size: 0'", message.contains("Size: 0"));
        }
    }
}
package org.jfree.data.general;

import org.jfree.chart.api.SortOrder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link DefaultPieDataset} class, focusing on exception handling.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that calling sortByKeys() with a null SortOrder throws an IllegalArgumentException.
     * The method contract requires a non-null order argument.
     */
    @Test
    public void sortByKeys_withNullOrder_shouldThrowIllegalArgumentException() {
        // Arrange: Create an empty dataset. The content doesn't matter for this test.
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        // Act & Assert: Attempt to sort with a null order and verify the exception.
        try {
            dataset.sortByKeys(null);
            fail("Expected an IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message clearly states the cause of the error.
            assertEquals("Null 'order' argument.", e.getMessage());
        }
    }
}
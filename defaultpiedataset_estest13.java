package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link DefaultPieDataset} class.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that calling setValue() with a null key throws an
     * IllegalArgumentException, as null keys are not permitted.
     */
    @Test
    public void setValue_withNullKey_shouldThrowIllegalArgumentException() {
        // Arrange: Create an empty dataset
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        final Double value = 10.0;

        // Act & Assert: Attempt to set a value with a null key and expect an exception
        try {
            dataset.setValue(null, value);
            fail("Expected an IllegalArgumentException to be thrown for a null key, but it was not.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct
            assertEquals("Null 'key' argument.", e.getMessage());
        }
    }
}
package org.jfree.data.general;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link DefaultKeyedValueDataset} class.
 */
class DefaultKeyedValueDatasetTest {

    /**
     * Verifies that calling setValue() with a null key throws an
     * IllegalArgumentException, as this is a required argument.
     */
    @Test
    void setValue_withNullKey_shouldThrowIllegalArgumentException() {
        // Arrange: Create an empty dataset instance.
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        final Number anyValue = 100;
        final String expectedErrorMessage = "Null 'key' argument.";

        // Act & Assert: Call the method with a null key and verify the exception.
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> dataset.setValue(null, anyValue)
        );

        // Verify that the exception message is correct.
        assertEquals(expectedErrorMessage, thrown.getMessage());
    }
}
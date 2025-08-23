package org.jfree.data.general;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link DefaultKeyedValueDataset} class.
 */
public class DefaultKeyedValueDatasetTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException
     * when the provided key is null.
     */
    @Test
    public void constructorShouldThrowExceptionForNullKey() {
        // Use assertThrows to clearly state that an exception is expected.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            // Attempt to create a dataset with a null key.
            new DefaultKeyedValueDataset(null, 100.0);
        });

        // Verify that the exception message is correct, ensuring the right validation failed.
        assertEquals("Null 'key' argument.", exception.getMessage());
    }
}
package org.jfree.data.general;

import org.jfree.data.KeyedValue;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link DefaultKeyedValueDataset} class.
 */
public class DefaultKeyedValueDatasetTest {

    /**
     * Verifies that calling updateValue() on a dataset that was initialized
     * with a null key throws an IllegalArgumentException.
     */
    @Test
    public void updateValue_whenKeyIsNull_throwsIllegalArgumentException() {
        // Arrange: Create a dataset whose underlying KeyedValue has a null key.
        // This is achieved by passing an empty dataset to the constructor,
        // as an empty dataset's getKey() method returns null.
        KeyedValue sourceDataWithNullKey = new DefaultKeyedValueDataset();
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(sourceDataWithNullKey);
        Number newValue = 100;

        // Act & Assert: Expect an IllegalArgumentException when updating the value.
        // The internal implementation of updateValue retrieves the existing key
        // to set the new value, and a null key is not permitted.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            dataset.updateValue(newValue);
        });

        // Verify that the exception message is as expected.
        assertEquals("Null 'key' argument.", exception.getMessage());
    }
}
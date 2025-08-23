package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link DefaultKeyedValueDataset} class.
 */
public class DefaultKeyedValueDatasetTest {

    /**
     * Verifies that getValue() returns the same value object that was provided
     * to the constructor.
     */
    @Test
    public void getValue_whenDatasetIsConstructedWithValue_returnsTheSameValue() {
        // Arrange: Define a simple key and a numeric value.
        Comparable<String> key = "Sample Key";
        Number expectedValue = 123.45;

        // Act: Create the dataset using the constructor that accepts a key and a value.
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(key, expectedValue);
        Number actualValue = dataset.getValue();

        // Assert: The retrieved value should be the exact same instance as the one provided.
        assertSame("The value returned by getValue() should be the same object " +
                   "provided at construction.", expectedValue, actualValue);
    }
}
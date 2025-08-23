package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A test suite for the {@link DefaultKeyedValueDataset} class.
 */
public class DefaultKeyedValueDatasetTest {

    /**
     * Verifies that a value provided to the constructor can be correctly retrieved
     * using the getValue() method.
     */
    @Test
    public void getValue_shouldReturnTheValueProvidedInTheConstructor() {
        // Arrange: Define a key and a value for the dataset.
        Comparable<String> key = "Sample Key";
        Number expectedValue = -93;

        // Act: Create a new dataset instance with the specified key and value.
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(key, expectedValue);
        Number actualValue = dataset.getValue();

        // Assert: The value retrieved from the dataset should match the initial value.
        assertEquals(expectedValue, actualValue);
    }
}
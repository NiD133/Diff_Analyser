package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link DefaultKeyedValueDataset} class.
 */
public class DefaultKeyedValueDatasetTest {

    /**
     * Verifies that the getValue() method returns the value provided in the constructor.
     */
    @Test
    public void getValue_shouldReturnValueFromConstructor() {
        // Arrange: Create a dataset with a well-defined key and value.
        Comparable<String> key = "Population";
        Number expectedValue = 1500;
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(key, expectedValue);

        // Act: Retrieve the value from the dataset.
        Number actualValue = dataset.getValue();

        // Assert: The retrieved value should be the same as the one used for construction.
        assertEquals(expectedValue, actualValue);
    }
}
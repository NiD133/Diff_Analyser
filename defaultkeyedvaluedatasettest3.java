package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigInteger;

/**
 * This test suite focuses on verifying the behavior of the {@link DefaultKeyedValueDataset} class,
 * specifically its data retrieval capabilities.
 */
public class DefaultKeyedValueDataset_ESTestTest3 {

    /**
     * Tests that the getValue method correctly returns the value provided in the constructor.
     * This ensures that the dataset accurately stores and retrieves the initial key-value pair.
     */
    @Test
    public void getValue_whenInitializedWithValue_returnsSameValue() {
        // Arrange: Define a key and a value for the dataset.
        // A simple String is used for the key and a BigInteger for the value
        // to ensure the dataset correctly handles different Number types.
        Comparable<String> key = "Sample Key";
        Number expectedValue = BigInteger.valueOf(100L);

        // Act: Create a new DefaultKeyedValueDataset with the specified key and value,
        // and then retrieve the value.
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(key, expectedValue);
        Number actualValue = dataset.getValue();

        // Assert: Verify that the retrieved value is the exact same instance
        // as the one passed to the constructor.
        assertSame("The value retrieved from the dataset should be the same instance as the initial value.",
                expectedValue, actualValue);
    }
}
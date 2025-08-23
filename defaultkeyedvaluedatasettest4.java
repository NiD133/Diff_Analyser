package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigInteger;

/**
 * A test suite for the {@link DefaultKeyedValueDataset} class, focusing on
 * its data handling capabilities.
 */
public class DefaultKeyedValueDatasetTest {

    /**
     * Verifies that the getValue() method correctly returns the value
     * that was provided to the constructor.
     */
    @Test
    public void testGetValueReturnsConstructedValue() {
        // Arrange: Define a key and a value for the dataset.
        // The original test used a complex BigInteger to ensure different Number types are handled.
        // We retain BigInteger but instantiate it in a more readable way.
        // The value 65443 corresponds to the byte array {0, 0, -93} used in the original test.
        Comparable<String> key = "Test Key";
        Number expectedValue = new BigInteger("65443");
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(key, expectedValue);

        // Act: Retrieve the value from the dataset.
        Number actualValue = dataset.getValue();

        // Assert: The retrieved value must be identical to the one set in the constructor.
        assertEquals(expectedValue, actualValue);
    }
}
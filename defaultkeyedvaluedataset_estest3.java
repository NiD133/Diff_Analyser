package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.math.BigInteger;

/**
 * Tests for the {@link DefaultKeyedValueDataset} class, focusing on value retrieval.
 */
public class DefaultKeyedValueDatasetTest {

    /**
     * Verifies that getValue() returns the same Number instance that was
     * provided in the constructor.
     */
    @Test
    public void getValue_whenConstructedWithKeyAndValue_returnsSameValueInstance() {
        // Arrange: Define a simple key and a specific value to initialize the dataset.
        // Using simple, clear values makes the test's intent obvious.
        Comparable<String> key = "Test Key";
        Number expectedValue = new BigInteger("123456789");
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(key, expectedValue);

        // Act: Retrieve the value from the dataset.
        Number actualValue = dataset.getValue();

        // Assert: The retrieved value should be the exact same instance as the one
        // used to create the dataset.
        assertSame("The returned value should be the same instance provided to the constructor.",
                expectedValue, actualValue);
        
        // Also, verify the value is equal, which is a good secondary check.
        assertEquals("The returned value should be equal to the one provided to the constructor.",
                expectedValue, actualValue);
    }
}
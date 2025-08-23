package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * A test suite for the {@link RandomUtils} class.
 * This class focuses on verifying the contract of the static methods.
 */
public class RandomUtilsTest {

    /**
     * Tests that {@code RandomUtils.nextBytes(int)} returns a byte array
     * of the specified length.
     */
    @Test
    public void nextBytesShouldReturnArrayOfCorrectLength() {
        // Arrange: Define the expected length of the byte array.
        final int expectedLength = 5;

        // Act: Call the method under test.
        final byte[] result = RandomUtils.nextBytes(expectedLength);

        // Assert: Verify the properties of the returned array.
        // We cannot test for specific random values, but we can test the contract.
        assertNotNull("The returned byte array should not be null", result);
        assertEquals("The returned byte array should have the requested length",
                     expectedLength, result.length);
    }
}
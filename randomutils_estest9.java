package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    /**
     * Tests that calling nextBytes with a count of 0 returns an empty, non-null byte array.
     */
    @Test
    public void nextBytesShouldReturnEmptyArrayWhenCountIsZero() {
        // Act: Call the method with a count of 0
        final byte[] result = RandomUtils.nextBytes(0);

        // Assert: Verify the result is an empty, non-null array
        assertNotNull("The returned array should not be null.", result);
        assertEquals("The length of the returned array should be 0.", 0, result.length);
    }
}
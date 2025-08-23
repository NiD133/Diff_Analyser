package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link RandomUtils}.
 */
public class RandomUtilsTest {

    @Test
    public void randomBytes_withZeroCount_shouldReturnEmptyArray() {
        // Arrange
        // The specific RandomUtils instance (secure, insecure, etc.) does not matter
        // for this edge case, so we can use any of them.
        final RandomUtils randomUtils = RandomUtils.secure();
        final int zeroCount = 0;

        // Act
        final byte[] resultBytes = randomUtils.randomBytes(zeroCount);

        // Assert
        assertNotNull("The returned array should not be null.", resultBytes);
        assertEquals("Requesting zero bytes should return an empty array.", 0, resultBytes.length);
    }
}
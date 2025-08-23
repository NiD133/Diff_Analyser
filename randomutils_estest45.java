package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * Unit tests for the {@link RandomUtils} class.
 */
public class RandomUtilsTest {

    /**
     * Tests that the toString() method on an instance created via
     * RandomUtils.secure() returns a non-null string.
     */
    @Test
    public void toString_onSecureInstance_shouldReturnNonNull() {
        // Arrange: Get a secure instance of RandomUtils.
        RandomUtils secureRandomUtils = RandomUtils.secure();

        // Act: Call the toString() method.
        String result = secureRandomUtils.toString();

        // Assert: The result should not be null.
        assertNotNull("The toString() method should always return a non-null string.", result);
    }
}
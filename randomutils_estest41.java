package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

import java.security.SecureRandom;

/**
 * Unit tests for the {@link RandomUtils} class.
 */
public class RandomUtilsTest {

    /**
     * Tests that the internal {@code secureRandom()} method successfully creates
     * and returns a non-null {@link SecureRandom} instance.
     */
    @Test
    public void secureRandomShouldReturnNonNullInstance() {
        // Act: Call the static factory method to get a SecureRandom instance.
        final SecureRandom secureRandom = RandomUtils.secureRandom();

        // Assert: Verify that the returned instance is not null.
        assertNotNull("The secureRandom() method should always return a valid instance.", secureRandom);
    }
}
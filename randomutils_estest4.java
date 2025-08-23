package org.apache.commons.lang3;

import org.junit.Test;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    /**
     * Verifies that RandomUtils.insecure().random() returns a valid, non-null
     * instance of {@link Random}. As per the class documentation, this instance
     * should specifically be a {@link ThreadLocalRandom}.
     */
    @Test
    public void insecureRandomShouldReturnNonNullThreadLocalRandomInstance() {
        // Act: Obtain the Random instance from the insecure RandomUtils factory.
        final Random randomGenerator = RandomUtils.insecure().random();

        // Assert: Verify that the returned instance is not null and is of the expected type.
        assertNotNull("The Random instance from insecure() should not be null.", randomGenerator);
        assertTrue("The Random instance from insecure() should be a ThreadLocalRandom.",
                randomGenerator instanceof ThreadLocalRandom);
    }
}
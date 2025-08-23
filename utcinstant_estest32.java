package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Duration;
import java.time.Instant;

/**
 * Unit tests for {@link UtcInstant}.
 */
public class UtcInstantTest {

    /**
     * Tests that the plus() method throws a NullPointerException when a null duration is provided.
     * This adheres to the method's contract, which specifies the duration must not be null.
     */
    @Test(expected = NullPointerException.class)
    public void testPlus_throwsExceptionForNullDuration() {
        // Arrange: Create a standard UtcInstant instance. The specific value is not
        // important for this test, so a common constant like EPOCH is used for clarity.
        UtcInstant instant = UtcInstant.of(Instant.EPOCH);
        Duration nullDuration = null;

        // Act: Attempt to add a null duration to the instant.
        // The test framework will assert that a NullPointerException is thrown here.
        instant.plus(nullDuration);
    }
}
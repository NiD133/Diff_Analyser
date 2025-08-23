package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Duration;
import java.time.Instant;

/**
 * Unit tests for {@link UtcInstant}.
 */
public class UtcInstantTest {

    /**
     * Tests that calling minus() with a null duration throws a NullPointerException,
     * as the method contract requires a non-null argument.
     */
    @Test(expected = NullPointerException.class)
    public void minus_withNullDuration_throwsNullPointerException() {
        // Arrange: Create a standard reference instant. The specific value is not
        // important for this test, as we are testing null argument handling.
        UtcInstant instant = UtcInstant.of(Instant.EPOCH);

        // Act: Call the method under test with a null argument.
        // The @Test annotation will assert that a NullPointerException is thrown.
        instant.minus((Duration) null);
    }
}
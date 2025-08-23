package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Duration;

/**
 * Unit tests for the TaiInstant class.
 * This test focuses on handling null arguments in the plus() method.
 */
public class TaiInstantTest {

    /**
     * Tests that calling plus() with a null Duration throws a NullPointerException.
     * The contract of the plus(Duration) method specifies that the duration must not be null.
     */
    @Test(expected = NullPointerException.class)
    public void plus_whenDurationIsNull_throwsNullPointerException() {
        // Arrange: Create a TaiInstant instance. The specific value is not important.
        TaiInstant instant = TaiInstant.ofTaiSeconds(0L, 0L);
        Duration nullDuration = null;

        // Act: Attempt to add a null duration to the instant.
        // The test expects this line to throw a NullPointerException.
        instant.plus(nullDuration);

        // Assert: The expected exception is verified by the @Test(expected=...) annotation.
    }
}
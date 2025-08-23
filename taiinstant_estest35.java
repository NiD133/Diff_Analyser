package org.threeten.extra.scale;

import java.time.Duration;
import org.junit.Test;

/**
 * Tests for the {@link TaiInstant} class.
 */
public class TaiInstantTest {

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------

    /**
     * Tests that calling minus() with a null duration throws a NullPointerException,
     * as per the method's contract.
     */
    @Test(expected = NullPointerException.class)
    public void minus_whenDurationIsNull_throwsNullPointerException() {
        // Arrange: Create an arbitrary TaiInstant instance to call the method on.
        TaiInstant instant = TaiInstant.ofTaiSeconds(1L, 0L);
        Duration nullDuration = null;

        // Act: Call the minus method with a null argument.
        // The @Test annotation expects this to throw a NullPointerException.
        instant.minus(nullDuration);
    }
}
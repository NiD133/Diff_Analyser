package org.threeten.extra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.Clock;
import org.junit.Test;

/**
 * Tests for {@link DayOfYear}.
 */
public class DayOfYearTest {

    /**
     * Tests that DayOfYear.now() throws a NullPointerException when the provided Clock is null.
     * This is standard behavior for methods accepting objects that cannot be null.
     */
    @Test
    public void now_withNullClock_throwsNullPointerException() {
        // The method under test is expected to use Objects.requireNonNull,
        // which throws an NPE with a specific message.
        try {
            DayOfYear.now((Clock) null);
            fail("Expected a NullPointerException to be thrown for a null clock.");
        } catch (NullPointerException ex) {
            assertEquals("clock", ex.getMessage());
        }
    }
}
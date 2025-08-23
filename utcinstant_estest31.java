package org.threeten.extra.scale;

import org.junit.Test;
import java.time.ArithmeticException;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * This test focuses on handling arithmetic overflows within the {@link UtcInstant} class.
 */
public class UtcInstantOverflowTest {

    /**
     * Tests that converting a {@code UtcInstant} with the maximum possible
     * Modified Julian Day to a standard {@code java.time.Instant} throws
     * an {@code ArithmeticException}.
     * <p>
     * The conversion from a Modified Julian Day to epoch seconds requires a
     * multiplication that is expected to exceed the capacity of a {@code long}
     * when using {@code Long.MAX_VALUE}, leading to an overflow.
     */
    @Test
    public void toInstant_withMaximumModifiedJulianDay_throwsArithmeticException() {
        // Arrange: Create a UtcInstant representing a point in time so far in the
        // future that it cannot be represented by a standard Instant.
        // We use the maximum value for a Modified Julian Day to trigger an overflow.
        UtcInstant farFutureInstant = UtcInstant.ofModifiedJulianDay(Long.MAX_VALUE, 0L);

        // Act & Assert: Verify that attempting the conversion throws an ArithmeticException.
        ArithmeticException exception = assertThrows(
                "Converting a UtcInstant at Long.MAX_VALUE MJD should cause an overflow.",
                ArithmeticException.class,
                farFutureInstant::toInstant);

        // Optionally, assert on the exception message for more specific verification.
        assertTrue(
                "The exception message should indicate a 'long overflow'.",
                exception.getMessage().contains("long overflow"));
    }
}
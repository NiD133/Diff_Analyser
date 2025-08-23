package org.threeten.extra.scale;

import org.junit.Test;
import java.time.DateTimeException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link UtcInstant}.
 * This class focuses on improving a specific test case for understandability.
 */
public class UtcInstantTest {

    /**
     * Tests that converting a {@code UtcInstant} to a {@code java.time.Instant}
     * throws an exception if the value is too large to be represented.
     */
    @Test
    public void toInstant_whenUtcInstantExceedsMaxInstant_throwsDateTimeException() {
        // Arrange: Create a UtcInstant representing a point in time far beyond
        // the maximum value supported by java.time.Instant. The Modified Julian Day
        // value is intentionally chosen to be extremely large to trigger the exception.
        long farFutureModifiedJulianDay = 73281320003633L;
        UtcInstant utcInstantFarInFuture = UtcInstant.ofModifiedJulianDay(farFutureModifiedJulianDay, 0L);

        // Act & Assert
        try {
            utcInstantFarInFuture.toInstant();
            fail("Expected DateTimeException was not thrown for an out-of-range instant.");
        } catch (DateTimeException e) {
            // Verify that the exception message clearly indicates the cause of the error.
            assertEquals("Instant exceeds minimum or maximum instant", e.getMessage());
        }
    }
}
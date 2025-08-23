package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UtcInstant_ESTestTest44 extends UtcInstant_ESTest_scaffolding {

    /**
     * Tests that {@link UtcInstant#durationUntil(UtcInstant)} throws an ArithmeticException
     * when the instant has a Modified Julian Day at its maximum value.
     * <p>
     * This is an edge case test to ensure that potential internal {@code long} overflows
     * are handled by throwing an exception. The overflow can occur even when calculating
     * the duration to the same instant (which should logically be zero), because the
     * internal implementation may first convert the instant's components to a single
     * large number before performing subtraction.
     */
    @Test(timeout = 4000)
    public void durationUntil_withMaxMJD_throwsExceptionOnInternalOverflow() {
        // Arrange: Create an instant at the far-future boundary using Long.MAX_VALUE
        // for the Modified Julian Day. The nano-of-day is an arbitrary valid value.
        UtcInstant instantAtMaxMJD = UtcInstant.ofModifiedJulianDay(Long.MAX_VALUE, 73281320003515L);

        // Act & Assert: Attempting to calculate a duration should cause an overflow.
        try {
            instantAtMaxMJD.durationUntil(instantAtMaxMJD);
            fail("Expected an ArithmeticException due to long overflow, but none was thrown.");
        } catch (ArithmeticException e) {
            // Verify that the exception is the expected overflow.
            assertEquals("long overflow", e.getMessage());
        }
    }
}
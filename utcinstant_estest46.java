package org.threeten.extra.scale;

import org.junit.Test;
import java.time.DateTimeException;
import java.time.LocalDate;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

// Note: The original test class structure and runner annotations are preserved,
// as the request is to improve the understandability of the test case itself.
public class UtcInstant_ESTestTest46 extends UtcInstant_ESTest_scaffolding {

    /**
     * Tests that toString() throws a DateTimeException when the Modified Julian Day
     * is too large to be represented by a java.time.LocalDate.
     */
    @Test(timeout = 4000)
    public void toString_throwsException_whenModifiedJulianDayExceedsLocalDateRange() {
        // ARRANGE
        // The UtcInstant.toString() method internally converts its state to a
        // java.time.LocalDate to build the string representation. This test verifies
        // that toString() fails if the Modified Julian Day (MJD) is outside the
        // supported range of LocalDate.

        // The conversion logic in UtcInstant.buildToString() is:
        // LocalDate.ofEpochDay(mjDay + OFFSET_MJD_EPOCH)
        // where OFFSET_MJD_EPOCH is 40587L.
        final long MJD_CONVERSION_OFFSET = 40587L;

        // Calculate the maximum valid MJD that can be successfully converted to a LocalDate.
        long maxSupportedEpochDay = LocalDate.MAX.toEpochDay();
        long maxValidMjd = maxSupportedEpochDay - MJD_CONVERSION_OFFSET;

        // Create a UtcInstant with an MJD value that is just outside the valid range.
        // The nanoOfDay is set to 0 as it is not relevant to this failure condition.
        long outOfRangeMjd = maxValidMjd + 1;
        UtcInstant instantWithLargeMjd = UtcInstant.ofModifiedJulianDay(outOfRangeMjd, 0L);

        // ACT & ASSERT
        try {
            instantWithLargeMjd.toString();
            fail("Expected a DateTimeException for an MJD value outside the LocalDate range.");
        } catch (DateTimeException e) {
            // The exception is thrown by LocalDate.ofEpochDay. We verify its message
            // to confirm the cause of the failure.
            String expectedMessageContent = "Invalid value for EpochDay";
            assertTrue(
                "Exception message should indicate an invalid epoch day. Got: " + e.getMessage(),
                e.getMessage().contains(expectedMessageContent)
            );
        }
    }
}
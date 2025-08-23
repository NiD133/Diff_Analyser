package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class contains tests for EthiopicChronology, focusing on edge cases.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class EthiopicChronologyTest {

    /**
     * Tests that isLeapDay throws an ArithmeticException when the instant is Long.MIN_VALUE
     * and the time zone has a negative offset. This combination causes an integer overflow
     * during the internal UTC-to-local time conversion.
     */
    @Test(timeout = 4000)
    public void isLeapDay_throwsArithmeticException_onOverflowWithMinInstantAndNegativeOffset() {
        // Arrange: Create a chronology with a time zone that has a negative offset.
        // The isLeapDay method needs to convert the UTC instant to a local instant.
        // This conversion (localMillis = utcMillis + offset) will cause an arithmetic overflow
        // when the instant is Long.MIN_VALUE and the offset is negative.
        final DateTimeZone negativeOffsetZone = DateTimeZone.forOffsetMillis(-4246);
        final EthiopicChronology ethiopicChronology = EthiopicChronology.getInstance(negativeOffsetZone);
        final long minInstant = Long.MIN_VALUE;

        // Act & Assert
        try {
            ethiopicChronology.isLeapDay(minInstant);
            fail("Expected an ArithmeticException due to an overflow when applying the time zone offset.");
        } catch (ArithmeticException e) {
            // Verify that the expected exception was thrown with the correct message from the underlying DateTimeZone class.
            assertEquals("Adding time zone offset caused overflow", e.getMessage());
        }
    }
}
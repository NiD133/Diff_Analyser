package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.time.Instant;
import org.junit.Test;

/**
 * This class contains the improved test case for the FileTimes class.
 * In a real-world scenario, this test would be part of a comprehensive FileTimesTest suite.
 */
public class FileTimes_ESTestTest20 {

    /**
     * Tests that {@link FileTimes#toNtfsTime(Instant)} throws an ArithmeticException
     * when the provided Instant is so far in the future that its representation in
     * NTFS time (100-nanosecond intervals since 1601) would overflow a {@code long}.
     */
    @Test
    public void toNtfsTime_whenInstantIsTooLarge_throwsArithmeticException() {
        // ARRANGE: An Instant so far in the future that its NTFS time representation
        // would exceed the capacity of a long. Instant.MAX is a clear, self-documenting
        // constant for this purpose.
        final Instant veryLargeInstant = Instant.MAX;

        // ACT & ASSERT: The conversion should fail with an ArithmeticException because
        // the resulting NTFS time value exceeds Long.MAX_VALUE.
        final ArithmeticException exception = assertThrows(ArithmeticException.class, () -> {
            FileTimes.toNtfsTime(veryLargeInstant);
        });

        // ASSERT: Verify the exception message to confirm the cause of the failure.
        assertEquals("Overflow", exception.getMessage());
    }
}
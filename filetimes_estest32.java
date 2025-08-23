package org.apache.commons.io.file.attribute;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.file.attribute.FileTime;
import java.time.DateTimeException;
import java.time.Instant;

/**
 * Tests for date-time arithmetic in the {@link FileTimes} utility class,
 * specifically focusing on boundary conditions that cause exceptions.
 */
public class FileTimesArithmeticTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that {@link FileTimes#minusSeconds(FileTime, long)} throws a {@link DateTimeException}
     * when subtracting a large number of seconds results in a time that precedes the minimum
     * supported {@link Instant} (an underflow).
     */
    @Test
    public void minusSecondsShouldThrowExceptionOnInstantUnderflow() {
        // Arrange
        final FileTime now = FileTimes.now();
        // A very large number of seconds, guaranteed to cause an underflow
        // below the minimum representable Instant.
        final long largeSecondsToSubtract = Long.MAX_VALUE;

        // Assert: Configure the expected exception and its message.
        thrown.expect(DateTimeException.class);
        thrown.expectMessage("Instant exceeds minimum or maximum instant");

        // Act: This call is expected to throw the configured exception.
        FileTimes.minusSeconds(now, largeSecondsToSubtract);
    }
}
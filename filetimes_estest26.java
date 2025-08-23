package org.apache.commons.io.file.attribute;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.attribute.FileTime;
import java.time.DateTimeException;
import java.time.Instant;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link FileTimes}.
 */
class FileTimesTest {

    @Test
    void plusMillis_whenResultIsBeforeMinInstant_throwsDateTimeException() {
        // Arrange: Create a FileTime at the earliest possible moment (Instant.MIN).
        // Any attempt to subtract time from this should cause an underflow.
        final FileTime minimumFileTime = FileTime.from(Instant.MIN);
        final long negativeMillis = -1L;

        // Act & Assert: Verify that adding a negative millisecond value throws a
        // DateTimeException because the result would be before the minimum supported Instant.
        final DateTimeException exception = assertThrows(DateTimeException.class, () -> {
            FileTimes.plusMillis(minimumFileTime, negativeMillis);
        });

        // Further assert on the exception message to ensure it's the expected error.
        assertEquals("Instant exceeds minimum or maximum instant", exception.getMessage());
    }
}
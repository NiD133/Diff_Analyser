package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertFalse;

import java.time.Instant;
import org.junit.Test;

/**
 * Tests for the {@link FileTimes} utility class.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#isUnixTime(long)} returns false for a number of seconds
     * that exceeds the maximum value representable by {@link Instant}.
     * <p>
     * A {@code FileTime} is internally based on {@code Instant}, so its range of supported
     * seconds is limited by {@code Instant.MIN.getEpochSecond()} and {@code Instant.MAX.getEpochSecond()}.
     * This test verifies the behavior for a value just outside this upper boundary.
     * </p>
     */
    @Test
    public void isUnixTime_shouldReturnFalse_whenSecondsExceedMaxSupportedValue() {
        // Arrange: Define a value that is one second greater than the maximum supported by Instant.
        final long secondsBeyondMax = Instant.MAX.getEpochSecond() + 1L;

        // Act: Check if this out-of-range value is considered a valid Unix time.
        final boolean isWithinRange = FileTimes.isUnixTime(secondsBeyondMax);

        // Assert: The method should correctly identify the value as out of range.
        assertFalse("A seconds value greater than Instant.MAX.getEpochSecond() should be considered invalid.", isWithinRange);
    }
}
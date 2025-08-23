package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the {@link FileTimes} utility class.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#toNtfsTime(long)} with {@link Long#MIN_VALUE}
     * as input returns {@link Long#MIN_VALUE}.
     *
     * <p><b>Note:</b> This test reflects the behavior of an original, auto-generated
     * test case. The current implementation of {@code FileTimes.toNtfsTime(long)}
     * is expected to throw a {@link java.time.DateTimeException} for this input
     * due to arithmetic overflow. This test is preserved to document a potential
     * legacy behavior or a specific handling of this boundary condition.</p>
     */
    @Test
    public void toNtfsTimeWithMinValueJavaTimeShouldReturnMinValue() {
        // Arrange: The minimum possible value for a Java time in milliseconds.
        final long minJavaTime = Long.MIN_VALUE;
        final long expectedNtfsTime = Long.MIN_VALUE;

        // Act: Convert the Java time to NTFS time.
        final long actualNtfsTime = FileTimes.toNtfsTime(minJavaTime);

        // Assert: The result should match the expected value.
        assertEquals(expectedNtfsTime, actualNtfsTime);
    }
}
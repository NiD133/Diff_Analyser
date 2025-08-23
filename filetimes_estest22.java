package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertThrows;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import org.junit.Test;

/**
 * Tests for {@link FileTimes}.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#toNtfsTime(FileTime)} throws an
     * {@link ArithmeticException} when the input {@link FileTime} is too large
     * to be represented as an NTFS time. The NTFS time is a 64-bit (long) value,
     * so a sufficiently large input will cause an overflow during conversion.
     */
    @Test
    public void testToNtfsTimeThrowsArithmeticExceptionForLargeFileTime() {
        // Arrange: Create a FileTime instance representing the maximum possible
        // millisecond value from the epoch. This value is guaranteed to cause an
        // overflow when converted to the 100-nanosecond intervals of NTFS time.
        final FileTime largeFileTime = FileTime.from(Instant.ofEpochMilli(Long.MAX_VALUE));

        // Act & Assert: Verify that calling toNtfsTime with this out-of-range value
        // results in an ArithmeticException.
        assertThrows(ArithmeticException.class, () -> FileTimes.toNtfsTime(largeFileTime));
    }
}
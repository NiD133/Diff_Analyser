package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests for {@link FileTimes}.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#isUnixTime(long)} correctly identifies the maximum
     * 32-bit signed integer value as a valid Unix time. This is a significant
     * boundary case, often associated with the "Year 2038 problem".
     */
    @Test
    public void isUnixTime_with32BitIntegerMaxValue_shouldReturnTrue() {
        // Arrange: The maximum value for a 32-bit signed integer, a known
        // boundary for traditional Unix time.
        final long max32BitIntValue = Integer.MAX_VALUE;

        // Act: Check if this value is considered a valid Unix time.
        final boolean result = FileTimes.isUnixTime(max32BitIntValue);

        // Assert: The result should be true.
        assertTrue("The maximum 32-bit signed integer should be a valid Unix time", result);
    }
}
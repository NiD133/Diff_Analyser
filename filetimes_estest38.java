package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;

import java.nio.file.attribute.FileTime;
import org.junit.Test;

/**
 * Tests for the {@link FileTimes} utility class.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#toUnixTime(FileTime)} returns 0 when the input is null,
     * as specified by its Javadoc contract.
     */
    @Test
    public void testToUnixTime_withNullInput_shouldReturnZero() {
        // Arrange: The input is a null FileTime.
        final FileTime nullFileTime = null;
        final long expectedResult = 0L;

        // Act: Call the method under test.
        final long actualResult = FileTimes.toUnixTime(nullFileTime);

        // Assert: Verify that the result is 0.
        assertEquals(expectedResult, actualResult);
    }
}
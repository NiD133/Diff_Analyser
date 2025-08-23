package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertNull;

import java.nio.file.attribute.FileTime;
import java.util.Date;

import org.junit.Test;

/**
 * Tests for {@link FileTimes}.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#toDate(FileTime)} returns null when the input is null,
     * as specified by its Javadoc.
     */
    @Test
    public void testToDateWithNullInputShouldReturnNull() {
        // Arrange: The input FileTime is null.
        final FileTime nullFileTime = null;

        // Act: Call the method under test.
        final Date result = FileTimes.toDate(nullFileTime);

        // Assert: The result should also be null.
        assertNull("Expected a null Date when the input FileTime is null", result);
    }
}
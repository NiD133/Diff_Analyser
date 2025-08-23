package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertNull;

import java.nio.file.attribute.FileTime;
import java.util.Date;
import org.junit.Test;

/**
 * Tests for the {@link FileTimes} utility class.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#toFileTime(Date)} correctly handles a null input
     * by returning null, as specified in its contract.
     */
    @Test
    public void toFileTime_withNullDate_shouldReturnNull() {
        // Arrange: A null Date object.
        final Date nullDate = null;

        // Act: Attempt to convert the null Date to a FileTime.
        final FileTime result = FileTimes.toFileTime(nullDate);

        // Assert: The resulting FileTime should also be null.
        assertNull("The conversion of a null Date should result in a null FileTime.", result);
    }
}
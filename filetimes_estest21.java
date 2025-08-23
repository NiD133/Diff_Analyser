package org.apache.commons.io.file.attribute;

import java.nio.file.attribute.FileTime;
import org.junit.Test;

/**
 * Tests for {@link FileTimes}.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#toNtfsTime(FileTime)} throws a NullPointerException
     * when the input FileTime is null.
     */
    @Test(expected = NullPointerException.class)
    public void toNtfsTime_withNullFileTime_shouldThrowNullPointerException() {
        // This call is expected to throw a NullPointerException.
        FileTimes.toNtfsTime((FileTime) null);
    }
}
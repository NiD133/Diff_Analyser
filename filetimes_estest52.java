package org.apache.commons.io.file.attribute;

import java.nio.file.Path;
import org.junit.Test;

/**
 * Tests for the {@link FileTimes} utility class.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#setLastModifiedTime(Path)} throws a
     * NullPointerException when the path argument is null. This is the
     * expected behavior as per the contract of {@link java.nio.file.Files#setLastModifiedTime}.
     */
    @Test(expected = NullPointerException.class)
    public void setLastModifiedTime_withNullPath_throwsNullPointerException() {
        FileTimes.setLastModifiedTime((Path) null);
    }
}
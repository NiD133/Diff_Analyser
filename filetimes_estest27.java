package org.apache.commons.io.file.attribute;

import org.junit.Test;

/**
 * Tests for the {@link FileTimes} utility class.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#plusMillis(java.nio.file.attribute.FileTime, long)}
     * throws a NullPointerException when the fileTime parameter is null.
     */
    @Test(expected = NullPointerException.class)
    public void plusMillisShouldThrowNullPointerExceptionForNullFileTime() {
        // The 'expected' attribute of the @Test annotation asserts that this method call
        // must throw a NullPointerException for the test to pass.
        FileTimes.plusMillis(null, 24L);
    }
}
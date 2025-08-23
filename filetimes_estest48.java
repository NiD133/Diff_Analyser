package org.apache.commons.io.file.attribute;

import org.junit.Test;
import java.nio.file.attribute.FileTime;

/**
 * Unit tests for the {@link FileTimes} utility class.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#plusNanos(FileTime, long)} throws a
     * NullPointerException when the fileTime parameter is null.
     */
    @Test(expected = NullPointerException.class)
    public void testPlusNanosThrowsNullPointerExceptionForNullInput() {
        // The exact number of nanoseconds is irrelevant for this test, as the
        // method should perform the null check first.
        FileTimes.plusNanos(null, 1_000L);
    }
}
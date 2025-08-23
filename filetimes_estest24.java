package org.apache.commons.io.file.attribute;

import org.junit.Test;
import java.nio.file.attribute.FileTime;

/**
 * Tests for the {@link FileTimes} utility class, focusing on edge cases.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#plusSeconds(FileTime, long)} throws a
     * NullPointerException when the input FileTime is null.
     */
    @Test(expected = NullPointerException.class)
    public void testPlusSecondsWithNullFileTimeThrowsNullPointerException() {
        // Arrange: The value for seconds to add is arbitrary, as the method should
        // throw an exception before this value is used.
        final long arbitrarySecondsToAdd = 848L;

        // Act & Assert:
        // Call the method with a null FileTime.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        FileTimes.plusSeconds(null, arbitrarySecondsToAdd);
    }
}
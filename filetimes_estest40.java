package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertTrue;

import java.nio.file.attribute.FileTime;
import org.junit.Test;

/**
 * Tests for the {@link FileTimes} utility class.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#isUnixTime(FileTime)} returns true for a null input,
     * as specified in the method's documentation.
     */
    @Test
    public void isUnixTimeShouldReturnTrueForNullInput() {
        // Arrange: The input is a null FileTime. The explicit cast clarifies which
        // method overload is being tested.
        final FileTime nullFileTime = null;

        // Act: Call the method under test.
        final boolean result = FileTimes.isUnixTime(nullFileTime);

        // Assert: Verify that the result is true, as per the contract.
        assertTrue("A null FileTime should be considered a valid Unix time.", result);
    }
}
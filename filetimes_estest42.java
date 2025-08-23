package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link FileTimes}.
 *
 * This test suite focuses on improving the understandability of a single,
 * auto-generated test case for the {@code toNtfsTime(long)} method.
 */
public class FileTimesTest {

    /**
     * Tests that converting a very large Java time (in milliseconds) to NTFS time
     * correctly handles arithmetic overflow by returning {@link Long#MAX_VALUE}.
     *
     * <p>The conversion formula involves multiplication that can exceed the capacity of a
     * {@code long}. This test verifies that the implementation caps the result at the
     * maximum possible value for a long instead of wrapping around and producing an
     * incorrect value.</p>
     */
    @Test
    public void testToNtfsTimeFromJavaTimeOverflowsToLongMax() {
        // Arrange: A Java time so large that its conversion to NTFS time will exceed Long.MAX_VALUE.
        // Using Long.MAX_VALUE makes the intent of testing the upper boundary explicit.
        final long largeJavaTime = Long.MAX_VALUE;

        // Act: Attempt to convert the large Java time to NTFS time.
        final long ntfsTime = FileTimes.toNtfsTime(largeJavaTime);

        // Assert: The result should be capped at Long.MAX_VALUE, not an overflowed, incorrect value.
        assertEquals(Long.MAX_VALUE, ntfsTime);
    }
}
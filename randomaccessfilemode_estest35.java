package org.apache.commons.io;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link RandomAccessFileMode} enum.
 */
public class RandomAccessFileModeTest {

    /**
     * Tests that getMode() for READ_WRITE_SYNC_ALL returns the correct string "rws".
     */
    @Test
    public void testGetModeForReadWriteSyncAllReturnsCorrectString() {
        // Arrange
        final RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_ALL;
        final String expectedModeString = "rws";

        // Act
        final String actualModeString = mode.getMode();

        // Assert
        assertEquals("The mode string should match the expected value.", expectedModeString, actualModeString);
    }
}
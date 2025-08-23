package org.apache.commons.io;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the {@link RandomAccessFileMode} enum.
 */
public class RandomAccessFileModeTest {

    /**
     * Tests that {@link RandomAccessFileMode#valueOfMode(String)} correctly maps
     * the "rws" string to the READ_WRITE_SYNC_ALL enum constant.
     */
    @Test
    public void valueOfMode_shouldReturnReadWriteSyncAll_forRwsString() {
        // Arrange
        final String modeString = "rws";
        final RandomAccessFileMode expectedMode = RandomAccessFileMode.READ_WRITE_SYNC_ALL;

        // Act
        final RandomAccessFileMode actualMode = RandomAccessFileMode.valueOfMode(modeString);

        // Assert
        assertEquals("The mode string 'rws' should map to READ_WRITE_SYNC_ALL", expectedMode, actualMode);
    }
}
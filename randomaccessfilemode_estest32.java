package org.apache.commons.io;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link RandomAccessFileMode} enum.
 */
public class RandomAccessFileModeTest {

    /**
     * Tests that {@code RandomAccessFileMode.valueOfMode("rwd")} correctly returns
     * the {@code READ_WRITE_SYNC_CONTENT} enum constant.
     */
    @Test
    public void valueOfMode_withRwdString_returnsReadWriteSyncContent() {
        // Arrange
        final String rwdModeString = "rwd";
        final RandomAccessFileMode expectedMode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;

        // Act
        final RandomAccessFileMode actualMode = RandomAccessFileMode.valueOfMode(rwdModeString);

        // Assert
        assertEquals("The mode string 'rwd' should map to READ_WRITE_SYNC_CONTENT",
                     expectedMode, actualMode);
    }
}
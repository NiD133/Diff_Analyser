package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link CpioUtil} class.
 */
public class CpioUtilTest {

    /**
     * Tests that the fileType() method correctly extracts the file type bits
     * from a mode that also contains permission bits.
     */
    @Test
    public void fileTypeShouldExtractFileTypeBitsFromFullMode() {
        // Arrange: Create a mode for a regular file with rwxr-xr-x permissions.
        // CpioConstants.S_IFREG is the constant for a regular file.
        // 0755L is the octal representation of the permission bits.
        final long permissions = 0755L;
        final long mode = CpioConstants.S_IFREG | permissions;
        
        // Act: Call the method under test to extract the file type.
        final long actualFileType = CpioUtil.fileType(mode);

        // Assert: The result should be only the file type, with permission bits removed.
        assertEquals(CpioConstants.S_IFREG, actualFileType);
    }

    /**
     * Tests that the fileType() method returns 0 when the mode contains
     * only permission bits and no file type information.
     */
    @Test
    public void fileTypeShouldReturnZeroWhenModeHasNoFileTypeBits() {
        // Arrange: A mode with only permission bits.
        final long modeWithOnlyPermissions = 0755L;

        // Act: Extract the file type.
        final long actualFileType = CpioUtil.fileType(modeWithOnlyPermissions);

        // Assert: The result should be 0, as no file type bits were set.
        assertEquals(0L, actualFileType);
    }
}
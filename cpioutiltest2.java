package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * This class contains unit tests for the {@link CpioUtil} class.
 */
public class CpioUtilTest {

    /**
     * Tests that the {@code fileType()} method correctly returns 0 when the
     * input mode is 0, which signifies a default or non-specific file type.
     */
    @Test
    public void fileTypeShouldReturnZeroWhenModeIsZero() {
        // Arrange: Define the input mode for the test.
        final long mode = 0L;
        final long expectedFileType = 0L;

        // Act: Call the method under test.
        final long actualFileType = CpioUtil.fileType(mode);

        // Assert: Verify that the result matches the expected value.
        assertEquals(expectedFileType, actualFileType);
    }
}
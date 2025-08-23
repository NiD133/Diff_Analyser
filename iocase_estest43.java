package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.File;

/**
 * Tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that IOCase.isCaseSensitive() for the SYSTEM constant
     * correctly reflects the case sensitivity of the host operating system.
     */
    @Test
    public void isCaseSensitive_forSystem_shouldReflectFileSystemSensitivity() {
        // Arrange: Determine the expected sensitivity based on the OS file separator.
        // The IOCase documentation specifies that for the SYSTEM constant, sensitivity is
        // determined by the file separator: '/' for sensitive (Unix-like) and '\' for
        // insensitive (Windows).
        final boolean expectedSensitivity = (File.separatorChar == '/');

        // Act: Call the method under test with the SYSTEM constant.
        final boolean actualSensitivity = IOCase.isCaseSensitive(IOCase.SYSTEM);

        // Assert: The result should match the expected sensitivity for the current OS.
        // This makes the test robust and portable across different operating systems.
        assertEquals("IOCase.SYSTEM sensitivity should match the underlying OS",
                expectedSensitivity, actualSensitivity);
    }
}
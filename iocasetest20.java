package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the static {@link IOCase#isCaseSensitive(IOCase)} method.
 */
class IOCaseTest {

    @Test
    @DisplayName("isCaseSensitive should return true for SENSITIVE")
    void isCaseSensitive_whenSensitive_shouldReturnTrue() {
        assertTrue(IOCase.isCaseSensitive(IOCase.SENSITIVE));
    }

    @Test
    @DisplayName("isCaseSensitive should return false for INSENSITIVE")
    void isCaseSensitive_whenInsensitive_shouldReturnFalse() {
        assertFalse(IOCase.isCaseSensitive(IOCase.INSENSITIVE));
    }

    @Test
    @DisplayName("isCaseSensitive should return false for null input")
    void isCaseSensitive_whenNull_shouldReturnFalse() {
        // The method is documented as being null-safe and should return false for null.
        assertFalse(IOCase.isCaseSensitive(null));
    }

    @Test
    @DisplayName("isCaseSensitive for SYSTEM should match the underlying OS sensitivity")
    void isCaseSensitive_whenSystem_shouldDependOnOperatingSystem() {
        // The behavior of IOCase.SYSTEM depends on the operating system.
        final boolean isWindows = File.separatorChar == '\\';

        if (isWindows) {
            // On Windows, file systems are typically case-insensitive.
            assertFalse(IOCase.isCaseSensitive(IOCase.SYSTEM),
                "IOCase.SYSTEM should be case-insensitive on Windows.");
        } else {
            // On other systems (like Unix), file systems are typically case-sensitive.
            assertTrue(IOCase.isCaseSensitive(IOCase.SYSTEM),
                "IOCase.SYSTEM should be case-sensitive on non-Windows systems.");
        }
    }
}
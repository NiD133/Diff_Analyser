package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link IOCase} enum.
 */
@DisplayName("IOCase Enum")
class IOCaseTest {

    /**
     * Contains tests for the {@link IOCase#isCaseSensitive()} method.
     */
    @Nested
    @DisplayName("isCaseSensitive() method")
    class IsCaseSensitiveTest {

        @Test
        @DisplayName("should return true for SENSITIVE")
        void forSensitive_shouldReturnTrue() {
            assertTrue(IOCase.SENSITIVE.isCaseSensitive(),
                "IOCase.SENSITIVE must be case-sensitive.");
        }

        @Test
        @DisplayName("should return false for INSENSITIVE")
        void forInsensitive_shouldReturnFalse() {
            assertFalse(IOCase.INSENSITIVE.isCaseSensitive(),
                "IOCase.INSENSITIVE must not be case-sensitive.");
        }

        @Test
        @DisplayName("should correctly reflect the operating system's sensitivity for SYSTEM")
        void forSystem_shouldReflectOperatingSystem() {
            // The expected sensitivity for IOCase.SYSTEM depends on the OS.
            // It's sensitive for Unix-like systems (separator '/') and
            // insensitive for Windows (separator '\').
            final boolean isOsCaseSensitive = (File.separatorChar != '\\');

            assertEquals(isOsCaseSensitive, IOCase.SYSTEM.isCaseSensitive(),
                "IOCase.SYSTEM sensitivity should match the underlying OS.");
        }
    }
}
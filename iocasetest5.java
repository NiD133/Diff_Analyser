package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link IOCase#checkEquals(String, String)} method.
 */
@DisplayName("IOCase.checkEquals()")
class IOCaseCheckEqualsTest {

    private static final boolean IS_WINDOWS = File.separatorChar == '\\';

    @Nested
    @DisplayName("when case is SENSITIVE")
    class SensitiveCase {

        @Test
        @DisplayName("should return true for identical strings")
        void returnsTrueForIdenticalStrings() {
            assertTrue(IOCase.SENSITIVE.checkEquals("ABC", "ABC"));
        }

        @Test
        @DisplayName("should return false for strings with different casing")
        void returnsFalseForDifferentCaseStrings() {
            assertFalse(IOCase.SENSITIVE.checkEquals("ABC", "Abc"));
        }
    }

    @Nested
    @DisplayName("when case is INSENSITIVE")
    class InsensitiveCase {

        @Test
        @DisplayName("should return true for identical strings")
        void returnsTrueForIdenticalStrings() {
            assertTrue(IOCase.INSENSITIVE.checkEquals("ABC", "ABC"));
        }

        @Test
        @DisplayName("should return true for strings with different casing")
        void returnsTrueForDifferentCaseStrings() {
            assertTrue(IOCase.INSENSITIVE.checkEquals("ABC", "Abc"));
        }
    }

    @Nested
    @DisplayName("when case is SYSTEM")
    class SystemCase {

        @Test
        @DisplayName("should return true for identical strings")
        void returnsTrueForIdenticalStrings() {
            assertTrue(IOCase.SYSTEM.checkEquals("ABC", "ABC"));
        }

        @Test
        @DisplayName("should behave according to the operating system for strings with different casing")
        void returnsOSDependentResultForDifferentCaseStrings() {
            // On Windows (case-insensitive FS), this should be true.
            // On Unix-like systems (case-sensitive FS), this should be false.
            final boolean expected = IS_WINDOWS;
            assertEquals(expected, IOCase.SYSTEM.checkEquals("ABC", "Abc"));
        }
    }
}
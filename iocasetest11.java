package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link IOCase#checkStartsWith(String, String)} method.
 */
@DisplayName("IOCase.checkStartsWith()")
class IOCaseTest {

    private static final boolean IS_WINDOWS = File.separatorChar == '\\';

    @Nested
    @DisplayName("when using SENSITIVE")
    class Sensitive {

        @Test
        @DisplayName("should return true for an exact case prefix match")
        void returnsTrueForExactMatch() {
            assertTrue(IOCase.SENSITIVE.checkStartsWith("ABC", "AB"),
                "SENSITIVE should find a prefix with an exact case match.");
        }

        @Test
        @DisplayName("should return false for a different case prefix match")
        void returnsFalseForDifferentCase() {
            assertFalse(IOCase.SENSITIVE.checkStartsWith("ABC", "Ab"),
                "SENSITIVE should not find a prefix with a different case.");
        }
    }

    @Nested
    @DisplayName("when using INSENSITIVE")
    class Insensitive {

        @Test
        @DisplayName("should return true for an exact case prefix match")
        void returnsTrueForExactMatch() {
            assertTrue(IOCase.INSENSITIVE.checkStartsWith("ABC", "AB"),
                "INSENSITIVE should find a prefix with an exact case match.");
        }

        @Test
        @DisplayName("should return true for a different case prefix match")
        void returnsTrueForDifferentCase() {
            assertTrue(IOCase.INSENSITIVE.checkStartsWith("ABC", "Ab"),
                "INSENSITIVE should find a prefix even with a different case.");
        }
    }

    @Nested
    @DisplayName("when using SYSTEM")
    class System {

        @Test
        @DisplayName("should return true for an exact case prefix match")
        void returnsTrueForExactMatch() {
            assertTrue(IOCase.SYSTEM.checkStartsWith("ABC", "AB"),
                "SYSTEM should always find a prefix with an exact case match.");
        }

        @Test
        @DisplayName("should behave case-insensitively on Windows and sensitively otherwise")
        void behavesAccordingToOS() {
            final String osBehavior = IS_WINDOWS ? "case-insensitive" : "case-sensitive";
            assertEquals(IS_WINDOWS, IOCase.SYSTEM.checkStartsWith("ABC", "Ab"),
                "SYSTEM should be " + osBehavior + " on the current OS.");
        }
    }
}
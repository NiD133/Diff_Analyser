package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link IOCase} enum.
 */
@DisplayName("IOCase")
class IOCaseTest {

    private static final boolean IS_WINDOWS = File.separatorChar == '\\';

    @Nested
    @DisplayName("checkCompareTo method")
    class CheckCompareToTests {

        @Test
        @DisplayName("SENSITIVE should perform a case-sensitive comparison")
        void whenSensitive_checkCompareTo_shouldBeCaseSensitive() {
            // Equal strings
            assertEquals(0, IOCase.SENSITIVE.checkCompareTo("ABC", "ABC"));

            // Different case
            assertTrue(IOCase.SENSITIVE.checkCompareTo("ABC", "abc") < 0, "'ABC' should be less than 'abc'");
            assertTrue(IOCase.SENSITIVE.checkCompareTo("abc", "ABC") > 0, "'abc' should be greater than 'ABC'");
        }

        @Test
        @DisplayName("INSENSITIVE should perform a case-insensitive comparison")
        void whenInsensitive_checkCompareTo_shouldBeCaseInsensitive() {
            // Equal strings
            assertEquals(0, IOCase.INSENSITIVE.checkCompareTo("ABC", "ABC"));

            // Different case should be treated as equal
            assertEquals(0, IOCase.INSENSITIVE.checkCompareTo("ABC", "abc"));
            assertEquals(0, IOCase.INSENSITIVE.checkCompareTo("abc", "ABC"));
        }

        @Test
        @DisplayName("SYSTEM should perform a system-dependent comparison")
        void whenSystem_checkCompareTo_shouldDependOnOperatingSystem() {
            // Equal strings
            assertEquals(0, IOCase.SYSTEM.checkCompareTo("ABC", "ABC"));

            // Behavior for different-cased strings depends on the OS
            if (IS_WINDOWS) {
                // On Windows, comparison is case-insensitive
                assertEquals(0, IOCase.SYSTEM.checkCompareTo("ABC", "abc"), "On Windows, comparison should be case-insensitive");
                assertEquals(0, IOCase.SYSTEM.checkCompareTo("abc", "ABC"), "On Windows, comparison should be case-insensitive");
            } else {
                // On non-Windows (e.g., Unix), comparison is case-sensitive
                assertTrue(IOCase.SYSTEM.checkCompareTo("ABC", "abc") < 0, "On non-Windows, comparison should be case-sensitive");
                assertTrue(IOCase.SYSTEM.checkCompareTo("abc", "ABC") > 0, "On non-Windows, comparison should be case-sensitive");
            }
        }
    }
}
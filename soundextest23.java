package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the static utility methods in {@link SoundexUtils}.
 */
class SoundexUtilsTest {

    @Nested
    @DisplayName("clean() method")
    class CleanTests {

        @Test
        @DisplayName("should return null when the input is null")
        void cleanWithNullInputReturnsNull() {
            assertNull(SoundexUtils.clean(null));
        }

        @Test
        @DisplayName("should return an empty string when the input is empty")
        void cleanWithEmptyInputReturnsEmptyString() {
            assertEquals("", SoundexUtils.clean(""));
        }
    }

    @Nested
    @DisplayName("differenceEncoded() method")
    class DifferenceEncodedTests {

        @Test
        @DisplayName("should return 0 when the first string is null")
        void differenceEncodedWithNullFirstArgumentReturnsZero() {
            // The content of the second string is irrelevant for this null-check test.
            assertEquals(0, SoundexUtils.differenceEncoded(null, "ANY_STRING"));
        }

        @Test
        @DisplayName("should return 0 when the second string is null")
        void differenceEncodedWithNullSecondArgumentReturnsZero() {
            // The content of the first string is irrelevant for this null-check test.
            assertEquals(0, SoundexUtils.differenceEncoded("ANY_STRING", null));
        }
    }
}
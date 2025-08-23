package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharSetUtils}.
 */
class CharSetUtilsTest {

    @Nested
    @DisplayName("Tests for keep(String, String...)")
    class KeepTests {

        @Test
        @DisplayName("should return null when the input string is null")
        void shouldReturnNullForNullString() {
            assertNull(CharSetUtils.keep(null, (String) null), "keep(null, null) should be null");
            assertNull(CharSetUtils.keep(null, ""), "keep(null, \"\") should be null");
            assertNull(CharSetUtils.keep(null, "a-z"), "keep(null, \"a-z\") should be null");
        }

        @Test
        @DisplayName("should return an empty string when the input string is empty")
        void shouldReturnEmptyStringForEmptyString() {
            assertEquals("", CharSetUtils.keep("", (String) null), "keep(\"\", null) should be \"\"");
            assertEquals("", CharSetUtils.keep("", ""), "keep(\"\", \"\") should be \"\"");
            assertEquals("", CharSetUtils.keep("", "a-e"), "keep(\"\", \"a-e\") should be \"\"");
        }

        @Test
        @DisplayName("should return an empty string for a non-empty string with a null or empty set")
        void shouldReturnEmptyStringForNullOrEmptySet() {
            assertEquals("", CharSetUtils.keep("hello", (String) null), "A null set should result in an empty string");
            assertEquals("", CharSetUtils.keep("hello", ""), "An empty set should result in an empty string");
        }

        @Test
        @DisplayName("should return an empty string when no characters match the set")
        void shouldReturnEmptyStringWhenNoCharsMatch() {
            assertEquals("", CharSetUtils.keep("hello", "xyz"));
        }

        @Test
        @DisplayName("should return the original string when all characters match the set")
        void shouldReturnOriginalStringWhenAllCharsMatch() {
            assertEquals("hello", CharSetUtils.keep("hello", "a-z"), "Matching with a character range");
            assertEquals("hello", CharSetUtils.keep("hello", "oleh"), "Matching with a scrambled set of characters");
        }

        @Test
        @DisplayName("should return a string with only the characters that match the set")
        void shouldReturnOnlyMatchingCharacters() {
            assertEquals("ell", CharSetUtils.keep("hello", "el"));
        }
    }
}
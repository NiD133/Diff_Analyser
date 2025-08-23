package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharSetUtils#delete(String, String...)}.
 */
@DisplayName("CharSetUtils.delete()")
class CharSetUtilsDeleteTest {

    @Nested
    @DisplayName("when input string is null or empty")
    class NullAndEmptyInputString {

        @Test
        void shouldReturnNullWhenInputStringIsNull() {
            assertNull(CharSetUtils.delete(null, "a"));
            assertNull(CharSetUtils.delete(null, (String) null));
            assertNull(CharSetUtils.delete(null, (String[]) null));
            assertNull(CharSetUtils.delete(null)); // varargs
        }

        @Test
        void shouldReturnEmptyStringWhenInputStringIsEmpty() {
            assertEquals("", CharSetUtils.delete("", "a"));
            assertEquals("", CharSetUtils.delete("", (String) null));
            assertEquals("", CharSetUtils.delete("", (String[]) null));
            assertEquals("", CharSetUtils.delete("")); // varargs
        }
    }

    @Nested
    @DisplayName("when character set is null or empty")
    class NullAndEmptySet {

        @Test
        void shouldReturnOriginalStringWhenSetIsNull() {
            assertEquals("hello", CharSetUtils.delete("hello", (String) null));
            assertEquals("hello", CharSetUtils.delete("hello", (String[]) null));
        }

        @Test
        void shouldReturnOriginalStringWhenSetIsEmpty() {
            assertEquals("hello", CharSetUtils.delete("hello")); // empty varargs
            assertEquals("hello", CharSetUtils.delete("hello", ""));
        }
    }

    @Nested
    @DisplayName("when performing deletion")
    class DeletionLogic {

        @Test
        void shouldReturnOriginalStringWhenNoCharsMatch() {
            assertEquals("hello", CharSetUtils.delete("hello", "xyz"));
        }

        @Test
        void shouldDeleteMatchingCharacters() {
            assertEquals("heo", CharSetUtils.delete("hello", "l"));
            assertEquals("ho", CharSetUtils.delete("hello", "el"));
        }

        @Test
        void shouldReturnEmptyStringWhenAllCharsMatch() {
            assertEquals("", CharSetUtils.delete("hello", "elho"));
        }

        @Test
        void shouldDeleteCharactersUsingRangeSyntax() {
            assertEquals("", CharSetUtils.delete("hello", "a-z"));
        }

        @Test
        void shouldDeleteSpecialCharacters() {
            assertEquals("", CharSetUtils.delete("----", "-"));
        }
    }
}
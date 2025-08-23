package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the delete(String, String...) method in {@link CharSetUtils}.
 */
@DisplayName("CharSetUtils.delete()")
class CharSetUtilsDeleteTest {

    @Test
    @DisplayName("should return null when the input string is null")
    void delete_shouldReturnNull_whenInputIsNull() {
        assertNull(CharSetUtils.delete(null, "a-z"), "A null input string should result in null output.");
        assertNull(CharSetUtils.delete(null, (String) null), "A null input string should result in null output, regardless of the set.");
    }

    @Test
    @DisplayName("should return an empty string when the input string is empty")
    void delete_shouldReturnEmptyString_whenInputIsEmpty() {
        assertEquals("", CharSetUtils.delete("", "a-z"), "An empty input string should result in an empty string output.");
        assertEquals("", CharSetUtils.delete("", (String) null), "An empty input string should result in an empty string output, regardless of the set.");
    }

    @Test
    @DisplayName("should return the original string when the character set is null or empty")
    void delete_shouldReturnOriginalString_whenSetIsNullOrEmpty() {
        // A null set means no characters should be deleted.
        assertEquals("hello", CharSetUtils.delete("hello", (String) null));

        // An empty set means no characters should be deleted.
        assertEquals("hello", CharSetUtils.delete("hello", ""));
    }

    @Test
    @DisplayName("should return the original string when no characters match the set")
    void delete_shouldReturnOriginalString_whenNoCharactersMatch() {
        // The character 'z' is not in "hello", so the string should be unchanged.
        assertEquals("hello", CharSetUtils.delete("hello", "z"));
    }

    @Test
    @DisplayName("should delete all characters present in the character set")
    void delete_shouldRemoveMatchingCharacters() {
        // Deletes 'e' from "hello" because it falls within the 'a-e' range.
        assertEquals("hllo", CharSetUtils.delete("hello", "a-e"));

        // Deletes 'l', 'l', and 'o' from "hello" as they are in the 'l-p' range.
        assertEquals("he", CharSetUtils.delete("hello", "l-p"));
    }
}
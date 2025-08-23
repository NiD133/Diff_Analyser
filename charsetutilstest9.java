package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharSetUtils#keep(String, String...)}.
 */
@DisplayName("Tests for CharSetUtils.keep(String, String...)")
class CharSetUtilsKeepTest extends AbstractLangTest {

    @Test
    @DisplayName("should return null when the input string is null")
    void testKeepWithNullString() {
        assertNull(CharSetUtils.keep(null, "a-e"), "Set with a range");
        assertNull(CharSetUtils.keep(null, (String[]) null), "Null set");
        assertNull(CharSetUtils.keep(null), "Empty set");
    }

    @Test
    @DisplayName("should return an empty string when the input string is empty")
    void testKeepWithEmptyString() {
        assertEquals("", CharSetUtils.keep("", "a-e"), "Set with a range");
        assertEquals("", CharSetUtils.keep("", (String[]) null), "Null set");
        assertEquals("", CharSetUtils.keep(""), "Empty set");
    }

    @Test
    @DisplayName("should return an empty string when the character set is null or empty")
    void testKeepWithNullOrEmptySet() {
        assertEquals("", CharSetUtils.keep("hello", (String[]) null), "For a null set");
        assertEquals("", CharSetUtils.keep("hello"), "For an empty set");
        assertEquals("", CharSetUtils.keep("hello", (String) null), "For a set containing null");
    }

    @Test
    @DisplayName("should keep characters that match a character range")
    void testKeepWithCharacterRange() {
        assertEquals("e", CharSetUtils.keep("hello", "a-e"));
    }

    @Test
    @DisplayName("should keep characters specified in a simple set")
    void testKeepWithSimpleSet() {
        assertEquals("ell", CharSetUtils.keep("hello", "el"));
        assertEquals("ll", CharSetUtils.keep("hello", "l"));
    }

    @Test
    @DisplayName("should return the original string if all its characters are in the set")
    void testKeepWhenAllCharactersMatch() {
        assertEquals("hello", CharSetUtils.keep("hello", "elho"), "Set contains all characters");
        assertEquals("hello", CharSetUtils.keep("hello", "a-z"), "Set is a superset range");
    }

    @Test
    @DisplayName("should correctly handle special characters like hyphen in the set")
    void testKeepWithSpecialCharacters() {
        assertEquals("----", CharSetUtils.keep("----", "-"));
    }
}
package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharSetUtils#count(String, String...)}.
 */
class CharSetUtilsTest extends AbstractLangTest {

    private static final String HELLO = "hello";

    @Test
    void countWithNullOrEmptyStringShouldReturnZero() {
        // A null or empty input string should always result in a count of 0.
        assertEquals(0, CharSetUtils.count(null, "a-z"));
        assertEquals(0, CharSetUtils.count(null, (String[]) null));
        assertEquals(0, CharSetUtils.count("", "a-z"));
        assertEquals(0, CharSetUtils.count("", (String[]) null));
    }

    @Test
    void countWithNullOrEmptySetShouldReturnZero() {
        // A null or empty set of characters should always result in a count of 0.
        assertEquals(0, CharSetUtils.count(HELLO, (String[]) null));
        assertEquals(0, CharSetUtils.count(HELLO)); // Varargs equivalent of a null set
        assertEquals(0, CharSetUtils.count(HELLO, (String) null)); // Set containing a single null string
        assertEquals(0, CharSetUtils.count(HELLO, "")); // Set defined by an empty string
    }

    @Test
    void countShouldCorrectlyCountCharsFromSimpleSet() {
        // Counts 'e', 'l', 'l' from the set "el"
        assertEquals(3, CharSetUtils.count(HELLO, "el"));
    }

    @Test
    void countShouldCorrectlyCountCharsFromRangeSet() {
        // Counts 'e' from the range 'a-e'
        assertEquals(1, CharSetUtils.count(HELLO, "a-e"));
        // Counts 'e' and 'h' from the range 'e-i'
        assertEquals(2, CharSetUtils.count(HELLO, "e-i"));
    }

    @Test
    void countWithFullRangeSetShouldCountAllChars() {
        // All characters in "hello" are in the range 'a-z'
        assertEquals(5, CharSetUtils.count(HELLO, "a-z"));
    }

    @Test
    void countWithNonMatchingSetShouldReturnZero() {
        // No characters from "hello" are in the set "x"
        assertEquals(0, CharSetUtils.count(HELLO, "x"));
    }
}
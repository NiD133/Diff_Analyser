package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for the {@link IOCase} enum.
 * This version focuses on improving clarity and structure.
 */
@DisplayName("IOCase Test")
class IOCaseTest {

    /**
     * Groups tests for the {@code checkEndsWith} method using {@code IOCase.SENSITIVE}.
     */
    @Nested
    @DisplayName("checkEndsWith with IOCase.SENSITIVE")
    class CheckEndsWithSensitiveTest {

        @ParameterizedTest(name = "''{0}'' ends with ''{1}'' should be {2}")
        @CsvSource({
            // str,    end,   expected
            "ABC,     '',    true",   // An empty suffix is always considered to be present.
            "ABC,     'C',   true",
            "ABC,     'BC',  true",
            "ABC,     'ABC', true",   // The string is a valid suffix of itself.
            "ABC,     'A',   false",
            "ABC,     'AB',  false",
            "ABC,     'c',   false",  // Fails due to case sensitivity.
            "ABC,     'bC',  false",  // Fails due to case sensitivity.
            "ABC,     'ABCD',false",  // Suffix cannot be longer than the string.
            "'',      'ABC', false",  // An empty string cannot end with a non-empty suffix.
            "'',      '',    true"    // An empty string ends with an empty suffix.
        })
        void whenCheckingVariousSuffixes_thenReturnsExpectedResult(final String str, final String end, final boolean expected) {
            assertEquals(expected, IOCase.SENSITIVE.checkEndsWith(str, end));
        }

        @Test
        @DisplayName("should return false when the string to check is null")
        void whenStringIsNull_thenReturnsFalse() {
            assertFalse(IOCase.SENSITIVE.checkEndsWith(null, "A"));
        }

        @Test
        @DisplayName("should return false when the suffix is null")
        void whenSuffixIsNull_thenReturnsFalse() {
            assertFalse(IOCase.SENSITIVE.checkEndsWith("ABC", null));
        }

        @Test
        @DisplayName("should return false when both strings are null")
        void whenBothStringAndSuffixAreNull_thenReturnsFalse() {
            assertFalse(IOCase.SENSITIVE.checkEndsWith(null, null));
        }
    }
}
package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for {@link IOCase#checkIndexOf(String, int, String)} using the {@link IOCase#SENSITIVE} constant.
 */
@DisplayName("IOCase.SENSITIVE.checkIndexOf()")
class IOCaseCheckIndexOfSensitiveTest {

    private static final IOCase SENSITIVE_CASE = IOCase.SENSITIVE;
    private static final String TEXT = "ABCDEFGHIJ";

    @Nested
    @DisplayName("when substring is found")
    class SubstringFound {

        @ParameterizedTest(name = "finds \"{2}\" in \"{0}\" at index {3} when starting from {1}")
        @CsvSource({
            // Substring at start
            "ABCDEFGHIJ, 0, A,   0",
            "ABCDEFGHIJ, 0, AB,  0",
            "ABCDEFGHIJ, 0, ABC, 0",
            // Substring in middle
            "ABCDEFGHIJ, 0, D,   3",
            "ABCDEFGHIJ, 3, D,   3",
            "ABCDEFGHIJ, 0, DEF, 3",
            "ABCDEFGHIJ, 3, DEF, 3",
            // Substring at end
            "ABCDEFGHIJ, 0, J,   9",
            "ABCDEFGHIJ, 9, J,   9",
            "ABCDEFGHIJ, 8, IJ,  8",
            "ABCDEFGHIJ, 7, HIJ, 7"
        })
        void shouldReturnCorrectIndex(final String text, final int startIndex, final String search, final int expectedIndex) {
            assertEquals(expectedIndex, SENSITIVE_CASE.checkIndexOf(text, startIndex, search));
        }
    }

    @Nested
    @DisplayName("when substring is not found or inputs are invalid")
    class SubstringNotFound {

        @ParameterizedTest(name = "returns -1 when searching for \"{2}\" in \"{0}\" from index {1}")
        @CsvSource({
            // Start index is past the potential match
            "ABCDEFGHIJ, 1, A",
            "ABCDEFGHIJ, 4, D",
            "ABCDEFGHIJ, 9, IJ",
            "ABCDEFGHIJ, 8, HIJ",
            // Substring does not exist in the text
            "ABCDEFGHIJ, 0, DED",
            // Search string is longer than the text
            "DEF, 0, ABCDEFGHIJ"
        })
        void shouldReturnNegativeOne(final String text, final int startIndex, final String search) {
            assertEquals(-1, SENSITIVE_CASE.checkIndexOf(text, startIndex, search));
        }

        @Test
        @DisplayName("returns -1 for null inputs")
        void shouldReturnNegativeOneForNullInputs() {
            assertEquals(-1, SENSITIVE_CASE.checkIndexOf(TEXT, 0, null));
            assertEquals(-1, SENSITIVE_CASE.checkIndexOf(null, 0, "ABC"));
            assertEquals(-1, SENSITIVE_CASE.checkIndexOf(null, 0, null));
        }
    }
}
package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for {@link IOCase#checkRegionMatches(String, int, String)}.
 */
@DisplayName("IOCase.checkRegionMatches")
class IOCaseTest {

    @Nested
    @DisplayName("for SENSITIVE case")
    class Sensitive {

        @ParameterizedTest(name = "should return {3} for str=''{0}'', startIndex={1}, search=''{2}''")
        @CsvSource({
            // Successful matches
            "ABC, 0, ''   , true",
            "ABC, 0, A    , true",
            "ABC, 0, AB   , true",
            "ABC, 0, ABC  , true",
            "ABC, 1, ''   , true",
            "ABC, 1, BC   , true",
            "'',  0, ''   , true",

            // Unsuccessful matches (wrong substring)
            "ABC, 0, BC   , false",
            "ABC, 0, C    , false",
            "ABC, 1, A    , false",
            "ABC, 1, AB   , false",
            "ABC, 1, C    , false",

            // Unsuccessful matches (search string too long for region)
            "ABC, 0, ABCD , false",
            "ABC, 1, ABC  , false",
            "ABC, 1, ABCD , false",
            "'',  0, ABC  , false",

            // Unsuccessful matches (invalid start index)
            "'',  1, ''   , false",
            "'',  1, ABC  , false"
        })
        void checkRegionMatches(final String str, final int strStartIndex, final String search, final boolean expected) {
            assertEquals(expected, IOCase.SENSITIVE.checkRegionMatches(str, strStartIndex, search));
        }

        @Test
        @DisplayName("should return false for null inputs")
        void returnsFalseForNullInputs() {
            assertFalse(IOCase.SENSITIVE.checkRegionMatches("ABC", 0, null), "search string is null");
            assertFalse(IOCase.SENSITIVE.checkRegionMatches(null, 0, "ABC"), "source string is null");
            assertFalse(IOCase.SENSITIVE.checkRegionMatches(null, 0, null), "both strings are null");
        }
    }

    // Future tests for INSENSITIVE and SYSTEM cases could be added in their own @Nested classes here.
}
package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Tests for {@link IOCase#checkIndexOf(String, int, String)}.
 */
@DisplayName("Tests for IOCase.checkIndexOf(String, int, String)")
class IOCaseCheckIndexOfTest {

    private static final String TEXT = "Apache Commons IO";
    private static final String SEARCH_STR_MATCHING_CASE = "Commons";
    private static final String SEARCH_STR_DIFFERENT_CASE = "commons";
    private static final int EXPECTED_INDEX = 7;

    @Nested
    @DisplayName("using IOCase.SENSITIVE")
    class Sensitive {

        @Test
        @DisplayName("should return correct index when search string is found with matching case")
        void findsMatchingCase() {
            assertEquals(EXPECTED_INDEX, IOCase.SENSITIVE.checkIndexOf(TEXT, 0, SEARCH_STR_MATCHING_CASE));
        }

        @Test
        @DisplayName("should return -1 when search string has a different case")
        void doesNotFindDifferentCase() {
            assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(TEXT, 0, SEARCH_STR_DIFFERENT_CASE));
        }
    }

    @Nested
    @DisplayName("using IOCase.INSENSITIVE")
    class Insensitive {

        @Test
        @DisplayName("should return correct index when search string is found with matching case")
        void findsMatchingCase() {
            assertEquals(EXPECTED_INDEX, IOCase.INSENSITIVE.checkIndexOf(TEXT, 0, SEARCH_STR_MATCHING_CASE));
        }

        @Test
        @DisplayName("should return correct index when search string has a different case")
        void findsDifferentCase() {
            assertEquals(EXPECTED_INDEX, IOCase.INSENSITIVE.checkIndexOf(TEXT, 0, SEARCH_STR_DIFFERENT_CASE));
        }
    }

    @Nested
    @DisplayName("using IOCase.SYSTEM")
    class System {

        @Test
        @DisplayName("should return correct index when search string is found with matching case")
        void findsMatchingCase() {
            assertEquals(EXPECTED_INDEX, IOCase.SYSTEM.checkIndexOf(TEXT, 0, SEARCH_STR_MATCHING_CASE));
        }

        @Test
        @DisplayName("on Windows, should find search string with different case")
        @EnabledOnOs(OS.WINDOWS)
        void findsDifferentCaseOnWindows() {
            assertEquals(EXPECTED_INDEX, IOCase.SYSTEM.checkIndexOf(TEXT, 0, SEARCH_STR_DIFFERENT_CASE));
        }

        @Test
        @DisplayName("on non-Windows, should not find search string with different case")
        @DisabledOnOs(OS.WINDOWS)
        void doesNotFindDifferentCaseOnNonWindows() {
            assertEquals(-1, IOCase.SYSTEM.checkIndexOf(TEXT, 0, SEARCH_STR_DIFFERENT_CASE));
        }
    }

    @ParameterizedTest
    @EnumSource(IOCase.class)
    @DisplayName("should return -1 for any null input")
    void checkIndexOfWithNullsReturnsMinusOne(final IOCase ioCase) {
        assertAll(
            () -> assertEquals(-1, ioCase.checkIndexOf(null, 0, "any"), "Text to search in is null"),
            () -> assertEquals(-1, ioCase.checkIndexOf("any", 0, null), "Search string is null"),
            () -> assertEquals(-1, ioCase.checkIndexOf(null, 0, null), "Both inputs are null")
        );
    }
}
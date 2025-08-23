package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link IOCase} enum.
 */
@DisplayName("IOCase")
class IOCaseTest {

    @Nested
    @DisplayName("checkCompareTo(String, String) method")
    class CheckCompareToTest {

        @Nested
        @DisplayName("with IOCase.SENSITIVE")
        class Sensitive {

            @Test
            @DisplayName("should return 0 for equal strings")
            void returnsZeroForEqualStrings() {
                assertEquals(0, IOCase.SENSITIVE.checkCompareTo("ABC", "ABC"), "Comparison of identical strings should be 0.");
                assertEquals(0, IOCase.SENSITIVE.checkCompareTo("", ""), "Comparison of two empty strings should be 0.");
            }

            @Test
            @DisplayName("should return a negative value when the first string is lexicographically smaller")
            void returnsNegativeWhenFirstStringIsSmaller() {
                assertTrue(IOCase.SENSITIVE.checkCompareTo("ABC", "DEF") < 0, "'ABC' should be less than 'DEF'.");
            }

            @Test
            @DisplayName("should return a positive value when the first string is lexicographically larger")
            void returnsPositiveWhenFirstStringIsLarger() {
                assertTrue(IOCase.SENSITIVE.checkCompareTo("DEF", "ABC") > 0, "'DEF' should be greater than 'ABC'.");
            }

            @Test
            @DisplayName("should correctly compare non-empty strings with empty strings")
            void comparesWithEmptyStrings() {
                assertTrue(IOCase.SENSITIVE.checkCompareTo("ABC", "") > 0, "Any non-empty string should be greater than an empty string.");
                assertTrue(IOCase.SENSITIVE.checkCompareTo("", "ABC") < 0, "An empty string should be less than any non-empty string.");
            }

            @Test
            @DisplayName("should throw NullPointerException when any argument is null")
            void throwsNpeForNullInput() {
                assertThrows(NullPointerException.class, () -> IOCase.SENSITIVE.checkCompareTo("ABC", null));
                assertThrows(NullPointerException.class, () -> IOCase.SENSITIVE.checkCompareTo(null, "ABC"));
                assertThrows(NullPointerException.class, () -> IOCase.SENSITIVE.checkCompareTo(null, null));
            }
        }

        // Additional nested classes for IOCase.INSENSITIVE and IOCase.SYSTEM
        // could be added here to test their specific behaviors.
    }
}
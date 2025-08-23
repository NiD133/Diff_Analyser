package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link IOCase#checkEquals(String, String)} method.
 */
@DisplayName("IOCase.checkEquals()")
class IOCaseCheckEqualsTest {

    @Nested
    @DisplayName("when case-sensitive")
    class CaseSensitive {

        private final IOCase sensitive = IOCase.SENSITIVE;

        @Test
        @DisplayName("should return true for identical strings")
        void returnsTrueForIdenticalStrings() {
            assertTrue(sensitive.checkEquals("ABC", "ABC"), "Exact match should return true");
        }

        @Test
        @DisplayName("should return false for strings differing only in case")
        void returnsFalseForDifferentCase() {
            assertFalse(sensitive.checkEquals("ABC", "abc"), "Different case should return false");
        }

        @Test
        @DisplayName("should return false for non-equal strings of varying lengths")
        void returnsFalseForNonEqualStrings() {
            assertFalse(sensitive.checkEquals("ABC", "A"), "Shorter string should not match");
            assertFalse(sensitive.checkEquals("ABC", "AB"), "Prefix should not match");
            assertFalse(sensitive.checkEquals("ABC", "BC"), "Contained substring should not be an exact match");
            assertFalse(sensitive.checkEquals("ABC", "ABCD"), "Longer string should not match");
        }

        @Test
        @DisplayName("should handle empty strings correctly")
        void handlesEmptyStrings() {
            assertTrue(sensitive.checkEquals("", ""), "Two empty strings should be equal");
            assertFalse(sensitive.checkEquals("ABC", ""), "A non-empty and an empty string should not be equal");
            assertFalse(sensitive.checkEquals("", "ABC"), "An empty and a non-empty string should not be equal");
        }

        @Test
        @DisplayName("should handle null inputs correctly")
        void handlesNulls() {
            assertTrue(sensitive.checkEquals(null, null), "Two nulls should be considered equal");
            assertFalse(sensitive.checkEquals("ABC", null), "A non-null and a null string should not be equal");
            assertFalse(sensitive.checkEquals(null, "ABC"), "A null and a non-null string should not be equal");
        }
    }

    // Additional @Nested classes could be added here for IOCase.INSENSITIVE and IOCase.SYSTEM
    // to test their specific behaviors in a similarly structured way.
}
package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests the contains(char) method of the CharRange class.
 */
@DisplayName("CharRange.contains(char)")
class CharRangeContainsTest {

    @Nested
    @DisplayName("for a single-character range like is('c')")
    class SingleCharRangeTest {

        @Test
        void shouldContainOnlyTheSpecifiedCharacter() {
            final CharRange range = CharRange.is('c');

            assertTrue(range.contains('c'), "The range should contain its defining character.");
            assertFalse(range.contains('b'), "The range should not contain a character before it.");
            assertFalse(range.contains('d'), "The range should not contain a character after it.");
        }
    }

    @Nested
    @DisplayName("for a standard range like isIn('c', 'd')")
    class StandardRangeTest {

        @Test
        void shouldContainCharactersWithinAndOnTheEndpoints() {
            final CharRange range = CharRange.isIn('c', 'd');

            assertFalse(range.contains('b'), "Should not contain a character before the start.");
            assertTrue(range.contains('c'), "Should contain the start character.");
            assertTrue(range.contains('d'), "Should contain the end character.");
            assertFalse(range.contains('e'), "Should not contain a character after the end.");
        }

        @Test
        void shouldBehaveIdenticallyWhenEndpointsAreReversed() {
            // The constructor normalizes the range, so isIn('d', 'c') is the same as isIn('c', 'd').
            final CharRange range = CharRange.isIn('d', 'c');

            assertFalse(range.contains('b'), "Should not contain a character before the start.");
            assertTrue(range.contains('c'), "Should contain the start character (normalized).");
            assertTrue(range.contains('d'), "Should contain the end character (normalized).");
            assertFalse(range.contains('e'), "Should not contain a character after the end.");
        }
    }

    @Nested
    @DisplayName("for a negated range like isNotIn('c', 'd')")
    class NegatedRangeTest {

        @Test
        void shouldNotContainCharactersWithinTheNegatedEndpoints() {
            final CharRange range = CharRange.isNotIn('c', 'd');

            assertFalse(range.contains('c'), "Should not contain the start of the negated range.");
            assertFalse(range.contains('d'), "Should not contain the end of the negated range.");
        }

        @Test
        void shouldContainCharactersOutsideTheNegatedEndpoints() {
            final CharRange range = CharRange.isNotIn('c', 'd');

            assertTrue(range.contains('b'), "Should contain a character before the negated range.");
            assertTrue(range.contains('e'), "Should contain a character after the negated range.");
        }

        @Test
        void shouldContainBoundaryCharactersLikeMIN_VALUEAndMAX_VALUE() {
            final CharRange range = CharRange.isNotIn('c', 'd');

            assertTrue(range.contains((char) 0), "Should contain the minimum character value.");
            assertTrue(range.contains(Character.MAX_VALUE), "Should contain the maximum character value.");
        }
    }
}
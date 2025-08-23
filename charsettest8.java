package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for CharSet.contains(char)")
class CharSetContainsTest {

    @Nested
    @DisplayName("when set is defined by a character range")
    class WhenSetIsRange {

        @Test
        @DisplayName("should return true for characters within the range 'b-d'")
        void contains_withStandardRange_shouldReturnTrueForCharsInRange() {
            // Arrange
            final CharSet rangeSet = CharSet.getInstance("b-d");

            // Act & Assert
            assertFalse(rangeSet.contains('a'), "Should not contain 'a' (before range)");
            assertTrue(rangeSet.contains('b'), "Should contain 'b' (start of range)");
            assertTrue(rangeSet.contains('c'), "Should contain 'c' (middle of range)");
            assertTrue(rangeSet.contains('d'), "Should contain 'd' (end of range)");
            assertFalse(rangeSet.contains('e'), "Should not contain 'e' (after range)");
        }

        @Test
        @DisplayName("should handle reversed ranges like 'd-b' as if they were 'b-d'")
        void contains_withReversedRange_shouldBehaveLikeNormalRange() {
            // Arrange: The Javadoc states that reversed ranges are normalized.
            final CharSet reversedRangeSet = CharSet.getInstance("d-b");

            // Act & Assert
            assertFalse(reversedRangeSet.contains('a'), "Should not contain 'a' (before range)");
            assertTrue(reversedRangeSet.contains('b'), "Should contain 'b' (normalized start of range)");
            assertTrue(reversedRangeSet.contains('c'), "Should contain 'c' (middle of range)");
            assertTrue(reversedRangeSet.contains('d'), "Should contain 'd' (normalized end of range)");
            assertFalse(reversedRangeSet.contains('e'), "Should not contain 'e' (after range)");
        }

        @Test
        @DisplayName("should normalize the internal representation of a reversed range")
        void constructor_withReversedRange_shouldNormalizeInternalState() {
            // Arrange
            final CharSet reversedRangeSet = CharSet.getInstance("d-b");

            // Act & Assert
            // The internal representation should be normalized to the standard "b-d" form.
            assertEquals("[b-d]", reversedRangeSet.toString(), "toString() should show the normalized range");
            assertEquals(1, reversedRangeSet.getCharRanges().length, "Reversed range should be stored as a single CharRange");
        }
    }

    @Nested
    @DisplayName("when set is defined by individual characters")
    class WhenSetIsListOfChars {

        @Test
        @DisplayName("should return true only for characters present in the list 'bcd'")
        void contains_withContiguousCharsList_shouldReturnTrueForListedChars() {
            // Arrange
            final CharSet listSet = CharSet.getInstance("bcd");

            // Act & Assert
            assertFalse(listSet.contains('a'));
            assertTrue(listSet.contains('b'));
            assertTrue(listSet.contains('c'));
            assertTrue(listSet.contains('d'));
            assertFalse(listSet.contains('e'));
        }

        @Test
        @DisplayName("should return true only for characters present in the non-contiguous list 'bd'")
        void contains_withNonContiguousCharsList_shouldReturnTrueOnlyForSpecifiedChars() {
            // Arrange
            final CharSet listSet = CharSet.getInstance("bd");

            // Act & Assert
            assertFalse(listSet.contains('a'));
            assertTrue(listSet.contains('b'));
            assertFalse(listSet.contains('c'), "Should not contain 'c' as it's not in the list");
            assertTrue(listSet.contains('d'));
            assertFalse(listSet.contains('e'));
        }
    }

    @Test
    @DisplayName("when set is a negated range like '^b-d', should return true only for characters outside the range")
    void contains_withNegatedRange_shouldReturnTrueForCharsOutsideRange() {
        // Arrange
        final CharSet negatedRangeSet = CharSet.getInstance("^b-d");

        // Act & Assert
        assertTrue(negatedRangeSet.contains('a'), "Should contain 'a' (outside negated range)");
        assertFalse(negatedRangeSet.contains('b'), "Should not contain 'b' (start of negated range)");
        assertFalse(negatedRangeSet.contains('c'), "Should not contain 'c' (middle of negated range)");
        assertFalse(negatedRangeSet.contains('d'), "Should not contain 'd' (end of negated range)");
        assertTrue(negatedRangeSet.contains('e'), "Should contain 'e' (outside negated range)");
    }
}
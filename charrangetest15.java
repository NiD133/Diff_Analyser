package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the iterator of {@link CharRange}.
 * This class focuses on verifying the correctness of the iterator's behavior
 * for various types of character ranges (positive, negated, empty, and edge cases).
 */
@DisplayName("CharRange.iterator()")
class CharRangeIteratorTest extends AbstractLangTest {

    @Test
    @DisplayName("should iterate over a single character for a single-char range")
    void testSingleCharRange() {
        // Arrange
        final CharRange range = CharRange.is('a');
        final Iterator<Character> it = range.iterator();

        // Act & Assert
        assertNotNull(it);
        assertTrue(it.hasNext(), "Iterator should have an element");
        assertEquals('a', it.next(), "Iterator should return the correct character");
        assertFalse(it.hasNext(), "Iterator should be exhausted");
        assertThrows(NoSuchElementException.class, it::next, "next() should throw when exhausted");
    }

    @Test
    @DisplayName("should iterate over all characters for a multi-char range")
    void testMultiCharRange() {
        // Arrange
        final CharRange range = CharRange.isIn('a', 'd');
        final Iterator<Character> it = range.iterator();

        // Act & Assert
        assertNotNull(it);
        assertTrue(it.hasNext());
        assertEquals('a', it.next());
        assertTrue(it.hasNext());
        assertEquals('b', it.next());
        assertTrue(it.hasNext());
        assertEquals('c', it.next());
        assertTrue(it.hasNext());
        assertEquals('d', it.next());
        assertFalse(it.hasNext(), "Iterator should be exhausted");
        assertThrows(NoSuchElementException.class, it::next, "next() should throw when exhausted");
    }

    @Test
    @DisplayName("should be empty for a range that is negated over all possible characters")
    void testEmptyRange() {
        // Arrange
        final CharRange emptyRange = CharRange.isNotIn((char) 0, Character.MAX_VALUE);
        final Iterator<Character> it = emptyRange.iterator();

        // Act & Assert
        assertNotNull(it);
        assertFalse(it.hasNext(), "Iterator for an empty range should be empty");
        assertThrows(NoSuchElementException.class, it::next, "next() on an empty iterator should throw");
    }

    @Test
    @DisplayName("should correctly skip the excluded character in a negated range")
    void testNegatedRangeSkipsCharacter() {
        // Arrange
        final char charToSkip = 'k';
        final CharRange range = CharRange.isNot(charToSkip);
        final Iterator<Character> it = range.iterator();

        // Act & Assert
        // Iterate up to the character before the one to be skipped
        for (char c = 0; c < charToSkip; c++) {
            assertTrue(it.hasNext(), "Iterator should have more elements before the gap");
            assertEquals(c, it.next(), "Iterator should produce characters in sequence before the gap");
        }

        // The next character should be the one after the skipped character
        assertTrue(it.hasNext(), "Iterator should have elements after the gap");
        assertEquals((char) (charToSkip + 1), it.next(), "Iterator should skip the negated character");
    }

    @Nested
    @DisplayName("for negated ranges at character set boundaries")
    class NegatedRangeBoundaryTest {

        @Test
        @DisplayName("should yield only char 0 when negating [1, MAX_VALUE]")
        void testNegatedRangeYieldsFirstChar() {
            // Arrange
            // This range contains everything EXCEPT characters from 1 to MAX_VALUE, leaving only char 0.
            final CharRange range = CharRange.isNotIn((char) 1, Character.MAX_VALUE);
            final Iterator<Character> it = range.iterator();

            // Act & Assert
            assertNotNull(it);
            assertTrue(it.hasNext(), "Iterator should have one element");
            assertEquals((char) 0, it.next(), "Iterator should return the first character of the set");
            assertFalse(it.hasNext(), "Iterator should be exhausted");
            assertThrows(NoSuchElementException.class, it::next, "next() should throw when exhausted");
        }

        @Test
        @DisplayName("should yield only MAX_VALUE when negating [0, MAX_VALUE - 1]")
        void testNegatedRangeYieldsLastChar() {
            // Arrange
            // This range contains everything EXCEPT chars from 0 to MAX_VALUE - 1, leaving only MAX_VALUE.
            final CharRange range = CharRange.isNotIn((char) 0, (char) (Character.MAX_VALUE - 1));
            final Iterator<Character> it = range.iterator();

            // Act & Assert
            assertNotNull(it);
            assertTrue(it.hasNext(), "Iterator should have one element");
            assertEquals(Character.MAX_VALUE, it.next(), "Iterator should return the last character of the set");
            assertFalse(it.hasNext(), "Iterator should be exhausted");
            assertThrows(NoSuchElementException.class, it::next, "next() should throw when exhausted");
        }
    }
}
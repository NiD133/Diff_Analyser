package org.apache.commons.lang3;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharRange}.
 */
public class CharRangeTest {

    //-----------------------------------------------------------------------
    // Factory Methods and Constructor Tests
    //-----------------------------------------------------------------------

    @Test
    public void factory_is_shouldCreateSingletonRange() {
        // Arrange & Act
        CharRange range = CharRange.is('a');

        // Assert
        assertEquals('a', range.getStart());
        assertEquals('a', range.getEnd());
        assertFalse(range.isNegated());
    }

    @Test
    public void factory_isIn_shouldCreateInclusiveRange() {
        // Arrange & Act
        CharRange range = CharRange.isIn('a', 'c');

        // Assert
        assertEquals('a', range.getStart());
        assertEquals('c', range.getEnd());
        assertFalse(range.isNegated());
    }

    @Test
    public void factory_isIn_shouldReorderEndpoints() {
        // Arrange & Act
        CharRange range = CharRange.isIn('c', 'a');

        // Assert
        assertEquals('a', range.getStart());
        assertEquals('c', range.getEnd());
        assertFalse(range.isNegated());
    }

    @Test
    public void factory_isNot_shouldCreateNegatedSingletonRange() {
        // Arrange & Act
        CharRange range = CharRange.isNot('a');

        // Assert
        assertEquals('a', range.getStart());
        assertEquals('a', range.getEnd());
        assertTrue(range.isNegated());
    }

    @Test
    public void factory_isNotIn_shouldCreateNegatedRange() {
        // Arrange & Act
        CharRange range = CharRange.isNotIn('a', 'c');

        // Assert
        assertEquals('a', range.getStart());
        assertEquals('c', range.getEnd());
        assertTrue(range.isNegated());
    }

    //-----------------------------------------------------------------------
    // equals() and hashCode()
    //-----------------------------------------------------------------------

    @Test
    public void equals_shouldReturnTrue_forIdenticalRanges() {
        // Arrange
        CharRange range1 = CharRange.isIn('a', 'c');
        CharRange range2 = CharRange.isIn('a', 'c');
        CharRange range3 = CharRange.isNotIn('a', 'c');
        CharRange range4 = CharRange.isNotIn('a', 'c');

        // Assert
        assertEquals(range1, range2);
        assertEquals(range3, range4);
    }

    @Test
    public void equals_shouldReturnFalse_whenRangesHaveDifferentEndpoints() {
        // Arrange
        CharRange range1 = CharRange.isIn('a', 'c');
        CharRange range2 = CharRange.isIn('a', 'd');
        CharRange range3 = CharRange.isIn('b', 'c');

        // Assert
        assertNotEquals(range1, range2);
        assertNotEquals(range1, range3);
    }

    @Test
    public void equals_shouldReturnFalse_whenOneRangeIsNegatedAndOtherIsNot() {
        // Arrange
        CharRange inclusiveRange = CharRange.isIn('a', 'c');
        CharRange negatedRange = CharRange.isNotIn('a', 'c');

        // Assert
        assertNotEquals(inclusiveRange, negatedRange);
    }

    @Test
    public void equals_shouldReturnFalse_forNullOrDifferentType() {
        // Arrange
        CharRange range = CharRange.is('a');

        // Assert
        assertNotEquals(range, null);
        assertNotEquals(range, new Object());
    }

    @Test
    public void hashCode_shouldBeSame_forEqualObjects() {
        // Arrange
        CharRange range1 = CharRange.isIn('a', 'c');
        CharRange range2 = CharRange.isIn('c', 'a'); // Constructor reorders
        CharRange range3 = CharRange.isNotIn('a', 'c');
        CharRange range4 = CharRange.isNotIn('c', 'a');

        // Assert
        assertEquals(range1.hashCode(), range2.hashCode());
        assertEquals(range3.hashCode(), range4.hashCode());
        assertNotEquals(range1.hashCode(), range3.hashCode());
    }

    //-----------------------------------------------------------------------
    // contains(char)
    //-----------------------------------------------------------------------

    @Test
    public void containsChar_shouldReturnTrue_forCharInRange() {
        // Arrange
        CharRange range = CharRange.isIn('b', 'd');

        // Assert
        assertTrue(range.contains('b'));
        assertTrue(range.contains('c'));
        assertTrue(range.contains('d'));
    }

    @Test
    public void containsChar_shouldReturnFalse_forCharOutsideRange() {
        // Arrange
        CharRange range = CharRange.isIn('b', 'd');

        // Assert
        assertFalse(range.contains('a'));
        assertFalse(range.contains('e'));
    }

    @Test
    public void containsChar_shouldReturnFalse_forCharInNegatedRange() {
        // Arrange
        CharRange range = CharRange.isNotIn('b', 'd');

        // Assert
        assertFalse(range.contains('b'));
        assertFalse(range.contains('c'));
        assertFalse(range.contains('d'));
    }

    @Test
    public void containsChar_shouldReturnTrue_forCharOutsideNegatedRange() {
        // Arrange
        CharRange range = CharRange.isNotIn('b', 'd');

        // Assert
        assertTrue(range.contains('a'));
        assertTrue(range.contains('e'));
    }

    //-----------------------------------------------------------------------
    // contains(CharRange)
    //-----------------------------------------------------------------------

    @Test
    public void containsRange_shouldReturnTrue_forItself() {
        // Arrange
        CharRange range = CharRange.isIn('a', 'e');
        CharRange negatedRange = CharRange.isNotIn('a', 'e');

        // Assert
        assertTrue(range.contains(range));
        assertTrue(negatedRange.contains(negatedRange));
    }

    @Test
    public void containsRange_shouldReturnTrue_whenInnerRangeIsContainedInOuterRange() {
        // Arrange
        CharRange outer = CharRange.isIn('a', 'z');
        CharRange inner = CharRange.isIn('f', 'p');

        // Assert
        assertTrue(outer.contains(inner));
    }

    @Test
    public void containsRange_shouldReturnFalse_whenInnerRangeOverlapsOuterRange() {
        // Arrange
        CharRange range1 = CharRange.isIn('a', 'f');
        CharRange range2 = CharRange.isIn('d', 'z');

        // Assert
        assertFalse(range1.contains(range2));
    }

    @Test
    public void containsRange_shouldReturnTrue_whenInclusiveRangeIsContainedInNegatedRange() {
        // Arrange
        CharRange negatedRange = CharRange.isNot('m');
        CharRange inclusiveRange = CharRange.isIn('a', 'c');

        // Assert
        assertTrue("A negated range should contain any range that does not include the negated char.",
                negatedRange.contains(inclusiveRange));
    }

    @Test
    public void containsRange_shouldReturnFalse_whenInclusiveRangeOverlapsWithNegatedRange() {
        // Arrange
        CharRange negatedRange = CharRange.isNotIn('d', 'g');
        CharRange inclusiveRange = CharRange.isIn('a', 'e'); // 'e' is in the negated part

        // Assert
        assertFalse(negatedRange.contains(inclusiveRange));
    }

    @Test
    public void containsRange_shouldReturnFalse_whenComparingNegatedAndNonNegatedVersionsOfSameRange() {
        // Arrange
        CharRange negatedRange = CharRange.isNot('a');
        CharRange inclusiveRange = CharRange.is('a');

        // Assert
        assertFalse(negatedRange.contains(inclusiveRange));
        assertFalse(inclusiveRange.contains(negatedRange));
    }

    @Test
    public void containsRange_shouldReturnTrue_forTwoNegatedRangesWhereOneIsSubsetOfOther() {
        // Arrange
        // A negated range ^A contains another negated range ^B if the character set of B
        // is a superset of the character set of A.
        // The set of excluded chars ['+', 'X'] is a superset of ['+', '8'].
        // Therefore, the set of *included* chars in ^['+', '8'] contains the set of
        // *included* chars in ^['+', 'X'].
        CharRange widerNegatedRange = CharRange.isNotIn('+', '8');  // Everything except '+' through '8'
        CharRange narrowerNegatedRange = CharRange.isNotIn('+', 'X'); // Everything except '+' through 'X'

        // Act
        boolean contains = widerNegatedRange.contains(narrowerNegatedRange);

        // Assert
        assertTrue("A wider negated range should contain a narrower one", contains);
    }

    @Test(expected = NullPointerException.class)
    public void containsRange_shouldThrowNullPointerException_forNullArgument() {
        // Arrange
        CharRange range = CharRange.is('a');

        // Act
        range.contains(null);
    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------

    @Test
    public void toString_shouldReturnCorrectFormat_forSingletonRange() {
        assertEquals("a", CharRange.is('a').toString());
    }

    @Test
    public void toString_shouldReturnCorrectFormat_forMultiCharRange() {
        assertEquals("a-c", CharRange.isIn('a', 'c').toString());
    }

    @Test
    public void toString_shouldReturnCorrectFormat_forNegatedSingletonRange() {
        assertEquals("^a", CharRange.isNot('a').toString());
    }

    @Test
    public void toString_shouldReturnCorrectFormat_forNegatedMultiCharRange() {
        assertEquals("^a-c", CharRange.isNotIn('a', 'c').toString());
    }

    //-----------------------------------------------------------------------
    // iterator()
    //-----------------------------------------------------------------------

    @Test
    public void iterator_shouldIterateOverAllChars_forInclusiveRange() {
        // Arrange
        CharRange range = CharRange.isIn('a', 'c');
        List<Character> expected = new ArrayList<>();
        expected.add('a');
        expected.add('b');
        expected.add('c');

        // Act
        List<Character> actual = new ArrayList<>();
        for (char c : range) {
            actual.add(c);
        }

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void iterator_shouldBehaveCorrectly_forSingletonRange() {
        // Arrange
        CharRange range = CharRange.is('z');
        Iterator<Character> it = range.iterator();

        // Assert
        assertTrue(it.hasNext());
        assertEquals(Character.valueOf('z'), it.next());
        assertFalse(it.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void iterator_shouldThrowException_whenNextIsCalledWithoutHasNext() {
        // Arrange
        CharRange range = CharRange.is('a');
        Iterator<Character> it = range.iterator();

        // Act
        it.next(); // Consumes 'a'
        it.next(); // Should throw
    }

    @Test
    public void iterator_shouldBehaveCorrectly_forNegatedRange() {
        // Arrange
        // A negated range iterates over all characters *except* those in the range.
        // We test the first few characters to confirm the behavior.
        CharRange range = CharRange.isNotIn('\u0002', '\uFFFE');
        Iterator<Character> it = range.iterator();

        // Assert
        assertNotNull(it);
        assertTrue(it.hasNext());
        assertEquals(Character.valueOf('\u0000'), it.next());
        assertTrue(it.hasNext());
        assertEquals(Character.valueOf('\u0001'), it.next());
        assertTrue(it.hasNext());
        assertEquals(Character.valueOf('\uFFFF'), it.next());
        assertFalse(it.hasNext());
    }
}
package org.apache.commons.lang3;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for CharRange.
 *
 * Notes:
 * - Tests live in the same package to access the package-private CharRange.
 * - Uses plain JUnit 4 without EvoSuite scaffolding or mocks.
 * - Each test documents the behavior it verifies.
 */
public class CharRangeTest {

    @Test
    public void testIs_singleCharacterRange_basicProperties() {
        CharRange r = CharRange.is('P');

        assertFalse(r.isNegated());
        assertEquals('P', r.getStart());
        assertEquals('P', r.getEnd());
        assertTrue(r.contains('P'));
        assertFalse(r.contains('Q'));
    }

    @Test
    public void testIsIn_reversesMisorderedBounds_andIsInclusive() {
        // Input order is reversed; implementation should normalize to '?'..'D'
        CharRange r = CharRange.isIn('D', '?');

        assertFalse(r.isNegated());
        assertEquals('?', r.getStart());
        assertEquals('D', r.getEnd());
        assertTrue(r.contains('?'));
        assertTrue(r.contains('D'));
        assertFalse(r.contains('E'));
    }

    @Test
    public void testIsNot_negatedBasics_andContainsChar() {
        CharRange r = CharRange.isNot('k');

        assertTrue(r.isNegated());
        assertEquals('k', r.getStart());
        assertEquals('k', r.getEnd());
        assertFalse(r.contains('k')); // excluded
        assertTrue(r.contains('j'));  // everything else included
        assertTrue(r.contains('|'));
    }

    @Test
    public void testContains_char_boundsInclusiveAndOutsideExclusive() {
        CharRange r = CharRange.isIn('8', 'A');

        assertTrue(r.contains('8'));
        assertTrue(r.contains('A'));
        assertFalse(r.contains(' ')); // outside the range
    }

    @Test
    public void testContains_range_positiveWithinPositive_isTrue() {
        CharRange outer = CharRange.isIn(';', 'z');
        CharRange inner = CharRange.is('P'); // inside ';'..'z'

        assertTrue(outer.contains(inner));
    }

    @Test
    public void testContains_range_negatedDoesNotContainWhenExcludedInside() {
        // negated excludes only 'p'; the inner range crosses over 'p', so containment must be false
        CharRange negated = CharRange.isNot('p');
        CharRange inner = CharRange.isIn('o', 'q');

        assertFalse(negated.contains(inner));
    }

    @Test
    public void testContains_range_positiveDoesNotContainNegated() {
        CharRange positive = CharRange.isIn('8', 'A');
        CharRange negated = CharRange.isNotIn('Y', '@'); // a negated range

        assertFalse(positive.contains(negated));
    }

    @Test
    public void testContains_range_negatedCanContainPositiveWhenItDoesNotHitExclusion() {
        CharRange negated = CharRange.isNot('p');
        CharRange positive = CharRange.isIn('"', 'R'); // does not include 'p'

        assertTrue(negated.contains(positive));
    }

    @Test
    public void testEquals_and_hashCode_contract() {
        CharRange a1 = CharRange.isNot('O');
        CharRange a2 = CharRange.isNot('O');
        CharRange b = CharRange.is('O');     // different negation
        CharRange c = CharRange.isNot('P');  // different char

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());

        assertNotEquals(a1, b);
        assertNotEquals(a1, c);
        assertNotEquals(a1, new Object());
    }

    @Test
    public void testToString_simpleRange() {
        CharRange r = CharRange.isIn('8', 'A');

        assertEquals("8-A", r.toString());
    }

    @Test
    public void testContains_nullRangeThrowsNPE() {
        CharRange r = CharRange.is('5');

        try {
            r.contains((CharRange) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // message/content not asserted to avoid coupling to implementation details
        }
    }

    @Test
    public void testIterator_andForEach_nonNegatedYieldsAllCharsInOrder() {
        CharRange r = CharRange.isIn('0', '3');

        // Verify iteration order and content via Iterator
        Iterator<Character> it = r.iterator();
        List<Character> iterated = new ArrayList<>();
        while (it.hasNext()) {
            iterated.add(it.next());
        }
        assertEquals(4, iterated.size());
        assertArrayEquals(new Character[] {'0', '1', '2', '3'}, iterated.toArray(new Character[0]));

        // Verify the Iterable.forEach contract
        StringBuilder sb = new StringBuilder();
        r.forEach(sb::append);
        assertEquals("0123", sb.toString());
    }

    @Test
    public void testIterator_negatedFirstElementIsNotExcluded() {
        // Excludes only 'N'; the first produced element should not be 'N'
        CharRange negated = CharRange.isNotIn('N', 'N');

        Iterator<Character> it = negated.iterator();
        assertTrue(it.hasNext());
        Character first = it.next();
        assertNotEquals(Character.valueOf('N'), first);
    }

    @Test
    public void testContains_selfIsAlwaysTrue() {
        CharRange positive = CharRange.isIn('8', 'A');
        CharRange negated = CharRange.isNot('*');

        assertTrue(positive.contains(positive));
        assertTrue(negated.contains(negated));
    }

    @Test
    public void testReversedInputRange_containsStartChar() {
        CharRange r = CharRange.isIn('K', '#'); // reversed input

        assertEquals('#', r.getStart());
        assertEquals('K', r.getEnd());
        assertTrue(r.contains('#'));
        assertTrue(r.contains('J'));
        assertTrue(r.contains('K'));
        assertFalse(r.contains('L'));
    }

    @Test
    public void testForEach_nonNegated_callsConsumerExpectedNumberOfTimes() {
        CharRange r = CharRange.isIn('a', 'e'); // 5 chars: a b c d e

        AtomicInteger count = new AtomicInteger();
        r.forEach(ch -> count.incrementAndGet());

        assertEquals(5, count.get());
    }
}
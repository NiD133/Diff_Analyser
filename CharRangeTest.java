/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertNullPointerException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link CharRange}.
 */
class CharRangeTest extends AbstractLangTest {

    // Tests for class structure
    @Test
    void classModifiers_ShouldBeFinalPackagePrivate() {
        assertFalse(Modifier.isPublic(CharRange.class.getModifiers()), "Class should not be public");
        assertTrue(Modifier.isFinal(CharRange.class.getModifiers()), "Class should be final");
    }

    // Tests for factory methods and accessors
    @Test
    void factoryMethodIs_ShouldCreateSingleCharacterRange() {
        final CharRange range = CharRange.is('a');
        assertEquals('a', range.getStart());
        assertEquals('a', range.getEnd());
        assertFalse(range.isNegated());
        assertEquals("a", range.toString());
    }

    @Test
    void factoryMethodIsIn_WithNormalOrder_ShouldCreateRange() {
        final CharRange range = CharRange.isIn('a', 'e');
        assertEquals('a', range.getStart());
        assertEquals('e', range.getEnd());
        assertFalse(range.isNegated());
        assertEquals("a-e", range.toString());
    }

    @Test
    void factoryMethodIsIn_WithReversedOrder_ShouldNormalizeRange() {
        final CharRange range = CharRange.isIn('e', 'a');
        assertEquals('a', range.getStart());
        assertEquals('e', range.getEnd());
        assertFalse(range.isNegated());
        assertEquals("a-e", range.toString());
    }

    @Test
    void factoryMethodIsIn_WithSameCharacter_ShouldCreateSingleCharRange() {
        final CharRange range = CharRange.isIn('a', 'a');
        assertEquals('a', range.getStart());
        assertEquals('a', range.getEnd());
        assertFalse(range.isNegated());
        assertEquals("a", range.toString());
    }

    @Test
    void factoryMethodIsNot_ShouldCreateNegatedSingleCharRange() {
        final CharRange range = CharRange.isNot('a');
        assertEquals('a', range.getStart());
        assertEquals('a', range.getEnd());
        assertTrue(range.isNegated());
        assertEquals("^a", range.toString());
    }

    @Test
    void factoryMethodIsNotIn_WithNormalOrder_ShouldCreateNegatedRange() {
        final CharRange range = CharRange.isNotIn('a', 'e');
        assertEquals('a', range.getStart());
        assertEquals('e', range.getEnd());
        assertTrue(range.isNegated());
        assertEquals("^a-e", range.toString());
    }

    @Test
    void factoryMethodIsNotIn_WithReversedOrder_ShouldNormalizeNegatedRange() {
        final CharRange range = CharRange.isNotIn('e', 'a');
        assertEquals('a', range.getStart());
        assertEquals('e', range.getEnd());
        assertTrue(range.isNegated());
        assertEquals("^a-e", range.toString());
    }

    @Test
    void factoryMethodIsNotIn_WithSameCharacter_ShouldCreateNegatedSingleCharRange() {
        final CharRange range = CharRange.isNotIn('a', 'a');
        assertEquals('a', range.getStart());
        assertEquals('a', range.getEnd());
        assertTrue(range.isNegated());
        assertEquals("^a", range.toString());
    }

    // Tests for contains(char)
    @Test
    void containsChar_WithSingleCharRange_ShouldContainOnlyExactChar() {
        final CharRange range = CharRange.is('c');
        assertFalse(range.contains('b'));
        assertTrue(range.contains('c'));
        assertFalse(range.contains('d'));
    }

    @Test
    void containsChar_WithCharRange_ShouldContainCharsInRange() {
        final CharRange range = CharRange.isIn('c', 'd');
        assertFalse(range.contains('b'));
        assertTrue(range.contains('c'));
        assertTrue(range.contains('d'));
        assertFalse(range.contains('e'));
    }

    @Test
    void containsChar_WithReversedCharRange_ShouldNormalizeAndContainChars() {
        final CharRange range = CharRange.isIn('d', 'c'); // Reversed order
        assertFalse(range.contains('b'));
        assertTrue(range.contains('c'));
        assertTrue(range.contains('d'));
        assertFalse(range.contains('e'));
    }

    @Test
    void containsChar_WithNegatedRange_ShouldContainCharsOutsideRange() {
        final CharRange range = CharRange.isNotIn('c', 'd');
        assertTrue(range.contains('b'));
        assertFalse(range.contains('c'));
        assertFalse(range.contains('d'));
        assertTrue(range.contains('e'));
        assertTrue(range.contains((char) 0), "Should contain min char");
        assertTrue(range.contains(Character.MAX_VALUE), "Should contain max char");
    }

    // Tests for contains(CharRange)
    @Test
    void containsCharRange_WithIdenticalRange_ShouldReturnTrue() {
        final CharRange range = CharRange.is('c');
        final CharRange identicalRange = CharRange.is('c');
        assertTrue(range.contains(identicalRange));
    }

    @Test
    void containsCharRange_WithSubsetRange_ShouldReturnTrue() {
        final CharRange container = CharRange.isIn('b', 'd');
        final CharRange subset = CharRange.is('c');
        assertTrue(container.contains(subset));
    }

    @Test
    void containsCharRange_WithNonOverlappingRange_ShouldReturnFalse() {
        final CharRange range1 = CharRange.is('a');
        final CharRange range2 = CharRange.is('b');
        assertFalse(range1.contains(range2));
    }

    @Test
    void containsCharRange_WithNegatedContainerAndNormalRange_ShouldReturnFalse() {
        final CharRange negated = CharRange.isNot('c');
        final CharRange normal = CharRange.is('a');
        assertFalse(negated.contains(normal), "Negated container should not contain normal range");
    }

    @Test
    void containsCharRange_WithUniversalContainerAndNegatedRange_ShouldReturnTrue() {
        final CharRange universal = CharRange.isIn((char) 0, Character.MAX_VALUE);
        final CharRange negated = CharRange.isNot('c');
        assertTrue(universal.contains(negated));
    }

    @Test
    void containsCharRange_WithNegatedRangeAndPartialOverlap_ShouldReturnFalse() {
        final CharRange negated = CharRange.isNotIn('b', 'd');
        final CharRange normal = CharRange.isIn('a', 'b');
        assertFalse(negated.contains(normal), "Negated range should not contain partially overlapping range");
    }

    @Test
    void containsCharRange_WithNullArgument_ShouldThrowNullPointerException() {
        final CharRange range = CharRange.is('a');
        final NullPointerException e = assertNullPointerException(() -> range.contains(null));
        assertEquals("range", e.getMessage());
    }

    // Tests for equals and hashCode
    @Test
    void equals_WithSameRange_ShouldReturnTrue() {
        final CharRange range1 = CharRange.is('a');
        final CharRange range2 = CharRange.is('a');
        assertEquals(range1, range2);
    }

    @Test
    void equals_WithDifferentRange_ShouldReturnFalse() {
        final CharRange range1 = CharRange.is('a');
        final CharRange range2 = CharRange.isIn('a', 'e');
        assertNotEquals(range1, range2);
    }

    @Test
    void equals_WithNull_ShouldReturnFalse() {
        final CharRange range = CharRange.is('a');
        assertNotEquals(null, range);
    }

    @Test
    void hashCode_ForEqualObjects_ShouldBeEqual() {
        final CharRange range1 = CharRange.is('a');
        final CharRange range2 = CharRange.is('a');
        assertEquals(range1.hashCode(), range2.hashCode());
    }

    @Test
    void hashCode_ForDifferentObjects_ShouldBeDifferent() {
        final CharRange range1 = CharRange.is('a');
        final CharRange range2 = CharRange.is('b');
        assertNotEquals(range1.hashCode(), range2.hashCode());
    }

    // Tests for iterator
    @Test
    void iterator_ForSingleCharRange_ShouldIterateSingleCharacter() {
        final CharRange range = CharRange.is('a');
        final Iterator<Character> it = range.iterator();
        assertTrue(it.hasNext());
        assertEquals(Character.valueOf('a'), it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void iterator_ForCharRange_ShouldIterateAllCharactersInOrder() {
        final CharRange range = CharRange.isIn('a', 'd');
        final Iterator<Character> it = range.iterator();
        assertTrue(it.hasNext());
        assertEquals(Character.valueOf('a'), it.next());
        assertEquals(Character.valueOf('b'), it.next());
        assertEquals(Character.valueOf('c'), it.next());
        assertEquals(Character.valueOf('d'), it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void iterator_ForNegatedRange_ShouldIterateAllCharsExceptExcluded() {
        final CharRange range = CharRange.isNot('a');
        final Iterator<Character> it = range.iterator();
        while (it.hasNext()) {
            assertNotEquals('a', (char) it.next());
        }
    }

    @Test
    void iterator_ForEmptyRange_ShouldHaveNoElements() {
        final CharRange range = CharRange.isNotIn((char) 0, Character.MAX_VALUE);
        final Iterator<Character> it = range.iterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void iterator_ForNegatedRangeExcludingFirstChar_ShouldIterateMinChar() {
        final CharRange range = CharRange.isNotIn((char) 1, Character.MAX_VALUE);
        final Iterator<Character> it = range.iterator();
        assertTrue(it.hasNext());
        assertEquals(Character.valueOf((char) 0), it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void iterator_ForNegatedRangeExcludingLastChar_ShouldIterateMaxChar() {
        final CharRange range = CharRange.isNotIn((char) 0, (char) (Character.MAX_VALUE - 1));
        final Iterator<Character> it = range.iterator();
        assertTrue(it.hasNext());
        assertEquals(Character.valueOf(Character.MAX_VALUE), it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void iteratorRemove_ShouldThrowUnsupportedOperationException() {
        final CharRange range = CharRange.is('a');
        final Iterator<Character> it = range.iterator();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    // Tests for serialization
    @Test
    void serialization_ShouldRoundtripCorrectly() {
        CharRange range = CharRange.is('a');
        assertEquals(range, SerializationUtils.clone(range));
        range = CharRange.isIn('a', 'e');
        assertEquals(range, SerializationUtils.clone(range));
        range = CharRange.isNotIn('a', 'e');
        assertEquals(range, SerializationUtils.clone(range));
    }
}
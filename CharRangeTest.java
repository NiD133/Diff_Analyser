/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertNullPointerException;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CharRange}.
 */
class CharRangeTest extends AbstractLangTest {

    @Test
    void testClassModifiers() {
        // Verify class is non-public and final
        assertFalse(Modifier.isPublic(CharRange.class.getModifiers()), "CharRange should not be public");
        assertTrue(Modifier.isFinal(CharRange.class.getModifiers()), "CharRange should be final");
    }

    @Test
    void testSingleCharacterRange() {
        CharRange range = CharRange.is('a');
        assertEquals('a', range.getStart(), "Start should be 'a'");
        assertEquals('a', range.getEnd(), "End should be 'a'");
        assertFalse(range.isNegated(), "Range should not be negated");
        assertEquals("a", range.toString(), "String representation should be 'a'");
    }

    @Test
    void testRangeWithStartAndEnd() {
        CharRange range = CharRange.isIn('a', 'e');
        assertEquals('a', range.getStart(), "Start should be 'a'");
        assertEquals('e', range.getEnd(), "End should be 'e'");
        assertFalse(range.isNegated(), "Range should not be negated");
        assertEquals("a-e", range.toString(), "String representation should be 'a-e'");
    }

    @Test
    void testReversedRange() {
        CharRange range = CharRange.isIn('e', 'a');
        assertEquals('a', range.getStart(), "Start should be 'a' after reversal");
        assertEquals('e', range.getEnd(), "End should be 'e' after reversal");
        assertFalse(range.isNegated(), "Range should not be negated");
        assertEquals("a-e", range.toString(), "String representation should be 'a-e'");
    }

    @Test
    void testNegatedSingleCharacterRange() {
        CharRange range = CharRange.isNot('a');
        assertEquals('a', range.getStart(), "Start should be 'a'");
        assertEquals('a', range.getEnd(), "End should be 'a'");
        assertTrue(range.isNegated(), "Range should be negated");
        assertEquals("^a", range.toString(), "String representation should be '^a'");
    }

    @Test
    void testNegatedRangeWithStartAndEnd() {
        CharRange range = CharRange.isNotIn('a', 'e');
        assertEquals('a', range.getStart(), "Start should be 'a'");
        assertEquals('e', range.getEnd(), "End should be 'e'");
        assertTrue(range.isNegated(), "Range should be negated");
        assertEquals("^a-e", range.toString(), "String representation should be '^a-e'");
    }

    @Test
    void testCharacterContainment() {
        CharRange range = CharRange.is('c');
        assertFalse(range.contains('b'), "'b' should not be contained");
        assertTrue(range.contains('c'), "'c' should be contained");
        assertFalse(range.contains('d'), "'d' should not be contained");

        range = CharRange.isIn('c', 'd');
        assertTrue(range.contains('c'), "'c' should be contained");
        assertTrue(range.contains('d'), "'d' should be contained");
        assertFalse(range.contains('e'), "'e' should not be contained");

        range = CharRange.isNotIn('c', 'd');
        assertTrue(range.contains('b'), "'b' should be contained");
        assertFalse(range.contains('c'), "'c' should not be contained");
        assertTrue(range.contains('e'), "'e' should be contained");
    }

    @Test
    void testRangeContainment() {
        CharRange singleCharRange = CharRange.is('c');
        CharRange multiCharRange = CharRange.isIn('b', 'd');
        CharRange negatedRange = CharRange.isNot('c');

        assertFalse(singleCharRange.contains(multiCharRange), "Single character range should not contain multi-character range");
        assertTrue(multiCharRange.contains(singleCharRange), "Multi-character range should contain single character range");
        assertTrue(negatedRange.contains(singleCharRange), "Negated range should contain single character range");
    }

    @Test
    void testNullArgumentInContains() {
        CharRange range = CharRange.is('a');
        NullPointerException exception = assertNullPointerException(() -> range.contains(null));
        assertEquals("range", exception.getMessage(), "Exception message should be 'range'");
    }

    @Test
    void testEquality() {
        CharRange rangeA = CharRange.is('a');
        CharRange rangeAE = CharRange.isIn('a', 'e');
        CharRange rangeBF = CharRange.isIn('b', 'f');

        assertEquals(rangeA, CharRange.is('a'), "Ranges should be equal");
        assertNotEquals(rangeA, rangeAE, "Ranges should not be equal");
        assertNotEquals(rangeAE, rangeBF, "Ranges should not be equal");
    }

    @Test
    void testHashCodeConsistency() {
        CharRange rangeA = CharRange.is('a');
        CharRange rangeAE = CharRange.isIn('a', 'e');

        assertEquals(rangeA.hashCode(), CharRange.is('a').hashCode(), "Hash codes should be consistent");
        assertNotEquals(rangeA.hashCode(), rangeAE.hashCode(), "Hash codes should not be equal");
    }

    @Test
    void testIteratorFunctionality() {
        CharRange range = CharRange.is('a');
        Iterator<Character> iterator = range.iterator();

        assertTrue(iterator.hasNext(), "Iterator should have next element");
        assertEquals(Character.valueOf('a'), iterator.next(), "Next element should be 'a'");
        assertFalse(iterator.hasNext(), "Iterator should not have next element");
    }

    @Test
    void testIteratorRemoveUnsupported() {
        CharRange range = CharRange.is('a');
        Iterator<Character> iterator = range.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove, "Remove operation should throw exception");
    }

    @Test
    void testSerialization() {
        CharRange range = CharRange.is('a');
        assertEquals(range, SerializationUtils.clone(range), "Cloned range should be equal to original");
    }
}
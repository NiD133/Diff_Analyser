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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link CharSet}.
 */
class CharSetTest extends AbstractLangTest {

    // Helper method to verify expected character ranges
    private void assertCharRanges(CharSet charSet, CharRange... expectedRanges) {
        CharRange[] actualRanges = charSet.getCharRanges();
        assertEquals(expectedRanges.length, actualRanges.length, "Number of ranges");
        for (CharRange expected : expectedRanges) {
            assertTrue(ArrayUtils.contains(actualRanges, expected), 
                      "Missing range: " + expected);
        }
    }

    @Test
    void classModifiers_shouldBePublicAndNonFinal() {
        assertTrue(Modifier.isPublic(CharSet.class.getModifiers()));
        assertFalse(Modifier.isFinal(CharSet.class.getModifiers()));
    }

    // ========== Combo Pattern Tests ==========
    @Test
    void getInstance_withSingleCharacters_createsSingleCharRanges() {
        CharSet set = CharSet.getInstance("abc");
        assertCharRanges(set, 
            CharRange.is('a'), 
            CharRange.is('b'), 
            CharRange.is('c')
        );
    }

    @Test
    void getInstance_withHyphenRanges_createsRangeObjects() {
        CharSet set = CharSet.getInstance("a-ce-f");
        assertCharRanges(set,
            CharRange.isIn('a', 'c'),
            CharRange.isIn('e', 'f')
        );
    }

    @Test
    void getInstance_withMixedSingleCharAndRange_createsCorrectRanges() {
        CharSet set = CharSet.getInstance("ae-f");
        assertCharRanges(set,
            CharRange.is('a'),
            CharRange.isIn('e', 'f')
        );
    }

    @Test
    void getInstance_withRangeThenSingleChar_ordersRangesConsistently() {
        CharSet set = CharSet.getInstance("e-fa");
        assertCharRanges(set,
            CharRange.is('a'),
            CharRange.isIn('e', 'f')
        );
    }

    @Test
    void getInstance_withMultipleComponents_createsAllRanges() {
        CharSet set = CharSet.getInstance("ae-fm-pz");
        assertCharRanges(set,
            CharRange.is('a'),
            CharRange.isIn('e', 'f'),
            CharRange.isIn('m', 'p'),
            CharRange.is('z')
        );
    }

    // ========== Negated Pattern Tests ==========
    @Test
    void getInstance_withLeadingCaret_createsNegatedFirstCharacter() {
        CharSet set = CharSet.getInstance("^abc");
        assertCharRanges(set,
            CharRange.isNot('a'),
            CharRange.is('b'),
            CharRange.is('c')
        );
    }

    @Test
    void getInstance_withCaretAfterFirstChar_createsNegatedCharacter() {
        CharSet set = CharSet.getInstance("b^ac");
        assertCharRanges(set,
            CharRange.is('b'),
            CharRange.isNot('a'),
            CharRange.is('c')
        );
    }

    @Test
    void getInstance_withMultipleCarets_createsMultipleNegatedRanges() {
        CharSet set = CharSet.getInstance("^b^a");
        assertCharRanges(set,
            CharRange.isNot('b'),
            CharRange.isNot('a')
        );
    }

    @Test
    void getInstance_withNegatedRangeAndCharacter_createsCombinedRanges() {
        CharSet set = CharSet.getInstance("b^a-c^z");
        assertCharRanges(set,
            CharRange.isNotIn('a', 'c'),
            CharRange.isNot('z'),
            CharRange.is('b')
        );
    }

    // ========== Edge Case Tests ==========
    @Test
    void getInstance_withCaretInRange_treatsCaretAsCharacter() {
        CharSet set = CharSet.getInstance("a-^c");
        assertCharRanges(set,
            CharRange.isIn('a', '^'),
            CharRange.is('c')
        );
        assertFalse(set.contains('b'));
        assertTrue(set.contains('^'));
    }

    @Test
    void getInstance_withLeadingCaretAndRange_createsNegatedRange() {
        CharSet set = CharSet.getInstance("^a-^c");
        assertCharRanges(set,
            CharRange.isNotIn('a', '^'),
            CharRange.is('c')
        );
        assertTrue(set.contains('b'));
        assertFalse(set.contains('^'));
    }

    @Test
    void getInstance_withHyphenAndCaret_createsValidRanges() {
        CharSet set = CharSet.getInstance("a- ^-- ");
        assertCharRanges(set,
            CharRange.isIn('a', ' '),
            CharRange.isNotIn('-', ' ')
        );
        assertTrue(set.contains('#'));
    }

    @Test
    void getInstance_withCaretRange_createsValidRange() {
        CharSet set = CharSet.getInstance("^-b");
        assertCharRanges(set, CharRange.isIn('^', 'b'));
        assertTrue(set.contains('^'));
        assertTrue(set.contains('a'));
    }

    @Test
    void getInstance_withReversedCaretRange_normalizesRange() {
        CharSet set = CharSet.getInstance("b-^");
        assertCharRanges(set, CharRange.isIn('^', 'b'));
        assertTrue(set.contains('a'));
        assertTrue(set.contains('^'));
    }

    // ========== Dash Handling Tests ==========
    @Test
    void getInstance_withSingleDash_createsDashCharacter() {
        CharSet set = CharSet.getInstance("-");
        assertCharRanges(set, CharRange.is('-'));
    }

    @Test
    void getInstance_withMultipleDashes_createsSingleDashCharacter() {
        CharSet set = CharSet.getInstance("---");
        assertCharRanges(set, CharRange.is('-'));
    }

    @Test
    void getInstance_withDashAndChar_createsSeparateRanges() {
        CharSet set = CharSet.getInstance("-a");
        assertCharRanges(set, 
            CharRange.is('-'), 
            CharRange.is('a')
        );
    }

    @Test
    void getInstance_withCharThenDash_createsRange() {
        CharSet set = CharSet.getInstance("a--");
        assertCharRanges(set, CharRange.isIn('a', '-'));
    }

    // ========== Null/Empty Handling ==========
    @Test
    void getInstance_withNull_returnsEmptySet() {
        CharSet set = CharSet.getInstance((String) null);
        assertEquals(0, set.getCharRanges().length);
        assertEquals("[]", set.toString());
    }

    @Test
    void getInstance_withEmptyString_returnsEmptySet() {
        CharSet set = CharSet.getInstance("");
        assertEquals(0, set.getCharRanges().length);
        assertEquals("[]", set.toString());
    }

    @Test
    void getInstance_withSingleChar_createsSingleRange() {
        CharSet set = CharSet.getInstance("a");
        assertCharRanges(set, CharRange.is('a'));
        assertEquals("[a]", set.toString());
    }

    // ========== Contains() Tests ==========
    @Test
    void contains_withRangeSet_returnsCorrectValues() {
        CharSet btod = CharSet.getInstance("b-d");
        
        assertFalse(btod.contains('a'));
        assertTrue(btod.contains('b'));
        assertTrue(btod.contains('c'));
        assertTrue(btod.contains('d'));
        assertFalse(btod.contains('e'));
    }

    @Test
    void contains_withNegatedSet_returnsCorrectValues() {
        CharSet notbtod = CharSet.getInstance("^b-d");
        
        assertTrue(notbtod.contains('a'));
        assertFalse(notbtod.contains('b'));
        assertFalse(notbtod.contains('c'));
        assertFalse(notbtod.contains('d'));
        assertTrue(notbtod.contains('e'));
    }

    @Test
    void contains_withReversedRange_handlesCorrectly() {
        CharSet dtob = CharSet.getInstance("d-b");
        CharRange[] ranges = dtob.getCharRanges();
        
        assertEquals(1, ranges.length);
        assertEquals("[b-d]", dtob.toString());
        assertTrue(dtob.contains('c'));
    }

    // ========== equals() Tests ==========
    @Test
    void equals_withSameDefinition_returnsTrue() {
        CharSet abc1 = CharSet.getInstance("abc");
        CharSet abc2 = CharSet.getInstance("abc");
        assertEquals(abc1, abc2);
    }

    @Test
    void equals_withDifferentDefinition_returnsFalse() {
        CharSet abc = CharSet.getInstance("abc");
        CharSet range = CharSet.getInstance("a-c");
        assertNotEquals(abc, range);
    }

    @Test
    void equals_withNull_returnsFalse() {
        CharSet abc = CharSet.getInstance("abc");
        assertNotEquals(null, abc);
    }

    // ========== Static Instances Tests ==========
    @Test
    void getInstance_withCommonPatterns_returnsCachedInstances() {
        assertSame(CharSet.EMPTY, CharSet.getInstance((String) null));
        assertSame(CharSet.EMPTY, CharSet.getInstance(""));
        assertSame(CharSet.ASCII_ALPHA, CharSet.getInstance("a-zA-Z"));
        assertSame(CharSet.ASCII_ALPHA_LOWER, CharSet.getInstance("a-z"));
        assertSame(CharSet.ASCII_NUMERIC, CharSet.getInstance("0-9"));
    }

    @Test
    void getInstance_withStringArray_handlesVariousInputs() {
        assertNull(CharSet.getInstance((String[]) null));
        assertEquals("[]", CharSet.getInstance().toString());
        assertEquals("[]", CharSet.getInstance(new String[] {null}).toString());
        assertEquals("[a-e]", CharSet.getInstance("a-e").toString());
    }

    @Test
    void hashCode_withEqualObjects_returnsSameValue() {
        CharSet abc1 = CharSet.getInstance("abc");
        CharSet abc2 = CharSet.getInstance("abc");
        assertEquals(abc1.hashCode(), abc2.hashCode());
    }

    // ========== Javadoc Examples ==========
    @Test
    void javadocExamples_behaveAsDocumented() {
        // Example 1: Negated range
        assertFalse(CharSet.getInstance("^a-c").contains('a'));
        assertTrue(CharSet.getInstance("^a-c").contains('d'));
        
        // Example 2: Double negation
        assertTrue(CharSet.getInstance("^^a-c").contains('a'));
        assertFalse(CharSet.getInstance("^^a-c").contains('^'));
        
        // Example 3: Combined patterns
        assertTrue(CharSet.getInstance("^a-cd-f").contains('d'));
        assertTrue(CharSet.getInstance("a-c^").contains('^'));
        assertTrue(CharSet.getInstance("^", "a-c").contains('^'));
    }

    // ========== Serialization Tests ==========
    @Test
    void serialization_withSingleChar_roundtripsSuccessfully() {
        CharSet set = CharSet.getInstance("a");
        assertEquals(set, SerializationUtils.clone(set));
    }

    @Test
    void serialization_withCharRange_roundtripsSuccessfully() {
        CharSet set = CharSet.getInstance("a-e");
        assertEquals(set, SerializationUtils.clone(set));
    }

    @Test
    void serialization_withComplexSet_roundtripsSuccessfully() {
        CharSet set = CharSet.getInstance("be-f^a-z");
        assertEquals(set, SerializationUtils.clone(set));
    }

    // ========== Static Field Tests ==========
    @Test
    void staticInstance_EMPTY_hasNoRanges() {
        assertEquals(0, CharSet.EMPTY.getCharRanges().length);
    }

    @Test
    void staticInstance_ASCII_ALPHA_hasLetterRanges() {
        CharRange[] ranges = CharSet.ASCII_ALPHA.getCharRanges();
        assertEquals(2, ranges.length);
        assertTrue(ArrayUtils.contains(ranges, CharRange.isIn('a', 'z')));
        assertTrue(ArrayUtils.contains(ranges, CharRange.isIn('A', 'Z')));
    }

    @Test
    void staticInstance_ASCII_NUMERIC_hasDigitRange() {
        CharRange[] ranges = CharSet.ASCII_NUMERIC.getCharRanges();
        assertEquals(1, ranges.length);
        assertTrue(ArrayUtils.contains(ranges, CharRange.isIn('0', '9')));
    }
}
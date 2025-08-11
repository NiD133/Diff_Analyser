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
 * 
 * CharSet supports a special syntax for defining character sets:
 * - Individual characters: "abc" creates set {a, b, c}
 * - Ranges: "a-z" creates set {a, b, c, ..., z}
 * - Negation: "^a-z" creates set of all characters EXCEPT a-z
 * - Combinations: "a-ce-g" creates set {a, b, c, e, f, g}
 */
class CharSetTest extends AbstractLangTest {

    // ========== Class Structure Tests ==========
    
    @Test
    void shouldHaveCorrectClassModifiers() {
        assertTrue(Modifier.isPublic(CharSet.class.getModifiers()));
        assertFalse(Modifier.isFinal(CharSet.class.getModifiers()));
    }

    // ========== Basic Construction Tests ==========
    
    @Test
    void shouldCreateEmptyCharSetFromNullAndEmptyString() {
        CharSet nullSet = CharSet.getInstance((String) null);
        CharSet emptySet = CharSet.getInstance("");
        
        assertEquals(0, nullSet.getCharRanges().length);
        assertEquals(0, emptySet.getCharRanges().length);
        assertEquals("[]", nullSet.toString());
        assertEquals("[]", emptySet.toString());
    }

    @Test
    void shouldCreateSingleCharacterSet() {
        CharSet charSet = CharSet.getInstance("a");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(1, ranges.length);
        assertEquals("a", ranges[0].toString());
        assertEquals("[a]", charSet.toString());
    }

    @Test
    void shouldCreateNegatedSingleCharacterSet() {
        CharSet charSet = CharSet.getInstance("^a");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(1, ranges.length);
        assertEquals("^a", ranges[0].toString());
        assertEquals("[^a]", charSet.toString());
    }

    @Test
    void shouldCreateSimpleCharacterRange() {
        CharSet charSet = CharSet.getInstance("a-e");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(1, ranges.length);
        assertEquals("a-e", ranges[0].toString());
        assertEquals("[a-e]", charSet.toString());
    }

    @Test
    void shouldCreateNegatedCharacterRange() {
        CharSet charSet = CharSet.getInstance("^a-e");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(1, ranges.length);
        assertEquals("^a-e", ranges[0].toString());
        assertEquals("[^a-e]", charSet.toString());
    }

    // ========== Multiple Character and Range Tests ==========
    
    @Test
    void shouldCreateSetFromMultipleIndividualCharacters() {
        // Test: "abc" should create three individual character ranges
        CharSet charSet = CharSet.getInstance("abc");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(3, ranges.length);
        assertContainsCharRange(ranges, CharRange.is('a'));
        assertContainsCharRange(ranges, CharRange.is('b'));
        assertContainsCharRange(ranges, CharRange.is('c'));
    }

    @Test
    void shouldCreateSetFromMultipleRanges() {
        // Test: "a-ce-f" should create two ranges: a-c and e-f
        CharSet charSet = CharSet.getInstance("a-ce-f");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(2, ranges.length);
        assertContainsCharRange(ranges, CharRange.isIn('a', 'c'));
        assertContainsCharRange(ranges, CharRange.isIn('e', 'f'));
    }

    @Test
    void shouldCreateSetFromMixedCharactersAndRanges() {
        // Test: "ae-f" should create char 'a' and range e-f
        CharSet charSet = CharSet.getInstance("ae-f");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(2, ranges.length);
        assertContainsCharRange(ranges, CharRange.is('a'));
        assertContainsCharRange(ranges, CharRange.isIn('e', 'f'));
    }

    @Test
    void shouldHandleOrderIndependentRangeDefinition() {
        // Test: "e-fa" should create the same as "ae-f" (order shouldn't matter)
        CharSet charSet = CharSet.getInstance("e-fa");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(2, ranges.length);
        assertContainsCharRange(ranges, CharRange.is('a'));
        assertContainsCharRange(ranges, CharRange.isIn('e', 'f'));
    }

    @Test
    void shouldCreateComplexMixedCharacterSet() {
        // Test: "ae-fm-pz" should create char 'a', range e-f, range m-p, char 'z'
        CharSet charSet = CharSet.getInstance("ae-fm-pz");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(4, ranges.length);
        assertContainsCharRange(ranges, CharRange.is('a'));
        assertContainsCharRange(ranges, CharRange.isIn('e', 'f'));
        assertContainsCharRange(ranges, CharRange.isIn('m', 'p'));
        assertContainsCharRange(ranges, CharRange.is('z'));
    }

    // ========== Negation Tests ==========
    
    @Test
    void shouldHandleNegationAtBeginning() {
        // Test: "^abc" should negate 'a' but include 'b' and 'c' normally
        CharSet charSet = CharSet.getInstance("^abc");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(3, ranges.length);
        assertContainsCharRange(ranges, CharRange.isNot('a'));
        assertContainsCharRange(ranges, CharRange.is('b'));
        assertContainsCharRange(ranges, CharRange.is('c'));
    }

    @Test
    void shouldHandleNegationInMiddle() {
        // Test: "b^ac" should include 'b', negate 'a', include 'c'
        CharSet charSet = CharSet.getInstance("b^ac");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(3, ranges.length);
        assertContainsCharRange(ranges, CharRange.is('b'));
        assertContainsCharRange(ranges, CharRange.isNot('a'));
        assertContainsCharRange(ranges, CharRange.is('c'));
    }

    @Test
    void shouldHandleMultipleNegations() {
        // Test: "^b^a" should negate both 'b' and 'a'
        CharSet charSet = CharSet.getInstance("^b^a");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(2, ranges.length);
        assertContainsCharRange(ranges, CharRange.isNot('b'));
        assertContainsCharRange(ranges, CharRange.isNot('a'));
    }

    @Test
    void shouldHandleNegatedRangeWithOtherElements() {
        // Test: "b^a-c^z" should include 'b', negate range a-c, negate 'z'
        CharSet charSet = CharSet.getInstance("b^a-c^z");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(3, ranges.length);
        assertContainsCharRange(ranges, CharRange.is('b'));
        assertContainsCharRange(ranges, CharRange.isNotIn('a', 'c'));
        assertContainsCharRange(ranges, CharRange.isNot('z'));
    }

    // ========== Edge Cases with Special Characters ==========
    
    @Test
    void shouldHandleCaretInRanges() {
        // Test: "a-^c" should create range from 'a' to '^', plus char 'c'
        CharSet charSet = CharSet.getInstance("a-^c");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertContainsCharRange(ranges, CharRange.isIn('a', '^'));
        assertContainsCharRange(ranges, CharRange.is('c'));
        
        // Verify behavior
        assertFalse(charSet.contains('b'));
        assertTrue(charSet.contains('^'));
        assertTrue(charSet.contains('_')); // between ^ and a in ASCII
        assertTrue(charSet.contains('c'));
    }

    @Test
    void shouldHandleNegatedCaretRange() {
        // Test: "^a-^c" should negate range from 'a' to '^', plus include 'c'
        CharSet charSet = CharSet.getInstance("^a-^c");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertContainsCharRange(ranges, CharRange.isNotIn('a', '^'));
        assertContainsCharRange(ranges, CharRange.is('c'));
        
        // Verify behavior
        assertTrue(charSet.contains('b'));   // 'b' is in the negated range
        assertFalse(charSet.contains('^'));  // '^' is excluded by negation
        assertFalse(charSet.contains('_'));  // '_' is excluded by negation
    }

    @Test
    void shouldHandleComplexCaretAndNegationCombination() {
        // Test: "a- ^-- " contains everything (complex edge case)
        CharSet charSet = CharSet.getInstance("a- ^-- ");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertContainsCharRange(ranges, CharRange.isIn('a', ' '));     // "a- "
        assertContainsCharRange(ranges, CharRange.isNotIn('-', ' '));  // "^-- "
        
        // This combination should contain most characters
        assertTrue(charSet.contains('#'));
        assertTrue(charSet.contains('^'));
        assertTrue(charSet.contains('a'));
        assertTrue(charSet.contains('*'));
        assertTrue(charSet.contains('A'));
    }

    // ========== Dash Handling Tests ==========
    
    @Test
    void shouldHandleSingleDash() {
        CharSet charSet = CharSet.getInstance("-");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(1, ranges.length);
        assertContainsCharRange(ranges, CharRange.is('-'));
    }

    @Test
    void shouldHandleMultipleDashes() {
        // Multiple dashes should be treated as single dash character
        CharSet charSet = CharSet.getInstance("--");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(1, ranges.length);
        assertContainsCharRange(ranges, CharRange.is('-'));
    }

    @Test
    void shouldHandleDashAtBeginning() {
        // Test: "-a" should create dash char and 'a' char
        CharSet charSet = CharSet.getInstance("-a");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(2, ranges.length);
        assertContainsCharRange(ranges, CharRange.is('-'));
        assertContainsCharRange(ranges, CharRange.is('a'));
    }

    @Test
    void shouldHandleDashAtEnd() {
        // Test: "a-" should create 'a' char and dash char
        CharSet charSet = CharSet.getInstance("a-");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(2, ranges.length);
        assertContainsCharRange(ranges, CharRange.is('a'));
        assertContainsCharRange(ranges, CharRange.is('-'));
    }

    @Test
    void shouldCreateRangeWithDoubleDash() {
        // Test: "a--" should create range from 'a' to '-'
        CharSet charSet = CharSet.getInstance("a--");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(1, ranges.length);
        assertContainsCharRange(ranges, CharRange.isIn('a', '-'));
    }

    // ========== Caret Edge Cases ==========
    
    @Test
    void shouldHandleSingleCaret() {
        // Single caret should be treated as literal character
        CharSet charSet = CharSet.getInstance("^");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(1, ranges.length);
        assertContainsCharRange(ranges, CharRange.is('^'));
    }

    @Test
    void shouldHandleDoubleCaret() {
        // Double caret should negate the caret character
        CharSet charSet = CharSet.getInstance("^^");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(1, ranges.length);
        assertContainsCharRange(ranges, CharRange.isNot('^'));
    }

    @Test
    void shouldHandleTripleCaret() {
        // Triple caret: negate caret + literal caret
        CharSet charSet = CharSet.getInstance("^^^");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(2, ranges.length);
        assertContainsCharRange(ranges, CharRange.isNot('^'));
        assertContainsCharRange(ranges, CharRange.is('^'));
    }

    @Test
    void shouldHandleCaretAtEnd() {
        // Test: "a^" should include 'a' and '^' as literal characters
        CharSet charSet = CharSet.getInstance("a^");
        CharRange[] ranges = charSet.getCharRanges();
        
        assertEquals(2, ranges.length);
        assertContainsCharRange(ranges, CharRange.is('a'));
        assertContainsCharRange(ranges, CharRange.is('^'));
    }

    // ========== Character Membership Tests ==========
    
    @Test
    void shouldCorrectlyIdentifyCharacterMembership() {
        CharSet rangeSet = CharSet.getInstance("b-d");        // Range b to d
        CharSet individualSet = CharSet.getInstance("bcd");   // Individual chars b, c, d
        CharSet sparseSet = CharSet.getInstance("bd");        // Just b and d
        CharSet negatedSet = CharSet.getInstance("^b-d");     // Everything except b-d
        
        // Test range set
        assertFalse(rangeSet.contains('a'));
        assertTrue(rangeSet.contains('b'));
        assertTrue(rangeSet.contains('c'));
        assertTrue(rangeSet.contains('d'));
        assertFalse(rangeSet.contains('e'));
        
        // Test individual character set (should behave same as range)
        assertFalse(individualSet.contains('a'));
        assertTrue(individualSet.contains('b'));
        assertTrue(individualSet.contains('c'));
        assertTrue(individualSet.contains('d'));
        assertFalse(individualSet.contains('e'));
        
        // Test sparse set (missing 'c')
        assertFalse(sparseSet.contains('a'));
        assertTrue(sparseSet.contains('b'));
        assertFalse(sparseSet.contains('c'));  // Key difference
        assertTrue(sparseSet.contains('d'));
        assertFalse(sparseSet.contains('e'));
        
        // Test negated set (opposite of range)
        assertTrue(negatedSet.contains('a'));
        assertFalse(negatedSet.contains('b'));
        assertFalse(negatedSet.contains('c'));
        assertFalse(negatedSet.contains('d'));
        assertTrue(negatedSet.contains('e'));
    }

    @Test
    void shouldHandleReversedRanges() {
        // Test that "d-b" is equivalent to "b-d"
        CharSet reversedRange = CharSet.getInstance("d-b");
        CharSet normalRange = CharSet.getInstance("b-d");
        
        // Both should contain the same characters
        assertTrue(reversedRange.contains('b'));
        assertTrue(reversedRange.contains('c'));
        assertTrue(reversedRange.contains('d'));
        assertFalse(reversedRange.contains('a'));
        assertFalse(reversedRange.contains('e'));
        
        // Should have same string representation
        assertEquals("[b-d]", reversedRange.toString());
        assertEquals(normalRange.toString(), reversedRange.toString());
    }

    // ========== Factory Method Tests ==========
    
    @Test
    void shouldReturnSameInstanceForCommonPatterns() {
        // Test that common patterns return cached instances
        assertSame(CharSet.EMPTY, CharSet.getInstance((String) null));
        assertSame(CharSet.EMPTY, CharSet.getInstance(""));
        assertSame(CharSet.ASCII_ALPHA, CharSet.getInstance("a-zA-Z"));
        assertSame(CharSet.ASCII_ALPHA, CharSet.getInstance("A-Za-z"));
        assertSame(CharSet.ASCII_ALPHA_LOWER, CharSet.getInstance("a-z"));
        assertSame(CharSet.ASCII_ALPHA_UPPER, CharSet.getInstance("A-Z"));
        assertSame(CharSet.ASCII_NUMERIC, CharSet.getInstance("0-9"));
    }

    @Test
    void shouldHandleStringArrayInput() {
        assertNull(CharSet.getInstance((String[]) null));
        assertEquals("[]", CharSet.getInstance().toString());
        assertEquals("[]", CharSet.getInstance(new String[] {null}).toString());
        assertEquals("[a-e]", CharSet.getInstance("a-e").toString());
    }

    // ========== Predefined CharSet Tests ==========
    
    @Test
    void shouldHaveCorrectPredefinedCharSets() {
        // Test EMPTY
        assertEquals(0, CharSet.EMPTY.getCharRanges().length);
        
        // Test ASCII_ALPHA (a-z and A-Z)
        CharRange[] alphaRanges = CharSet.ASCII_ALPHA.getCharRanges();
        assertEquals(2, alphaRanges.length);
        assertContainsCharRange(alphaRanges, CharRange.isIn('a', 'z'));
        assertContainsCharRange(alphaRanges, CharRange.isIn('A', 'Z'));
        
        // Test ASCII_ALPHA_LOWER (a-z)
        CharRange[] lowerRanges = CharSet.ASCII_ALPHA_LOWER.getCharRanges();
        assertEquals(1, lowerRanges.length);
        assertContainsCharRange(lowerRanges, CharRange.isIn('a', 'z'));
        
        // Test ASCII_ALPHA_UPPER (A-Z)
        CharRange[] upperRanges = CharSet.ASCII_ALPHA_UPPER.getCharRanges();
        assertEquals(1, upperRanges.length);
        assertContainsCharRange(upperRanges, CharRange.isIn('A', 'Z'));
        
        // Test ASCII_NUMERIC (0-9)
        CharRange[] numericRanges = CharSet.ASCII_NUMERIC.getCharRanges();
        assertEquals(1, numericRanges.length);
        assertContainsCharRange(numericRanges, CharRange.isIn('0', '9'));
    }

    // ========== Equality and HashCode Tests ==========
    
    @Test
    void shouldImplementEqualityCorrectly() {
        CharSet individualChars = CharSet.getInstance("abc");
        CharSet individualChars2 = CharSet.getInstance("abc");
        CharSet rangeChars = CharSet.getInstance("a-c");
        CharSet rangeChars2 = CharSet.getInstance("a-c");
        CharSet negatedRange = CharSet.getInstance("^a-c");
        CharSet negatedRange2 = CharSet.getInstance("^a-c");

        // Test null
        assertNotEquals(null, individualChars);

        // Test same content, same representation
        assertEquals(individualChars, individualChars);
        assertEquals(individualChars, individualChars2);
        assertEquals(rangeChars, rangeChars2);
        assertEquals(negatedRange, negatedRange2);

        // Test same content, different representation (should NOT be equal)
        assertNotEquals(individualChars, rangeChars);
        
        // Test different content
        assertNotEquals(individualChars, negatedRange);
        assertNotEquals(rangeChars, negatedRange);
    }

    @Test
    void shouldHaveConsistentHashCodes() {
        CharSet set1 = CharSet.getInstance("abc");
        CharSet set2 = CharSet.getInstance("abc");
        CharSet set3 = CharSet.getInstance("a-c");
        
        // Equal objects should have equal hash codes
        assertEquals(set1.hashCode(), set1.hashCode());
        assertEquals(set1.hashCode(), set2.hashCode());
        
        // Different representations may have different hash codes
        assertEquals(set3.hashCode(), set3.hashCode());
    }

    // ========== Documentation Examples Tests ==========
    
    @Test
    void shouldMatchJavadocExamples() {
        // Test examples from the class javadoc
        assertFalse(CharSet.getInstance("^a-c").contains('a'));      // negated range excludes 'a'
        assertTrue(CharSet.getInstance("^a-c").contains('d'));       // negated range includes 'd'
        assertTrue(CharSet.getInstance("^^a-c").contains('a'));      // double negation includes 'a'
        assertFalse(CharSet.getInstance("^^a-c").contains('^'));     // double negation excludes '^'
        assertTrue(CharSet.getInstance("^a-cd-f").contains('d'));    // 'd' is in non-negated part
        assertTrue(CharSet.getInstance("a-c^").contains('^'));       // '^' at end is literal
        assertTrue(CharSet.getInstance("^", "a-c").contains('^'));   // separate '^' argument
    }

    // ========== Serialization Tests ==========
    
    @Test
    void shouldSerializeAndDeserializeCorrectly() {
        CharSet simpleSet = CharSet.getInstance("a");
        assertEquals(simpleSet, SerializationUtils.clone(simpleSet));
        
        CharSet rangeSet = CharSet.getInstance("a-e");
        assertEquals(rangeSet, SerializationUtils.clone(rangeSet));
        
        CharSet complexSet = CharSet.getInstance("be-f^a-z");
        assertEquals(complexSet, SerializationUtils.clone(complexSet));
    }

    // ========== Helper Methods ==========
    
    /**
     * Asserts that the given array contains the specified CharRange.
     * This helper method improves readability of range verification tests.
     */
    private void assertContainsCharRange(CharRange[] ranges, CharRange expectedRange) {
        assertTrue(ArrayUtils.contains(ranges, expectedRange),
                "Expected ranges to contain: " + expectedRange + 
                " but found: " + java.util.Arrays.toString(ranges));
    }
}
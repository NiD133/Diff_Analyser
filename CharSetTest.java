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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharSet}.
 */
class CharSetTest extends AbstractLangTest {

    @Test
    @DisplayName("CharSet class should be public and not final")
    void testClass() {
        assertTrue(Modifier.isPublic(CharSet.class.getModifiers()), "CharSet should be public");
        assertFalse(Modifier.isFinal(CharSet.class.getModifiers()), "CharSet should not be final");
    }

    @Nested
    @DisplayName("Tests for getInstance(String)")
    class GetInstanceStringTests {

        @Test
        @DisplayName("Test with simple character combinations")
        void testSimpleCharacterCombinations() {
            CharSet set = CharSet.getInstance("abc");
            CharRange[] array = set.getCharRanges();
            assertEquals(3, array.length, "CharSet 'abc' should contain 3 CharRanges");
            assertTrue(ArrayUtils.contains(array, CharRange.is('a')), "CharSet should contain 'a'");
            assertTrue(ArrayUtils.contains(array, CharRange.is('b')), "CharSet should contain 'b'");
            assertTrue(ArrayUtils.contains(array, CharRange.is('c')), "CharSet should contain 'c'");
        }

        @Test
        @DisplayName("Test with range character combinations")
        void testRangeCharacterCombinations() {
            CharSet set = CharSet.getInstance("a-ce-f");
            CharRange[] array = set.getCharRanges();
            assertEquals(2, array.length, "CharSet 'a-ce-f' should contain 2 CharRanges");
            assertTrue(ArrayUtils.contains(array, CharRange.isIn('a', 'c')), "CharSet should contain range 'a-c'");
            assertTrue(ArrayUtils.contains(array, CharRange.isIn('e', 'f')), "CharSet should contain range 'e-f'");
        }

        @Test
        @DisplayName("Test with mixed character and range combinations")
        void testMixedCharacterAndRangeCombinations() {
            CharSet set = CharSet.getInstance("ae-f");
            CharRange[] array = set.getCharRanges();
            assertEquals(2, array.length, "CharSet 'ae-f' should contain 2 CharRanges");
            assertTrue(ArrayUtils.contains(array, CharRange.is('a')), "CharSet should contain 'a'");
            assertTrue(ArrayUtils.contains(array, CharRange.isIn('e', 'f')), "CharSet should contain range 'e-f'");

            set = CharSet.getInstance("e-fa");
            array = set.getCharRanges();
            assertEquals(2, array.length, "CharSet 'e-fa' should contain 2 CharRanges");
            assertTrue(ArrayUtils.contains(array, CharRange.is('a')), "CharSet should contain 'a'");
            assertTrue(ArrayUtils.contains(array, CharRange.isIn('e', 'f')), "CharSet should contain range 'e-f'");

            set = CharSet.getInstance("ae-fm-pz");
            array = set.getCharRanges();
            assertEquals(4, array.length, "CharSet 'ae-fm-pz' should contain 4 CharRanges");
            assertTrue(ArrayUtils.contains(array, CharRange.is('a')), "CharSet should contain 'a'");
            assertTrue(ArrayUtils.contains(array, CharRange.isIn('e', 'f')), "CharSet should contain range 'e-f'");
            assertTrue(ArrayUtils.contains(array, CharRange.isIn('m', 'p')), "CharSet should contain range 'm-p'");
            assertTrue(ArrayUtils.contains(array, CharRange.is('z')), "CharSet should contain 'z'");
        }
    }

    @Nested
    @DisplayName("Tests for getInstance(String) with negated characters")
    class GetInstanceStringNegatedTests {

        @Test
        @DisplayName("Test with simple negated character combinations")
        void testSimpleNegatedCharacterCombinations() {
            CharSet set = CharSet.getInstance("^abc");
            CharRange[] array = set.getCharRanges();
            assertEquals(3, array.length, "CharSet '^abc' should contain 3 CharRanges");
            assertTrue(ArrayUtils.contains(array, CharRange.isNot('a')), "CharSet should contain not 'a'");
            assertTrue(ArrayUtils.contains(array, CharRange.is('b')), "CharSet should contain 'b'");
            assertTrue(ArrayUtils.contains(array, CharRange.is('c')), "CharSet should contain 'c'");
        }

        @Test
        @DisplayName("Test with mixed negated character combinations")
        void testMixedNegatedCharacterCombinations() {
            CharSet set = CharSet.getInstance("b^ac");
            CharRange[] array = set.getCharRanges();
            assertEquals(3, array.length, "CharSet 'b^ac' should contain 3 CharRanges");
            assertTrue(ArrayUtils.contains(array, CharRange.is('b')), "CharSet should contain 'b'");
            assertTrue(ArrayUtils.contains(array, CharRange.isNot('a')), "CharSet should contain not 'a'");
            assertTrue(ArrayUtils.contains(array, CharRange.is('c')), "CharSet should contain 'c'");

            set = CharSet.getInstance("db^ac");
            array = set.getCharRanges();
            assertEquals(4, array.length, "CharSet 'db^ac' should contain 4 CharRanges");
            assertTrue(ArrayUtils.contains(array, CharRange.is('d')), "CharSet should contain 'd'");
            assertTrue(ArrayUtils.contains(array, CharRange.is('b')), "CharSet should contain 'b'");
            assertTrue(ArrayUtils.contains(array, CharRange.isNot('a')), "CharSet should contain not 'a'");
            assertTrue(ArrayUtils.contains(array, CharRange.is('c')), "CharSet should contain 'c'");

            set = CharSet.getInstance("^b^a");
            array = set.getCharRanges();
            assertEquals(2, array.length, "CharSet '^b^a' should contain 2 CharRanges");
            assertTrue(ArrayUtils.contains(array, CharRange.isNot('b')), "CharSet should contain not 'b'");
            assertTrue(ArrayUtils.contains(array, CharRange.isNot('a')), "CharSet should contain not 'a'");

            set = CharSet.getInstance("b^a-c^z");
            array = set.getCharRanges();
            assertEquals(3, array.length, "CharSet 'b^a-c^z' should contain 3 CharRanges");
            assertTrue(ArrayUtils.contains(array, CharRange.isNotIn('a', 'c')), "CharSet should contain not in range 'a-c'");
            assertTrue(ArrayUtils.contains(array, CharRange.isNot('z')), "CharSet should contain not 'z'");
            assertTrue(ArrayUtils.contains(array, CharRange.is('b')), "CharSet should contain 'b'");
        }
    }

    @Nested
    @DisplayName("Tests for getInstance(String) with odd combinations")
    class GetInstanceStringOddCombinationsTests {

        @Test
        @DisplayName("Test with range ending with negate")
        void testRangeEndingWithNegate() {
            CharSet set = CharSet.getInstance("a-^c");
            CharRange[] array = set.getCharRanges();
            assertTrue(ArrayUtils.contains(array, CharRange.isIn('a', '^')), "CharSet should contain range 'a-^'");
            assertTrue(ArrayUtils.contains(array, CharRange.is('c')), "CharSet should contain 'c'");
            assertFalse(set.contains('b'), "CharSet should not contain 'b'");
            assertTrue(set.contains('^'), "CharSet should contain '^'");
            assertTrue(set.contains('_'), "CharSet should contain '_'");
            assertTrue(set.contains('c'), "CharSet should contain 'c'");
        }

        @Test
        @DisplayName("Test with negate range ending with negate")
        void testNegateRangeEndingWithNegate() {
            CharSet set = CharSet.getInstance("^a-^c");
            CharRange[] array = set.getCharRanges();
            assertTrue(ArrayUtils.contains(array, CharRange.isNotIn('a', '^')), "CharSet should contain not in range 'a-^'");
            assertTrue(ArrayUtils.contains(array, CharRange.is('c')), "CharSet should contain 'c'");
            assertTrue(set.contains('b'), "CharSet should contain 'b'");
            assertFalse(set.contains('^'), "CharSet should not contain '^'");
            assertFalse(set.contains('_'), "CharSet should not contain '_'");
        }

        @Test
        @DisplayName("Test with range containing space")
        void testRangeContainingSpace() {
            CharSet set = CharSet.getInstance("a- ^-- "); //contains everything
            CharRange[] array = set.getCharRanges();
            assertTrue(ArrayUtils.contains(array, CharRange.isIn('a', ' ')), "CharSet should contain range 'a- '");
            assertTrue(ArrayUtils.contains(array, CharRange.isNotIn('-', ' ')), "CharSet should contain not in range '- '");
            assertTrue(set.contains('#'), "CharSet should contain '#'");
            assertTrue(set.contains('^'), "CharSet should contain '^'");
            assertTrue(set.contains('a'), "CharSet should contain 'a'");
            assertTrue(set.contains('*'), "CharSet should contain '*'");
            assertTrue(set.contains('A'), "CharSet should contain 'A'");
        }

        @Test
        @DisplayName("Test with range starting with negate")
        void testRangeStartingWithNegate() {
            CharSet set = CharSet.getInstance("^-b");
            CharRange[] array = set.getCharRanges();
            assertTrue(ArrayUtils.contains(array, CharRange.isIn('^', 'b')), "CharSet should contain range '^-b'");
            assertTrue(set.contains('b'), "CharSet should contain 'b'");
            assertTrue(set.contains('_'), "CharSet should contain '_'");
            assertFalse(set.contains('A'), "CharSet should not contain 'A'");
            assertTrue(set.contains('^'), "CharSet should contain '^'");
        }

        @Test
        @DisplayName("Test with range ending with negate 2")
        void testRangeEndingWithNegate2() {
            CharSet set = CharSet.getInstance("b-^");
            CharRange[] array = set.getCharRanges();
            assertTrue(ArrayUtils.contains(array, CharRange.isIn('^', 'b')), "CharSet should contain range 'b-^'");
            assertTrue(set.contains('b'), "CharSet should contain 'b'");
            assertTrue(set.contains('^'), "CharSet should contain '^'");
            assertTrue(set.contains('a'), "CharSet should contain 'a'");
            assertFalse(set.contains('c'), "CharSet should not contain 'c'");
        }
    }

    @Nested
    @DisplayName("Tests for getInstance(String) with odd dash handling")
    class GetInstanceStringOddDashTests {

        @Test
        @DisplayName("Test with single dash")
        void testSingleDash() {
            CharSet set = CharSet.getInstance("-");
            CharRange[] array = set.getCharRanges();
            assertEquals(1, array.length, "CharSet '-' should contain 1 CharRange");
            assertTrue(ArrayUtils.contains(array, CharRange.is('-')), "CharSet should contain '-'");
        }

        @Test
        @DisplayName("Test with multiple dashes")
        void testMultipleDashes() {
            CharSet set = CharSet.getInstance("--");
            CharRange[] array = set.getCharRanges();
            assertEquals(1, array.length, "CharSet '--' should contain 1 CharRange");
            assertTrue(ArrayUtils.contains(array, CharRange.is('-')), "CharSet should contain '-'");

            set = CharSet.getInstance("---");
            array = set.getCharRanges();
            assertEquals(1, array.length, "CharSet '---' should contain 1 CharRange");
            assertTrue(ArrayUtils.contains(array, CharRange.is('-')), "CharSet should contain '-'");

            set = CharSet.getInstance("----");
            array = set.getCharRanges();
            assertEquals(1, array.length, "CharSet '----' should contain 1 CharRange");
            assertTrue(ArrayUtils.contains(array, CharRange.is('-')), "CharSet should contain '-'");
        }

        @Test
        @DisplayName("Test with dash and character")
        void testDashAndCharacter() {
            CharSet set = CharSet.getInstance("-a");
            CharRange[] array = set.getCharRanges();
            assertEquals(2, array.length, "CharSet '-a' should contain 2 CharRanges");
            assertTrue(ArrayUtils.contains(array, CharRange.is('-')), "CharSet should contain '-'");
            assertTrue(ArrayUtils.contains(array, CharRange.is('a')), "CharSet should contain 'a'");

            set = CharSet.getInstance("a-");
            array = set.getCharRanges();
            assertEquals(2, array.length, "CharSet 'a-' should contain 2 CharRanges");
            assertTrue(ArrayUtils.contains(array, CharRange.is('a')), "CharSet should contain 'a'");
            assertTrue(ArrayUtils.contains(array, CharRange.is('-')), "CharSet should contain '-'");

            set = CharSet.getInstance("a--");
            array = set.getCharRanges();
            assertEquals(1, array.length, "CharSet 'a--' should contain 1 CharRange");
            assertTrue(ArrayUtils.contains(array, CharRange.isIn('a', '-')), "CharSet should contain range 'a--'");

            set = CharSet.getInstance("--a");
            array = set.getCharRanges();
            assertEquals(1, array.length, "CharSet '--a' should contain 1 CharRange");
            assertTrue(ArrayUtils.contains(array, CharRange.isIn('-', 'a')), "CharSet should contain range '--a'");
        }
    }

    @Nested
    @DisplayName("Tests for getInstance(String) with odd negate handling")
    class GetInstanceStringOddNegateTests {

        @Test
        @DisplayName("Test with single negate")
        void testSingleNegate() {
            CharSet set = CharSet.getInstance("^");
            CharRange[] array = set.getCharRanges();
            assertEquals(1, array.length, "CharSet '^' should contain 1 CharRange");
            assertTrue(ArrayUtils.contains(array, CharRange.is('^')), "CharSet should contain '^'");
        }

        @Test
        @DisplayName("Test with multiple negates")
        void testMultipleNegates() {
            CharSet set = CharSet.getInstance("^^");
            CharRange[] array = set.getCharRanges();
            assertEquals(1, array.length, "CharSet '^^' should contain 1 CharRange");
            assertTrue(ArrayUtils.contains(array, CharRange.isNot('^')), "CharSet should contain not '^'");

            set = CharSet.getInstance("^^^");
            array = set.getCharRanges();
            assertEquals(2, array.length, "CharSet '^^^' should contain 2 CharRanges");
            assertTrue(ArrayUtils.contains(array, CharRange.isNot('^')), "CharSet should contain not '^'");
            assertTrue(ArrayUtils.contains(array, CharRange.is('^')), "CharSet should contain '^'");

            set = CharSet.getInstance("^^^^");
            array = set.getCharRanges();
            assertEquals(1, array.length, "CharSet '^^^^' should contain 1 CharRange");
            assertTrue(ArrayUtils.contains(array, CharRange.isNot('^')), "CharSet should contain not '^'");
        }

        @Test
        @DisplayName("Test with character and negate")
        void testCharacterAndNegate() {
            CharSet set = CharSet.getInstance("a^");
            CharRange[] array = set.getCharRanges();
            assertEquals(2, array.length, "CharSet 'a^' should contain 2 CharRanges");
            assertTrue(ArrayUtils.contains(array, CharRange.is('a')), "CharSet should contain 'a'");
            assertTrue(ArrayUtils.contains(array, CharRange.is('^')), "CharSet should contain '^'");

            set = CharSet.getInstance("^a-");
            array = set.getCharRanges();
            assertEquals(2, array.length, "CharSet '^a-' should contain 2 CharRanges");
            assertTrue(ArrayUtils.contains(array, CharRange.isNot('a')), "CharSet should contain not 'a'");
            assertTrue(ArrayUtils.contains(array, CharRange.is('-')), "CharSet should contain '-'");

            set = CharSet.getInstance("^-c");
            array = set.getCharRanges();
            assertEquals(1, array.length, "CharSet '^-c' should contain 1 CharRange");
            assertTrue(ArrayUtils.contains(array, CharRange.isNotIn('^', 'c')), "CharSet should contain not in range '^c'");

            set = CharSet.getInstance("^c-^");
            array = set.getCharRanges();
            assertEquals(1, array.length, "CharSet '^c-^' should contain 1 CharRange");
            assertTrue(ArrayUtils.contains(array, CharRange.isNotIn('c', '^')), "CharSet should contain not in range 'c^'");

            set = CharSet.getInstance("^c-^d");
            array = set.getCharRanges();
            assertEquals(2, array.length, "CharSet '^c-^d' should contain 2 CharRanges");
            assertTrue(ArrayUtils.contains(array, CharRange.isNotIn('c', '^')), "CharSet should contain not in range 'c^'");
            assertTrue(ArrayUtils.contains(array, CharRange.is('d')), "CharSet should contain 'd'");

            set = CharSet.getInstance("^-");
            array = set.getCharRanges();
            assertEquals(2, array.length, "CharSet '^-' should contain 2 CharRanges");
            assertTrue(ArrayUtils.contains(array, CharRange.isNot('^')), "CharSet should contain not '^'");
            assertTrue(ArrayUtils.contains(array, CharRange.is('-')), "CharSet should contain '-'");
        }
    }

    @Nested
    @DisplayName("Tests for getInstance(String) with simple cases")
    class GetInstanceStringSimpleTests {

        @Test
        @DisplayName("Test with null or empty string")
        void testNullOrEmptyString() {
            CharSet set = CharSet.getInstance((String) null);
            CharRange[] array = set.getCharRanges();
            assertEquals("[]", set.toString(), "CharSet should be empty");
            assertEquals(0, array.length, "CharSet should have 0 CharRanges");

            set = CharSet.getInstance("");
            array = set.getCharRanges();
            assertEquals("[]", set.toString(), "CharSet should be empty");
            assertEquals(0, array.length, "CharSet should have 0 CharRanges");
        }

        @Test
        @DisplayName("Test with single character")
        void testSingleCharacter() {
            CharSet set = CharSet.getInstance("a");
            CharRange[] array = set.getCharRanges();
            assertEquals("[a]", set.toString(), "CharSet should contain 'a'");
            assertEquals(1, array.length, "CharSet should have 1 CharRange");
            assertEquals("a", array[0].toString(), "CharRange should be 'a'");
        }

        @Test
        @DisplayName("Test with negated single character")
        void testNegatedSingleCharacter() {
            CharSet set = CharSet.getInstance("^a");
            CharRange[] array = set.getCharRanges();
            assertEquals("[^a]", set.toString(), "CharSet should contain not 'a'");
            assertEquals(1, array.length, "CharSet should have 1 CharRange");
            assertEquals("^a", array[0].toString(), "CharRange should be '^a'");
        }

        @Test
        @DisplayName("Test with simple range")
        void testSimpleRange() {
            CharSet set = CharSet.getInstance("a-e");
            CharRange[] array = set.getCharRanges();
            assertEquals("[a-e]", set.toString(), "CharSet should contain range 'a-e'");
            assertEquals(1, array.length, "CharSet should have 1 CharRange");
            assertEquals("a-e", array[0].toString(), "CharRange should be 'a-e'");
        }

        @Test
        @DisplayName("Test with negated simple range")
        void testNegatedSimpleRange() {
            CharSet set = CharSet.getInstance("^a-e");
            CharRange[] array = set.getCharRanges();
            assertEquals("[^a-e]", set.toString(), "CharSet should contain not range 'a-e'");
            assertEquals(1, array.length, "CharSet should have 1 CharRange");
            assertEquals("^a-e", array[0].toString(), "CharRange should be '^a-e'");
        }
    }

    @Test
    @DisplayName("Test contains(char)")
    void testContains_Char() {
        final CharSet btod = CharSet.getInstance("b-d");
        final CharSet dtob = CharSet.getInstance("d-b");
        final CharSet bcd = CharSet.getInstance("bcd");
        final CharSet bd = CharSet.getInstance("bd");
        final CharSet notbtod = CharSet.getInstance("^b-d");

        assertFalse(btod.contains('a'), "b-d should not contain a");
        assertTrue(btod.contains('b'), "b-d should contain b");
        assertTrue(btod.contains('c'), "b-d should contain c");
        assertTrue(btod.contains('d'), "b-d should contain d");
        assertFalse(btod.contains('e'), "b-d should not contain e");

        assertFalse(bcd.contains('a'), "bcd should not contain a");
        assertTrue(bcd.contains('b'), "bcd should contain b");
        assertTrue(bcd.contains('c'), "bcd should contain c");
        assertTrue(bcd.contains('d'), "bcd should contain d");
        assertFalse(bcd.contains('e'), "bcd should not contain e");

        assertFalse(bd.contains('a'), "bd should not contain a");
        assertTrue(bd.contains('b'), "bd should contain b");
        assertFalse(bd.contains('c'), "bd should not contain c");
        assertTrue(bd.contains('d'), "bd should contain d");
        assertFalse(bd.contains('e'), "bd should not contain e");

        assertTrue(notbtod.contains('a'), "^b-d should contain a");
        assertFalse(notbtod.contains('b'), "^b-d should not contain b");
        assertFalse(notbtod.contains('c'), "^b-d should not contain c");
        assertFalse(notbtod.contains('d'), "^b-d should not contain d");
        assertTrue(notbtod.contains('e'), "^b-d should contain e");

        assertFalse(dtob.contains('a'), "d-b should not contain a");
        assertTrue(dtob.contains('b'), "d-b should contain b");
        assertTrue(dtob.contains('c'), "d-b should contain c");
        assertTrue(dtob.contains('d'), "d-b should contain d");
        assertFalse(dtob.contains('e'), "d-b should not contain e");

        final CharRange[] array = dtob.getCharRanges();
        assertEquals("[b-d]", dtob.toString(), "d-b should be equivalent to b-d");
        assertEquals(1, array.length, "d-b should contain 1 CharRange");
    }

    @Test
    @DisplayName("Test equals(Object)")
    void testEquals_Object() {
        final CharSet abc = CharSet.getInstance("abc");
        final CharSet abc2 = CharSet.getInstance("abc");
        final CharSet atoc = CharSet.getInstance("a-c");
        final CharSet atoc2 = CharSet.getInstance("a-c");
        final CharSet notatoc = CharSet.getInstance("^a-c");
        final CharSet notatoc2 = CharSet.getInstance("^a-c");

        assertNotEquals(null, abc, "CharSet should not be equal to null");

        assertEquals(abc, abc, "CharSet should be equal to itself");
        assertEquals(abc, abc2, "CharSets with same characters should be equal");
        assertNotEquals(abc, atoc, "CharSets with different definitions should not be equal");
        assertNotEquals(abc, notatoc, "CharSets with different definitions should not be equal");

        assertNotEquals(atoc, abc, "CharSets with different definitions should not be equal");
        assertEquals(atoc, atoc, "CharSet should be equal to itself");
        assertEquals(atoc, atoc2, "CharSets with same characters should be equal");
        assertNotEquals(atoc, notatoc, "CharSets with different definitions should not be equal");

        assertNotEquals(notatoc, abc, "CharSets with different definitions should not be equal");
        assertNotEquals(notatoc, atoc, "CharSets with different definitions should not be equal");
        assertEquals(notatoc, notatoc, "CharSet should be equal to itself");
        assertEquals(notatoc, notatoc2, "CharSets with same characters should be equal");
    }

    @Test
    @DisplayName("Test getInstance() with predefined character sets")
    void testGetInstance() {
        assertSame(CharSet.EMPTY, CharSet.getInstance((String) null), "Should return EMPTY for null input");
        assertSame(CharSet.EMPTY, CharSet.getInstance(""), "Should return EMPTY for empty string");
        assertSame(CharSet.ASCII_ALPHA, CharSet.getInstance("a-zA-Z"), "Should return ASCII_ALPHA");
        assertSame(CharSet.ASCII_ALPHA, CharSet.getInstance("A-Za-z"), "Should return ASCII_ALPHA");
        assertSame(CharSet.ASCII_ALPHA_LOWER, CharSet.getInstance("a-z"), "Should return ASCII_ALPHA_LOWER");
        assertSame(CharSet.ASCII_ALPHA_UPPER, CharSet.getInstance("A-Z"), "Should return ASCII_ALPHA_UPPER");
        assertSame(CharSet.ASCII_NUMERIC, CharSet.getInstance("0-9"), "Should return ASCII_NUMERIC");
    }

    @Test
    @DisplayName("Test getInstance(String[])")
    void testGetInstance_Stringarray() {
        assertNull(CharSet.getInstance((String[]) null), "Should return null for null input");
        assertEquals("[]", CharSet.getInstance().toString(), "Should return empty CharSet");
        assertEquals("[]", CharSet.getInstance(new String[] {null}).toString(), "Should return empty CharSet");
        assertEquals("[a-e]", CharSet.getInstance("a-e").toString(), "Should return CharSet with a-e");
    }

    @Test
    @DisplayName("Test hashCode()")
    void testHashCode() {
        final CharSet abc = CharSet.getInstance("abc");
        final CharSet abc2 = CharSet.getInstance("abc");
        final CharSet atoc = CharSet.getInstance("a-c");
        final CharSet atoc2 = CharSet.getInstance("a-c");
        final CharSet notatoc = CharSet.getInstance("^a-c");
        final CharSet notatoc2 = CharSet.getInstance("^a-c");

        assertEquals(abc.hashCode(), abc.hashCode(), "HashCode should be consistent");
        assertEquals(abc.hashCode(), abc2.hashCode(), "HashCodes of equal CharSets should be equal");
        assertEquals(atoc.hashCode(), atoc.hashCode(), "HashCode should be consistent");
        assertEquals(atoc.hashCode(), atoc2.hashCode(), "HashCodes of equal CharSets should be equal");
        assertEquals(notatoc.hashCode(), notatoc.hashCode(), "HashCode should be consistent");
        assertEquals(notatoc.hashCode(), notatoc2.hashCode(), "HashCodes of equal CharSets should be equal");
    }

    @Test
    @DisplayName("Test Javadoc examples")
    void testJavadocExamples() {
        assertFalse(CharSet.getInstance("^a-c").contains('a'), "^a-c should not contain a");
        assertTrue(CharSet.getInstance("^a-c").contains('d'), "^a-c should contain d");
        assertTrue(CharSet.getInstance("^^a-c").contains('a'), "^^a-c should contain a");
        assertFalse(CharSet.getInstance("^^a-c").contains('^'), "^^a-c should not contain ^");
        assertTrue(CharSet.getInstance("^a-cd-f").contains('d'), "^a-cd-f should contain d");
        assertTrue(CharSet.getInstance("a-c^").contains('^'), "a-c^ should contain ^");
        assertTrue(CharSet.getInstance("^", "a-c").contains('^'), "^, a-c should contain ^");
    }

    @Test
    @DisplayName("Test serialization")
    void testSerialization() {
        CharSet set = CharSet.getInstance("a");
        assertEquals(set, SerializationUtils.clone(set), "Serialized CharSet should be equal to original");
        set = CharSet.getInstance("a-e");
        assertEquals(set, SerializationUtils.clone(set), "Serialized CharSet should be equal to original");
        set = CharSet.getInstance("be-f^a-z");
        assertEquals(set, SerializationUtils.clone(set), "Serialized CharSet should be equal to original");
    }

    @Test
    @DisplayName("Test statics")
    void testStatics() {
        CharRange[] array;

        array = CharSet.EMPTY.getCharRanges();
        assertEquals(0, array.length, "EMPTY CharSet should have 0 CharRanges");

        array = CharSet.ASCII_ALPHA.getCharRanges();
        assertEquals(2, array.length, "ASCII_ALPHA CharSet should have 2 CharRanges");
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('a', 'z')), "ASCII_ALPHA should contain a-z");
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('A', 'Z')), "ASCII_ALPHA should contain A-Z");

        array = CharSet.ASCII_ALPHA_LOWER.getCharRanges();
        assertEquals(1, array.length, "ASCII_ALPHA_LOWER CharSet should have 1 CharRange");
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('a', 'z')), "ASCII_ALPHA_LOWER should contain a-z");

        array = CharSet.ASCII_ALPHA_UPPER.getCharRanges();
        assertEquals(1, array.length, "ASCII_ALPHA_UPPER CharSet should have 1 CharRange");
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('A', 'Z')), "ASCII_ALPHA_UPPER should contain A-Z");

        array = CharSet.ASCII_NUMERIC.getCharRanges();
        assertEquals(1, array.length, "ASCII_NUMERIC CharSet should have 1 CharRange");
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('0', '9')), "ASCII_NUMERIC should contain 0-9");
    }
}
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
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.  See the License for the specific
 * language governing permissions and limitations
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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link CharRange}.
 */
@DisplayName("CharRange Tests")
class CharRangeTest extends AbstractLangTest {

    @Test
    @DisplayName("CharRange class should be non-public and final")
    void testClass() {
        assertFalse(Modifier.isPublic(CharRange.class.getModifiers()), "CharRange should not be public");
        assertTrue(Modifier.isFinal(CharRange.class.getModifiers()), "CharRange should be final");
    }

    @Nested
    @DisplayName("Constructor and Accessor Tests")
    class ConstructorAccessorTests {
        @Test
        @DisplayName("Test CharRange.is(char)")
        void testConstructorAccessors_is() {
            final CharRange rangea = CharRange.is('a');
            assertEquals('a', rangea.getStart(), "Start should be 'a'");
            assertEquals('a', rangea.getEnd(), "End should be 'a'");
            assertFalse(rangea.isNegated(), "Range should not be negated");
            assertEquals("a", rangea.toString(), "toString() should return 'a'");
        }

        @Test
        @DisplayName("Test CharRange.isIn(char, char) - Normal Order")
        void testConstructorAccessors_isIn_Normal() {
            final CharRange rangea = CharRange.isIn('a', 'e');
            assertEquals('a', rangea.getStart(), "Start should be 'a'");
            assertEquals('e', rangea.getEnd(), "End should be 'e'");
            assertFalse(rangea.isNegated(), "Range should not be negated");
            assertEquals("a-e", rangea.toString(), "toString() should return 'a-e'");
        }

        @Test
        @DisplayName("Test CharRange.isIn(char, char) - Reversed Order")
        void testConstructorAccessors_isIn_Reversed() {
            final CharRange rangea = CharRange.isIn('e', 'a');
            assertEquals('a', rangea.getStart(), "Start should be 'a'");
            assertEquals('e', rangea.getEnd(), "End should be 'e'");
            assertFalse(rangea.isNegated(), "Range should not be negated");
            assertEquals("a-e", rangea.toString(), "toString() should return 'a-e'");
        }

        @Test
        @DisplayName("Test CharRange.isIn(char, char) - Same Characters")
        void testConstructorAccessors_isIn_Same() {
            final CharRange rangea = CharRange.isIn('a', 'a');
            assertEquals('a', rangea.getStart(), "Start should be 'a'");
            assertEquals('a', rangea.getEnd(), "End should be 'a'");
            assertFalse(rangea.isNegated(), "Range should not be negated");
            assertEquals("a", rangea.toString(), "toString() should return 'a'");
        }

        @Test
        @DisplayName("Test CharRange.isNot(char)")
        void testConstructorAccessors_isNot() {
            final CharRange rangea = CharRange.isNot('a');
            assertEquals('a', rangea.getStart(), "Start should be 'a'");
            assertEquals('a', rangea.getEnd(), "End should be 'a'");
            assertTrue(rangea.isNegated(), "Range should be negated");
            assertEquals("^a", rangea.toString(), "toString() should return '^a'");
        }

        @Test
        @DisplayName("Test CharRange.isNotIn(char, char) - Normal Order")
        void testConstructorAccessors_isNotIn_Normal() {
            final CharRange rangea = CharRange.isNotIn('a', 'e');
            assertEquals('a', rangea.getStart(), "Start should be 'a'");
            assertEquals('e', rangea.getEnd(), "End should be 'e'");
            assertTrue(rangea.isNegated(), "Range should be negated");
            assertEquals("^a-e", rangea.toString(), "toString() should return '^a-e'");
        }

        @Test
        @DisplayName("Test CharRange.isNotIn(char, char) - Reversed Order")
        void testConstructorAccessors_isNotIn_Reversed() {
            final CharRange rangea = CharRange.isNotIn('e', 'a');
            assertEquals('a', rangea.getStart(), "Start should be 'a'");
            assertEquals('e', rangea.getEnd(), "End should be 'e'");
            assertTrue(rangea.isNegated(), "Range should be negated");
            assertEquals("^a-e", rangea.toString(), "toString() should return '^a-e'");
        }

        @Test
        @DisplayName("Test CharRange.isNotIn(char, char) - Same Characters")
        void testConstructorAccessors_isNotIn_Same() {
            final CharRange rangea = CharRange.isNotIn('a', 'a');
            assertEquals('a', rangea.getStart(), "Start should be 'a'");
            assertEquals('a', rangea.getEnd(), "End should be 'a'");
            assertTrue(rangea.isNegated(), "Range should be negated");
            assertEquals("^a", rangea.toString(), "toString() should return '^a'");
        }
    }


    @Nested
    @DisplayName("Contains Tests")
    class ContainsTests {

        @Test
        @DisplayName("Test contains(char) method")
        void testContains_Char() {
            CharRange range = CharRange.is('c');
            assertFalse(range.contains('b'), "'c' range should not contain 'b'");
            assertTrue(range.contains('c'), "'c' range should contain 'c'");
            assertFalse(range.contains('d'), "'c' range should not contain 'd'");
            assertFalse(range.contains('e'), "'c' range should not contain 'e'");

            range = CharRange.isIn('c', 'd');
            assertFalse(range.contains('b'), "'c-d' range should not contain 'b'");
            assertTrue(range.contains('c'), "'c-d' range should contain 'c'");
            assertTrue(range.contains('d'), "'c-d' range should contain 'd'");
            assertFalse(range.contains('e'), "'c-d' range should not contain 'e'");

            range = CharRange.isIn('d', 'c'); // reversed order
            assertFalse(range.contains('b'), "'d-c' range should not contain 'b'");
            assertTrue(range.contains('c'), "'d-c' range should contain 'c'");
            assertTrue(range.contains('d'), "'d-c' range should contain 'd'");
            assertFalse(range.contains('e'), "'d-c' range should not contain 'e'");

            range = CharRange.isNotIn('c', 'd');
            assertTrue(range.contains('b'), "'^c-d' range should contain 'b'");
            assertFalse(range.contains('c'), "'^c-d' range should not contain 'c'");
            assertFalse(range.contains('d'), "'^c-d' range should not contain 'd'");
            assertTrue(range.contains('e'), "'^c-d' range should contain 'e'");
            assertTrue(range.contains((char) 0), "'^c-d' range should contain 0");
            assertTrue(range.contains(Character.MAX_VALUE), "'^c-d' range should contain MAX_VALUE");
        }

        @Test
        @DisplayName("Test contains(CharRange) method")
        void testContains_Charrange() {
            final CharRange a = CharRange.is('a');
            final CharRange b = CharRange.is('b');
            final CharRange c = CharRange.is('c');
            final CharRange c2 = CharRange.is('c');
            final CharRange d = CharRange.is('d');
            final CharRange e = CharRange.is('e');
            final CharRange cd = CharRange.isIn('c', 'd');
            final CharRange bd = CharRange.isIn('b', 'd');
            final CharRange bc = CharRange.isIn('b', 'c');
            final CharRange ab = CharRange.isIn('a', 'b');
            final CharRange de = CharRange.isIn('d', 'e');
            final CharRange ef = CharRange.isIn('e', 'f');
            final CharRange ae = CharRange.isIn('a', 'e');

            // normal/normal
            assertFalse(c.contains(b), "'c' should not contain 'b'");
            assertTrue(c.contains(c), "'c' should contain 'c'");
            assertTrue(c.contains(c2), "'c' should contain 'c2'");
            assertFalse(c.contains(d), "'c' should not contain 'd'");

            assertFalse(c.contains(cd), "'c' should not contain 'cd'");
            assertFalse(c.contains(bd), "'c' should not contain 'bd'");
            assertFalse(c.contains(bc), "'c' should not contain 'bc'");
            assertFalse(c.contains(ab), "'c' should not contain 'ab'");
            assertFalse(c.contains(de), "'c' should not contain 'de'");

            assertTrue(cd.contains(c), "'cd' should contain 'c'");
            assertTrue(bd.contains(c), "'bd' should contain 'c'");
            assertTrue(bc.contains(c), "'bc' should contain 'c'");
            assertFalse(ab.contains(c), "'ab' should not contain 'c'");
            assertFalse(de.contains(c), "'de' should not contain 'c'");

            assertTrue(ae.contains(b), "'ae' should contain 'b'");
            assertTrue(ae.contains(ab), "'ae' should contain 'ab'");
            assertTrue(ae.contains(bc), "'ae' should contain 'bc'");
            assertTrue(ae.contains(cd), "'ae' should contain 'cd'");
            assertTrue(ae.contains(de), "'ae' should contain 'de'");

            final CharRange notb = CharRange.isNot('b');
            final CharRange notc = CharRange.isNot('c');
            final CharRange notd = CharRange.isNot('d');
            final CharRange notab = CharRange.isNotIn('a', 'b');
            final CharRange notbc = CharRange.isNotIn('b', 'c');
            final CharRange notbd = CharRange.isNotIn('b', 'd');
            final CharRange notcd = CharRange.isNotIn('c', 'd');
            final CharRange notde = CharRange.isNotIn('d', 'e');
            final CharRange notae = CharRange.isNotIn('a', 'e');
            final CharRange all = CharRange.isIn((char) 0, Character.MAX_VALUE);
            final CharRange allbutfirst = CharRange.isIn((char) 1, Character.MAX_VALUE);

            // normal/negated
            assertFalse(c.contains(notc), "'c' should not contain 'notc'");
            assertFalse(c.contains(notbd), "'c' should not contain 'notbd'");
            assertTrue(all.contains(notc), "'all' should contain 'notc'");
            assertTrue(all.contains(notbd), "'all' should contain 'notbd'");
            assertFalse(allbutfirst.contains(notc), "'allbutfirst' should not contain 'notc'");
            assertFalse(allbutfirst.contains(notbd), "'allbutfirst' should not contain 'notbd'");

            // negated/normal
            assertTrue(notc.contains(a), "'notc' should contain 'a'");
            assertTrue(notc.contains(b), "'notc' should contain 'b'");
            assertFalse(notc.contains(c), "'notc' should not contain 'c'");
            assertTrue(notc.contains(d), "'notc' should contain 'd'");
            assertTrue(notc.contains(e), "'notc' should contain 'e'");

            assertTrue(notc.contains(ab), "'notc' should contain 'ab'");
            assertFalse(notc.contains(bc), "'notc' should not contain 'bc'");
            assertFalse(notc.contains(bd), "'notc' should not contain 'bd'");
            assertFalse(notc.contains(cd), "'notc' should not contain 'cd'");
            assertTrue(notc.contains(de), "'notc' should contain 'de'");
            assertFalse(notc.contains(ae), "'notc' should not contain 'ae'");
            assertFalse(notc.contains(all), "'notc' should not contain 'all'");
            assertFalse(notc.contains(allbutfirst), "'notc' should not contain 'allbutfirst'");

            assertTrue(notbd.contains(a), "'notbd' should contain 'a'");
            assertFalse(notbd.contains(b), "'notbd' should not contain 'b'");
            assertFalse(notbd.contains(c), "'notbd' should not contain 'c'");
            assertFalse(notbd.contains(d), "'notbd' should not contain 'd'");
            assertTrue(notbd.contains(e), "'notbd' should contain 'e'");

            assertTrue(notcd.contains(ab), "'notcd' should contain 'ab'");
            assertFalse(notcd.contains(bc), "'notcd' should not contain 'bc'");
            assertFalse(notcd.contains(bd), "'notcd' should not contain 'bd'");
            assertFalse(notcd.contains(cd), "'notcd' should not contain 'cd'");
            assertFalse(notcd.contains(de), "'notcd' should not contain 'de'");
            assertFalse(notcd.contains(ae), "'notcd' should not contain 'ae'");
            assertTrue(notcd.contains(ef), "'notcd' should contain 'ef'");
            assertFalse(notcd.contains(all), "'notcd' should not contain 'all'");
            assertFalse(notcd.contains(allbutfirst), "'notcd' should not contain 'allbutfirst'");

            // negated/negated
            assertFalse(notc.contains(notb), "'notc' should not contain 'notb'");
            assertTrue(notc.contains(notc), "'notc' should contain 'notc'");
            assertFalse(notc.contains(notd), "'notc' should not contain 'notd'");

            assertFalse(notc.contains(notab), "'notc' should not contain 'notab'");
            assertTrue(notc.contains(notbc), "'notc' should contain 'notbc'");
            assertTrue(notc.contains(notbd), "'notc' should contain 'notbd'");
            assertTrue(notc.contains(notcd), "'notc' should contain 'notcd'");
            assertFalse(notc.contains(notde), "'notc' should not contain 'notde'");

            assertFalse(notbd.contains(notb), "'notbd' should not contain 'notb'");
            assertFalse(notbd.contains(notc), "'notbd' should not contain 'notc'");
            assertFalse(notbd.contains(notd), "'notbd' should not contain 'notd'");

            assertFalse(notbd.contains(notab), "'notbd' should not contain 'notab'");
            assertFalse(notbd.contains(notbc), "'notbd' should not contain 'notbc'");
            assertTrue(notbd.contains(notbd), "'notbd' should contain 'notbd'");
            assertFalse(notbd.contains(notcd), "'notbd' should not contain 'notcd'");
            assertFalse(notbd.contains(notde), "'notbd' should not contain 'notde'");
            assertTrue(notbd.contains(notae), "'notbd' should contain 'notae'");
        }

        @Test
        @DisplayName("Test contains(null) throws NullPointerException")
        void testContainsNullArg() {
            final CharRange range = CharRange.is('a');
            final NullPointerException e = assertNullPointerException(() -> range.contains(null));
            assertEquals("range", e.getMessage(), "Exception message should be 'range'");
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Test equals(Object)")
        void testEquals_Object() {
            final CharRange rangea = CharRange.is('a');
            final CharRange rangeae = CharRange.isIn('a', 'e');
            final CharRange rangenotbf = CharRange.isIn('b', 'f');

            assertNotEquals(null, rangea, "Range should not be equal to null");

            assertEquals(rangea, rangea, "Range should be equal to itself");
            assertEquals(rangea, CharRange.is('a'), "Range should be equal to another instance with same values");
            assertEquals(rangeae, rangeae, "Range should be equal to itself");
            assertEquals(rangeae, CharRange.isIn('a', 'e'), "Range should be equal to another instance with same values");
            assertEquals(rangenotbf, rangenotbf, "Range should be equal to itself");
            assertEquals(rangenotbf, CharRange.isIn('b', 'f'), "Range should be equal to another instance with same values");

            assertNotEquals(rangea, rangeae, "Ranges with different values should not be equal");
            assertNotEquals(rangea, rangenotbf, "Ranges with different values should not be equal");
            assertNotEquals(rangeae, rangea, "Ranges with different values should not be equal");
            assertNotEquals(rangeae, rangenotbf, "Ranges with different values should not be equal");
            assertNotEquals(rangenotbf, rangea, "Ranges with different values should not be equal");
            assertNotEquals(rangenotbf, rangeae, "Ranges with different values should not be equal");
        }

        @Test
        @DisplayName("Test hashCode()")
        void testHashCode() {
            final CharRange rangea = CharRange.is('a');
            final CharRange rangeae = CharRange.isIn('a', 'e');
            final CharRange rangenotbf = CharRange.isIn('b', 'f');

            assertEquals(rangea.hashCode(), rangea.hashCode(), "Hash code should be consistent");
            assertEquals(rangea.hashCode(), CharRange.is('a').hashCode(), "Hash code should be equal for equal objects");
            assertEquals(rangeae.hashCode(), rangeae.hashCode(), "Hash code should be consistent");
            assertEquals(rangeae.hashCode(), CharRange.isIn('a', 'e').hashCode(), "Hash code should be equal for equal objects");
            assertEquals(rangenotbf.hashCode(), rangenotbf.hashCode(), "Hash code should be consistent");
            assertEquals(rangenotbf.hashCode(), CharRange.isIn('b', 'f').hashCode(), "Hash code should be equal for equal objects");

            assertNotEquals(rangea.hashCode(), rangeae.hashCode(), "Hash code should be different for different objects");
            assertNotEquals(rangea.hashCode(), rangenotbf.hashCode(), "Hash code should be different for different objects");
            assertNotEquals(rangeae.hashCode(), rangea.hashCode(), "Hash code should be different for different objects");
            assertNotEquals(rangeae.hashCode(), rangenotbf.hashCode(), "Hash code should be different for different objects");
            assertNotEquals(rangenotbf.hashCode(), rangea.hashCode(), "Hash code should be different for different objects");
            assertNotEquals(rangenotbf.hashCode(), rangeae.hashCode(), "Hash code should be different for different objects");
        }
    }

    @Nested
    @DisplayName("Iterator Tests")
    class IteratorTests {

        @Test
        @DisplayName("Test iterator() for single character range")
        void testIterator() {
            final CharRange a = CharRange.is('a');
            final CharRange ad = CharRange.isIn('a', 'd');
            final CharRange nota = CharRange.isNot('a');
            final CharRange emptySet = CharRange.isNotIn((char) 0, Character.MAX_VALUE);
            final CharRange notFirst = CharRange.isNotIn((char) 1, Character.MAX_VALUE);
            final CharRange notLast = CharRange.isNotIn((char) 0, (char) (Character.MAX_VALUE - 1));

            final Iterator<Character> aIt = a.iterator();
            assertNotNull(aIt, "Iterator should not be null");
            assertTrue(aIt.hasNext(), "Iterator should have next");
            assertEquals(Character.valueOf('a'), aIt.next(), "Next character should be 'a'");
            assertFalse(aIt.hasNext(), "Iterator should not have next");

            final Iterator<Character> adIt = ad.iterator();
            assertNotNull(adIt, "Iterator should not be null");
            assertTrue(adIt.hasNext(), "Iterator should have next");
            assertEquals(Character.valueOf('a'), adIt.next(), "Next character should be 'a'");
            assertEquals(Character.valueOf('b'), adIt.next(), "Next character should be 'b'");
            assertEquals(Character.valueOf('c'), adIt.next(), "Next character should be 'c'");
            assertEquals(Character.valueOf('d'), adIt.next(), "Next character should be 'd'");
            assertFalse(adIt.hasNext(), "Iterator should not have next");

            final Iterator<Character> notaIt = nota.iterator();
            assertNotNull(notaIt, "Iterator should not be null");
            assertTrue(notaIt.hasNext(), "Iterator should have next");
            while (notaIt.hasNext()) {
                final Character c = notaIt.next();
                assertNotEquals('a', c.charValue(), "Character should not be 'a'");
            }

            final Iterator<Character> emptySetIt = emptySet.iterator();
            assertNotNull(emptySetIt, "Iterator should not be null");
            assertFalse(emptySetIt.hasNext(), "Iterator should not have next");
            assertThrows(NoSuchElementException.class, emptySetIt::next, "Should throw NoSuchElementException");

            final Iterator<Character> notFirstIt = notFirst.iterator();
            assertNotNull(notFirstIt, "Iterator should not be null");
            assertTrue(notFirstIt.hasNext(), "Iterator should have next");
            assertEquals(Character.valueOf((char) 0), notFirstIt.next(), "Next character should be 0");
            assertFalse(notFirstIt.hasNext(), "Iterator should not have next");
            assertThrows(NoSuchElementException.class, notFirstIt::next, "Should throw NoSuchElementException");

            final Iterator<Character> notLastIt = notLast.iterator();
            assertNotNull(notLastIt, "Iterator should not be null");
            assertTrue(notLastIt.hasNext(), "Iterator should have next");
            assertEquals(Character.valueOf(Character.MAX_VALUE), notLastIt.next(), "Next character should be MAX_VALUE");
            assertFalse(notLastIt.hasNext(), "Iterator should not have next");
            assertThrows(NoSuchElementException.class, notLastIt::next, "Should throw NoSuchElementException");
        }

        @Test
        @DisplayName("Test iterator().remove() throws UnsupportedOperationException")
        void testIteratorRemove() {
            final CharRange a = CharRange.is('a');
            final Iterator<Character> aIt = a.iterator();
            assertThrows(UnsupportedOperationException.class, aIt::remove, "remove() should throw UnsupportedOperationException");
        }
    }


    @Test
    @DisplayName("Test Serialization")
    void testSerialization() {
        CharRange range = CharRange.is('a');
        assertEquals(range, SerializationUtils.clone(range), "Cloned range should be equal to original");
        range = CharRange.isIn('a', 'e');
        assertEquals(range, SerializationUtils.clone(range), "Cloned range should be equal to original");
        range = CharRange.isNotIn('a', 'e');
        assertEquals(range, SerializationUtils.clone(range), "Cloned range should be equal to original");
    }
}
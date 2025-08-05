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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * An improved test suite for {@link CharSet} focusing on understandability and maintainability.
 */
@DisplayName("Tests for org.apache.commons.lang3.CharSet")
class CharSetTest extends AbstractLangTest {

    @Test
    @DisplayName("Class should be public and not final")
    void shouldBePublicAndNonFinal() {
        assertTrue(Modifier.isPublic(CharSet.class.getModifiers()));
        assertFalse(Modifier.isFinal(CharSet.class.getModifiers()));
    }

    @Nested
    @DisplayName("Factory Method: getInstance(String...)")
    class GetInstanceTests {

        @Test
        @DisplayName("should return EMPTY for null, empty, or null-containing array")
        void shouldReturnEmptyForNullOrEmptyInput() {
            assertSame(CharSet.EMPTY, CharSet.getInstance((String) null));
            assertSame(CharSet.EMPTY, CharSet.getInstance(""));
            assertSame(CharSet.EMPTY, CharSet.getInstance(new String[0]));
            // Note: An array with a null string is not EMPTY, but a CharSet containing nothing.
            assertEquals("[]", CharSet.getInstance(new String[]{null}).toString());
        }

        @Test
        @DisplayName("should create a set for a single character 'a'")
        void shouldCreateSetForSingleCharacter() {
            final CharSet set = CharSet.getInstance("a");
            assertAll("Set for 'a'",
                    () -> assertEquals("[a]", set.toString()),
                    () -> assertTrue(set.contains('a')),
                    () -> assertFalse(set.contains('b'))
            );
        }

        @Test
        @DisplayName("should create a set for a character range 'a-e'")
        void shouldCreateSetForCharacterRange() {
            final CharSet set = CharSet.getInstance("a-e");
            assertAll("Set for 'a-e'",
                    () -> assertEquals("[a-e]", set.toString()),
                    () -> assertTrue(set.contains('a')),
                    () -> assertTrue(set.contains('c')),
                    () -> assertTrue(set.contains('e')),
                    () -> assertFalse(set.contains('f'))
            );
        }

        @Test
        @DisplayName("should create a set for a negated single character '^a'")
        void shouldCreateSetForNegatedCharacter() {
            final CharSet set = CharSet.getInstance("^a");
            assertAll("Set for '^a'",
                    () -> assertEquals("[^a]", set.toString()),
                    () -> assertFalse(set.contains('a')),
                    () -> assertTrue(set.contains('b'))
            );
        }

        @Test
        @DisplayName("should create a set for a negated character range '^a-e'")
        void shouldCreateSetForNegatedRange() {
            final CharSet set = CharSet.getInstance("^a-e");
            assertAll("Set for '^a-e'",
                    () -> assertEquals("[^a-e]", set.toString()),
                    () -> assertFalse(set.contains('a')),
                    () -> assertFalse(set.contains('c')),
                    () -> assertFalse(set.contains('e')),
                    () -> assertTrue(set.contains('f'))
            );
        }

        @Test
        @DisplayName("should handle combined characters and ranges like 'ae-fm-pz'")
        void shouldHandleCombinedDefinitions() {
            final CharSet set = CharSet.getInstance("ae-fm-pz");
            assertAll("Set for 'ae-fm-pz'",
                    () -> assertTrue(set.contains('a')),
                    () -> assertFalse(set.contains('d')),
                    () -> assertTrue(set.contains('e')),
                    () -> assertTrue(set.contains('f')),
                    () -> assertFalse(set.contains('g')),
                    () -> assertTrue(set.contains('m')),
                    () -> assertTrue(set.contains('p')),
                    () -> assertFalse(set.contains('q')),
                    () -> assertTrue(set.contains('z'))
            );
        }

        @Test
        @DisplayName("should handle negated characters inside a definition like 'b^a-c^z'")
        void shouldHandleInternalNegations() {
            final CharSet set = CharSet.getInstance("b^a-c^z");
            assertAll("Set for 'b^a-c^z'",
                    () -> assertTrue(set.contains('b')),
                    () -> assertFalse(set.contains('a')),
                    () -> assertFalse(set.contains('c')),
                    () -> assertTrue(set.contains('d')), // Not in negated range
                    () -> assertFalse(set.contains('z'))
            );
        }
    }

    @Nested
    @DisplayName("Edge Case Parsing")
    class EdgeCaseTests {

        @Test
        @DisplayName("should handle dash '-' as a literal when at start, end, or repeated")
        void shouldHandleDashAsLiteral() {
            assertAll("Dash at start '-a'",
                    () -> assertTrue(CharSet.getInstance("-a").contains('-')),
                    () -> assertTrue(CharSet.getInstance("-a").contains('a'))
            );
            assertAll("Dash at end 'a-'",
                    () -> assertTrue(CharSet.getInstance("a-").contains('a')),
                    () -> assertTrue(CharSet.getInstance("a-").contains('-'))
            );
            assertAll("Single dash '-'",
                    () -> assertTrue(CharSet.getInstance("-").contains('-')),
                    () -> assertFalse(CharSet.getInstance("-").contains('a'))
            );
            assertAll("Repeated dashes '----'",
                    () -> assertTrue(CharSet.getInstance("----").contains('-')),
                    () -> assertFalse(CharSet.getInstance("----").contains('a'))
            );
        }

        @Test
        @DisplayName("should handle caret '^' as a literal when not followed by a character or range")
        void shouldHandleCaretAsLiteral() {
            // A single caret is a literal.
            final CharSet singleCaret = CharSet.getInstance("^");
            assertAll("Set for '^'",
                    () -> assertEquals("[^]", singleCaret.toString()),
                    () -> assertTrue(singleCaret.contains('^'))
            );

            // A caret at the end of a definition is a literal.
            final CharSet caretAtEnd = CharSet.getInstance("a^");
            assertAll("Set for 'a^'",
                    () -> assertEquals("[a, ^]", caretAtEnd.toString()),
                    () -> assertTrue(caretAtEnd.contains('a')),
                    () -> assertTrue(caretAtEnd.contains('^'))
            );
        }

        @Test
        @DisplayName("should handle a double caret '^^' as a negated caret")
        void shouldHandleDoubleCaret() {
            final CharSet set = CharSet.getInstance("^^");
            assertAll("Set for '^^'",
                    () -> assertEquals("[^^]", set.toString()),
                    () -> assertFalse(set.contains('^')),
                    () -> assertTrue(set.contains('a'))
            );
        }

        @Test
        @DisplayName("should handle ranges where start and end characters are swapped, like 'd-b'")
        void shouldHandleReversedRange() {
            final CharSet set = CharSet.getInstance("d-b");
            assertAll("Set for 'd-b'",
                    () -> assertEquals("[b-d]", set.toString()),
                    () -> assertFalse(set.contains('a')),
                    () -> assertTrue(set.contains('b')),
                    () -> assertTrue(set.contains('c')),
                    () -> assertTrue(set.contains('d')),
                    () -> assertFalse(set.contains('e'))
            );
        }
    }

    @Nested
    @DisplayName("Behavioral Tests")
    class BehaviorTests {

        @Test
        @DisplayName("should correctly identify character membership")
        void contains() {
            final CharSet rangeBToD = CharSet.getInstance("b-d");
            final CharSet individualCharsBCD = CharSet.getInstance("bcd");
            final CharSet negatedRangeBToD = CharSet.getInstance("^b-d");

            assertAll("Set 'b-d'",
                    () -> assertFalse(rangeBToD.contains('a')),
                    () -> assertTrue(rangeBToD.contains('b')),
                    () -> assertTrue(rangeBToD.contains('c')),
                    () -> assertTrue(rangeBToD.contains('d')),
                    () -> assertFalse(rangeBToD.contains('e'))
            );

            assertAll("Set 'bcd'",
                    () -> assertFalse(individualCharsBCD.contains('a')),
                    () -> assertTrue(individualCharsBCD.contains('b')),
                    () -> assertTrue(individualCharsBCD.contains('c')),
                    () -> assertTrue(individualCharsBCD.contains('d')),
                    () -> assertFalse(individualCharsBCD.contains('e'))
            );

            assertAll("Set '^b-d'",
                    () -> assertTrue(negatedRangeBToD.contains('a')),
                    () -> assertFalse(negatedRangeBToD.contains('b')),
                    () -> assertFalse(negatedRangeBToD.contains('c')),
                    () -> assertFalse(negatedRangeBToD.contains('d')),
                    () -> assertTrue(negatedRangeBToD.contains('e'))
            );
        }

        @Test
        @DisplayName("should adhere to equals() and hashCode() contracts")
        void equalsAndHashCode() {
            final CharSet abc = CharSet.getInstance("abc");
            final CharSet abc2 = CharSet.getInstance("abc");
            final CharSet aToC = CharSet.getInstance("a-c");
            final CharSet aToC2 = CharSet.getInstance("a-c");

            // Test equals
            assertEquals(abc, abc2);
            assertEquals(aToC, aToC2);
            assertNotEquals(abc, aToC, "Sets with same characters but different definitions should not be equal");
            assertNotEquals(null, abc);

            // Test hashCode
            assertEquals(abc.hashCode(), abc2.hashCode());
            assertEquals(aToC.hashCode(), aToC2.hashCode());
        }

        @Test
        @DisplayName("should produce the correct string representation")
        void toStringTest() {
            assertEquals("[]", CharSet.getInstance("").toString());
            assertEquals("[a]", CharSet.getInstance("a").toString());
            assertEquals("[a-e]", CharSet.getInstance("a-e").toString());
            assertEquals("[^a-e]", CharSet.getInstance("^a-e").toString());
            assertEquals("[a-c, e-f]", CharSet.getInstance("a-ce-f").toString());
        }
    }

    @Nested
    @DisplayName("Static Constants")
    class StaticConstantsTest {

        @Test
        void shouldDefineCommonCharacterSetsCorrectly() {
            assertAll("EMPTY",
                    () -> assertEquals(0, CharSet.EMPTY.getCharRanges().length)
            );
            assertAll("ASCII_ALPHA",
                    () -> assertTrue(CharSet.ASCII_ALPHA.contains('a')),
                    () -> assertTrue(CharSet.ASCII_ALPHA.contains('Z')),
                    () -> assertFalse(CharSet.ASCII_ALPHA.contains('5'))
            );
            assertAll("ASCII_ALPHA_LOWER",
                    () -> assertTrue(CharSet.ASCII_ALPHA_LOWER.contains('a')),
                    () -> assertFalse(CharSet.ASCII_ALPHA_LOWER.contains('A'))
            );
            assertAll("ASCII_ALPHA_UPPER",
                    () -> assertTrue(CharSet.ASCII_ALPHA_UPPER.contains('A')),
                    () -> assertFalse(CharSet.ASCII_ALPHA_UPPER.contains('a'))
            );
            assertAll("ASCII_NUMERIC",
                    () -> assertTrue(CharSet.ASCII_NUMERIC.contains('0')),
                    () -> assertTrue(CharSet.ASCII_NUMERIC.contains('9')),
                    () -> assertFalse(CharSet.ASCII_NUMERIC.contains('A'))
            );
        }

        @Test
        void getInstanceShouldReturnCachedInstancesForCommonSets() {
            assertSame(CharSet.EMPTY, CharSet.getInstance((String) null));
            assertSame(CharSet.EMPTY, CharSet.getInstance(""));
            assertSame(CharSet.ASCII_ALPHA, CharSet.getInstance("a-zA-Z"));
            assertSame(CharSet.ASCII_ALPHA, CharSet.getInstance("A-Za-z"));
            assertSame(CharSet.ASCII_ALPHA_LOWER, CharSet.getInstance("a-z"));
            assertSame(CharSet.ASCII_ALPHA_UPPER, CharSet.getInstance("A-Z"));
            assertSame(CharSet.ASCII_NUMERIC, CharSet.getInstance("0-9"));
        }
    }

    @Nested
    @DisplayName("Serialization")
    class SerializationTest {

        @Test
        void shouldBeSerializableAndDeserializable() {
            assertEquals(CharSet.getInstance("a"), SerializationUtils.clone(CharSet.getInstance("a")));
            assertEquals(CharSet.getInstance("a-e"), SerializationUtils.clone(CharSet.getInstance("a-e")));
            assertEquals(CharSet.getInstance("be-f^a-z"), SerializationUtils.clone(CharSet.getInstance("be-f^a-z")));
            assertEquals(CharSet.ASCII_ALPHA, SerializationUtils.clone(CharSet.ASCII_ALPHA));
        }
    }
}
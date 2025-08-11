/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests {@link CharSetUtils}.
 */
class CharSetUtilsTest extends AbstractLangTest {

    @Test
    @DisplayName("The class should be public and have a public default constructor")
    void testApiDesign() {
        assertNotNull(new CharSetUtils(), "Instantiating via constructor should be possible.");

        // Test that the constructor is public, as documented for JavaBean tools.
        final Constructor<?>[] constructors = CharSetUtils.class.getDeclaredConstructors();
        assertEquals(1, constructors.length, "Should have only one constructor.");
        assertTrue(Modifier.isPublic(constructors[0].getModifiers()), "The constructor should be public.");

        // Test that the class is public and not final.
        assertTrue(Modifier.isPublic(CharSetUtils.class.getModifiers()), "The class should be public.");
        assertFalse(Modifier.isFinal(CharSetUtils.class.getModifiers()), "The class should not be final.");
    }

    @Nested
    @DisplayName("containsAny(String, String...)")
    class ContainsAnyTest {

        @Test
        void shouldReturnFalseForNullOrEmptyString() {
            assertFalse(CharSetUtils.containsAny(null, "a"));
            assertFalse(CharSetUtils.containsAny("", "a"));
        }

        @Test
        void shouldReturnFalseForNullOrEmptySet() {
            assertFalse(CharSetUtils.containsAny("hello", (String) null), "Null single set should return false");
            assertFalse(CharSetUtils.containsAny("hello", (String[]) null), "Null varargs array should return false");
            assertFalse(CharSetUtils.containsAny("hello"), "Empty varargs should return false");
            assertFalse(CharSetUtils.containsAny("hello", ""), "Empty string in set should return false");
        }

        @Test
        void shouldReturnFalseWhenNoCharsMatch() {
            assertFalse(CharSetUtils.containsAny("hello", "x"));
            assertFalse(CharSetUtils.containsAny("hello", "a-d"));
        }

        @Test
        void shouldReturnTrueWhenAnyCharMatches() {
            assertTrue(CharSetUtils.containsAny("hello", "el"));
            assertTrue(CharSetUtils.containsAny("hello", "e-i"));
            assertTrue(CharSetUtils.containsAny("hello", "a-z"));
        }
    }

    @Nested
    @DisplayName("count(String, String...)")
    class CountTest {

        @Test
        void shouldReturnZeroForNullOrEmptyInputs() {
            // Null or empty string
            assertEquals(0, CharSetUtils.count(null, "a"));
            assertEquals(0, CharSetUtils.count("", "a"));

            // Null or empty set
            assertEquals(0, CharSetUtils.count("hello", (String) null));
            assertEquals(0, CharSetUtils.count("hello", (String[]) null));
            assertEquals(0, CharSetUtils.count("hello"));
            assertEquals(0, CharSetUtils.count("hello", ""));
        }

        @ParameterizedTest
        @CsvSource({
            "'x', 0",
            "'el', 3",
            "'e-i', 2", // e, h
            "'l-p', 3", // l, l, o
            "'a-z', 5"
        })
        void shouldReturnCorrectCountForGivenSet(final String set, final int expectedCount) {
            assertEquals(expectedCount, CharSetUtils.count("hello", set));
        }
    }

    @Nested
    @DisplayName("delete(String, String...)")
    class DeleteTest {

        @Test
        void shouldReturnNullForNullString() {
            assertNull(CharSetUtils.delete(null, "a"));
        }

        @Test
        void shouldReturnEmptyForEmptyString() {
            assertEquals("", CharSetUtils.delete("", "a"));
        }

        @Test
        void shouldReturnOriginalStringForNullOrEmptySet() {
            assertEquals("hello", CharSetUtils.delete("hello", (String) null));
            assertEquals("hello", CharSetUtils.delete("hello", (String[]) null));
            assertEquals("hello", CharSetUtils.delete("hello"));
            assertEquals("hello", CharSetUtils.delete("hello", ""));
        }

        @ParameterizedTest
        @CsvSource({
            // input,    set,      expected
            "'hello',   'z',      'hello'",
            "'hello',   'l',      'heo'",
            "'hello',   'el',     'ho'",
            "'hello',   'elho',   ''",
            "'hello',   'a-e',    'hllo'",   // deletes 'e'
            "'hello',   'l-p',    'he'",     // deletes 'l', 'l', 'o'
            "'hello',   'a-z',    ''",
            "'----',    '-',      ''"
        })
        void shouldDeleteCharsInSet(final String input, final String set, final String expected) {
            assertEquals(expected, CharSetUtils.delete(input, set));
        }
    }

    @Nested
    @DisplayName("keep(String, String...)")
    class KeepTest {

        @Test
        void shouldReturnNullForNullString() {
            assertNull(CharSetUtils.keep(null, "a"));
        }

        @Test
        void shouldReturnEmptyForEmptyString() {
            assertEquals("", CharSetUtils.keep("", "a"));
        }

        @Test
        void shouldReturnEmptyStringForNullOrEmptySet() {
            assertEquals("", CharSetUtils.keep("hello", (String) null));
            assertEquals("", CharSetUtils.keep("hello", (String[]) null));
            assertEquals("", CharSetUtils.keep("hello"));
            assertEquals("", CharSetUtils.keep("hello", ""));
        }

        @ParameterizedTest
        @CsvSource({
            // input,    set,      expected
            "'hello',   'z',      ''",
            "'hello',   'l',      'll'",
            "'hello',   'el',     'ell'",
            "'hello',   'elho',   'hello'",
            "'hello',   'a-e',    'e'",
            "'hello',   'a-z',    'hello'",
            "'----',    '-',      '----'"
        })
        void shouldKeepCharsInSet(final String input, final String set, final String expected) {
            assertEquals(expected, CharSetUtils.keep(input, set));
        }
    }

    @Nested
    @DisplayName("squeeze(String, String...)")
    class SqueezeTest {

        @Test
        void shouldReturnNullForNullString() {
            assertNull(CharSetUtils.squeeze(null, "a"));
        }

        @Test
        void shouldReturnEmptyForEmptyString() {
            assertEquals("", CharSetUtils.squeeze("", "a"));
        }

        @Test
        void shouldReturnOriginalStringForNullOrEmptySet() {
            assertEquals("hello", CharSetUtils.squeeze("hello", (String) null));
            assertEquals("hello", CharSetUtils.squeeze("hello", (String[]) null));
            assertEquals("hello", CharSetUtils.squeeze("hello"));
            assertEquals("hello", CharSetUtils.squeeze("hello", ""));
        }

        @ParameterizedTest
        @CsvSource({
            // input,        set,      expected
            "'hello',       'a-e',    'hello'",      // 'e' is not repeated
            "'hello',       'l-p',    'helo'",       // 'll' -> 'l'
            "'helloo',      'l',      'helo'",       // 'll' -> 'l'; 'oo' is not in set
            "'helloo',      'o',      'hello'",      // 'oo' -> 'o'; 'll' is not in set
            "'helloo',      'lo',     'helo'",       // 'll' -> 'l', 'oo' -> 'o'
            "'fooffooff',   'of',     'fofof'",      // 'oo' -> 'o', 'ff' -> 'f'
            "'fooooff',     'fo',     'fof'",        // 'oooo' -> 'o', 'ff' -> 'f'
            "'helloo',      '^l',     'hello'",      // Squeeze chars NOT in set, so 'oo' -> 'o'
        })
        void shouldSqueezeCharsInSet(final String input, final String set, final String expected) {
            assertEquals(expected, CharSetUtils.squeeze(input, set));
        }
    }
}
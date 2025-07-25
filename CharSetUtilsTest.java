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

/**
 * Tests {@link CharSetUtils}.
 */
@DisplayName("CharSetUtils Tests")
class CharSetUtilsTest extends AbstractLangTest {

    @Test
    @DisplayName("Constructor is public and not final")
    void testConstructor() {
        // Verify that the constructor exists and is accessible.
        assertNotNull(new CharSetUtils());

        // Verify that there's only one constructor.
        final Constructor<?>[] constructors = CharSetUtils.class.getDeclaredConstructors();
        assertEquals(1, constructors.length, "Expected only one constructor.");

        // Verify that the constructor is public.
        assertTrue(Modifier.isPublic(constructors[0].getModifiers()), "Constructor should be public.");

        // Verify that the class is public.
        assertTrue(Modifier.isPublic(CharSetUtils.class.getModifiers()), "Class should be public.");

        // Verify that the class is not final.
        assertFalse(Modifier.isFinal(CharSetUtils.class.getModifiers()), "Class should not be final.");
    }

    @Nested
    @DisplayName("containsAny(String, String) tests")
    class ContainsAnyStringString {
        @Test
        @DisplayName("Null and empty string scenarios")
        void testNullAndEmptyStrings() {
            assertFalse(CharSetUtils.containsAny(null, (String) null), "Null string and null charset should return false");
            assertFalse(CharSetUtils.containsAny(null, ""), "Null string and empty charset should return false");

            assertFalse(CharSetUtils.containsAny("", (String) null), "Empty string and null charset should return false");
            assertFalse(CharSetUtils.containsAny("", ""), "Empty string and empty charset should return false");
            assertFalse(CharSetUtils.containsAny("", "a-e"), "Empty string and non-empty charset should return false");

            assertFalse(CharSetUtils.containsAny("hello", (String) null), "String and null charset should return false");
            assertFalse(CharSetUtils.containsAny("hello", ""), "String and empty charset should return false");
        }

        @Test
        @DisplayName("Positive and negative matches")
        void testPositiveAndNegativeMatches() {
            assertTrue(CharSetUtils.containsAny("hello", "a-e"), "Should return true if any character in charset is present");
            assertTrue(CharSetUtils.containsAny("hello", "l-p"), "Should return true if any character in charset is present");
            assertFalse(CharSetUtils.containsAny("hello", "xyz"), "Should return false if no character in charset is present");
        }
    }

    @Nested
    @DisplayName("containsAny(String, String...) tests")
    class ContainsAnyStringStringArray {
        @Test
        @DisplayName("Null and empty string scenarios")
        void testNullAndEmptyStrings() {
            assertFalse(CharSetUtils.containsAny(null, (String[]) null), "Null string and null charset array should return false");
            assertFalse(CharSetUtils.containsAny(null), "Null string and empty charset array should return false");
            assertFalse(CharSetUtils.containsAny(null, (String) null), "Null string and null charset should return false");
            assertFalse(CharSetUtils.containsAny(null, "a-e"), "Null string and non-empty charset should return false");

            assertFalse(CharSetUtils.containsAny("", (String[]) null), "Empty string and null charset array should return false");
            assertFalse(CharSetUtils.containsAny(""), "Empty string and empty charset array should return false");
            assertFalse(CharSetUtils.containsAny("", (String) null), "Empty string and null charset should return false");
            assertFalse(CharSetUtils.containsAny("", "a-e"), "Empty string and non-empty charset should return false");

            assertFalse(CharSetUtils.containsAny("hello", (String[]) null), "String and null charset array should return false");
            assertFalse(CharSetUtils.containsAny("hello"), "String and empty charset array should return false");
            assertFalse(CharSetUtils.containsAny("hello", (String) null), "String and null charset should return false");
        }

        @Test
        @DisplayName("Positive and negative matches")
        void testPositiveAndNegativeMatches() {
            assertTrue(CharSetUtils.containsAny("hello", "a-e"), "Should return true if any character in charset is present");

            assertTrue(CharSetUtils.containsAny("hello", "el"), "Should return true if any character in charset is present");
            assertFalse(CharSetUtils.containsAny("hello", "x"), "Should return false if no character in charset is present");
            assertTrue(CharSetUtils.containsAny("hello", "e-i"), "Should return true if any character in charset is present");
            assertTrue(CharSetUtils.containsAny("hello", "a-z"), "Should return true if any character in charset is present");
            assertFalse(CharSetUtils.containsAny("hello", ""), "Empty charset should return false");
        }
    }

    @Nested
    @DisplayName("count(String, String) tests")
    class CountStringString {
        @Test
        @DisplayName("Null and empty string scenarios")
        void testNullAndEmptyStrings() {
            assertEquals(0, CharSetUtils.count(null, (String) null), "Null string and null charset should return 0");
            assertEquals(0, CharSetUtils.count(null, ""), "Null string and empty charset should return 0");

            assertEquals(0, CharSetUtils.count("", (String) null), "Empty string and null charset should return 0");
            assertEquals(0, CharSetUtils.count("", ""), "Empty string and empty charset should return 0");
            assertEquals(0, CharSetUtils.count("", "a-e"), "Empty string and non-empty charset should return 0");

            assertEquals(0, CharSetUtils.count("hello", (String) null), "String and null charset should return 0");
            assertEquals(0, CharSetUtils.count("hello", ""), "String and empty charset should return 0");
        }

        @Test
        @DisplayName("Character counting")
        void testCharacterCounting() {
            assertEquals(1, CharSetUtils.count("hello", "a-e"), "Should return correct count of characters present");
            assertEquals(3, CharSetUtils.count("hello", "l-p"), "Should return correct count of characters present");
        }
    }

    @Nested
    @DisplayName("count(String, String...) tests")
    class CountStringStringArray {
        @Test
        @DisplayName("Null and empty string scenarios")
        void testNullAndEmptyStrings() {
            assertEquals(0, CharSetUtils.count(null, (String[]) null), "Null string and null charset array should return 0");
            assertEquals(0, CharSetUtils.count(null), "Null string and empty charset array should return 0");
            assertEquals(0, CharSetUtils.count(null, (String) null), "Null string and null charset should return 0");
            assertEquals(0, CharSetUtils.count(null, "a-e"), "Null string and non-empty charset should return 0");

            assertEquals(0, CharSetUtils.count("", (String[]) null), "Empty string and null charset array should return 0");
            assertEquals(0, CharSetUtils.count(""), "Empty string and empty charset array should return 0");
            assertEquals(0, CharSetUtils.count("", (String) null), "Empty string and null charset should return 0");
            assertEquals(0, CharSetUtils.count("", "a-e"), "Empty string and non-empty charset should return 0");

            assertEquals(0, CharSetUtils.count("hello", (String[]) null), "String and null charset array should return 0");
            assertEquals(0, CharSetUtils.count("hello"), "String and empty charset array should return 0");
            assertEquals(0, CharSetUtils.count("hello", (String) null), "String and null charset should return 0");
        }

        @Test
        @DisplayName("Character counting")
        void testCharacterCounting() {
            assertEquals(1, CharSetUtils.count("hello", "a-e"), "Should return correct count of characters present");

            assertEquals(3, CharSetUtils.count("hello", "el"), "Should return correct count of characters present");
            assertEquals(0, CharSetUtils.count("hello", "x"), "Should return correct count of characters present");
            assertEquals(2, CharSetUtils.count("hello", "e-i"), "Should return correct count of characters present");
            assertEquals(5, CharSetUtils.count("hello", "a-z"), "Should return correct count of characters present");
            assertEquals(0, CharSetUtils.count("hello", ""), "Empty charset should return 0");
        }
    }

    @Nested
    @DisplayName("delete(String, String) tests")
    class DeleteStringString {
        @Test
        @DisplayName("Null and empty string scenarios")
        void testNullAndEmptyStrings() {
            assertNull(CharSetUtils.delete(null, (String) null), "Null string and null charset should return null");
            assertNull(CharSetUtils.delete(null, ""), "Null string and empty charset should return null");

            assertEquals("", CharSetUtils.delete("", (String) null), "Empty string and null charset should return empty string");
            assertEquals("", CharSetUtils.delete("", ""), "Empty string and empty charset should return empty string");
            assertEquals("", CharSetUtils.delete("", "a-e"), "Empty string and non-empty charset should return empty string");

            assertEquals("hello", CharSetUtils.delete("hello", (String) null), "String and null charset should return original string");
            assertEquals("hello", CharSetUtils.delete("hello", ""), "String and empty charset should return original string");
        }

        @Test
        @DisplayName("Character deletion")
        void testCharacterDeletion() {
            assertEquals("hllo", CharSetUtils.delete("hello", "a-e"), "Should delete specified characters");
            assertEquals("he", CharSetUtils.delete("hello", "l-p"), "Should delete specified characters");
            assertEquals("hello", CharSetUtils.delete("hello", "z"), "Should not delete if characters not present");
        }
    }

    @Nested
    @DisplayName("delete(String, String...) tests")
    class DeleteStringStringArray {
        @Test
        @DisplayName("Null and empty string scenarios")
        void testNullAndEmptyStrings() {
            assertNull(CharSetUtils.delete(null, (String[]) null), "Null string and null charset array should return null");
            assertNull(CharSetUtils.delete(null), "Null string and empty charset array should return null");
            assertNull(CharSetUtils.delete(null, (String) null), "Null string and null charset should return null");
            assertNull(CharSetUtils.delete(null, "el"), "Null string and non-empty charset should return null");

            assertEquals("", CharSetUtils.delete("", (String[]) null), "Empty string and null charset array should return empty string");
            assertEquals("", CharSetUtils.delete(""), "Empty string and empty charset array should return empty string");
            assertEquals("", CharSetUtils.delete("", (String) null), "Empty string and null charset should return empty string");
            assertEquals("", CharSetUtils.delete("", "a-e"), "Empty string and non-empty charset should return empty string");

            assertEquals("hello", CharSetUtils.delete("hello", (String[]) null), "String and null charset array should return original string");
            assertEquals("hello", CharSetUtils.delete("hello"), "String and empty charset array should return original string");
            assertEquals("hello", CharSetUtils.delete("hello", (String) null), "String and null charset should return original string");
        }

        @Test
        @DisplayName("Character deletion")
        void testCharacterDeletion() {
            assertEquals("hello", CharSetUtils.delete("hello", "xyz"), "Should not delete characters not present");

            assertEquals("ho", CharSetUtils.delete("hello", "el"), "Should delete specified characters");
            assertEquals("", CharSetUtils.delete("hello", "elho"), "Should delete specified characters");
            assertEquals("hello", CharSetUtils.delete("hello", ""), "Empty charset should return original string");
            assertEquals("", CharSetUtils.delete("hello", "a-z"), "Should delete all characters if all are in charset");
            assertEquals("", CharSetUtils.delete("----", "-"), "Should delete all occurrences of specified character");
            assertEquals("heo", CharSetUtils.delete("hello", "l"), "Should delete specified character");
        }
    }

    @Nested
    @DisplayName("keep(String, String) tests")
    class KeepStringString {
        @Test
        @DisplayName("Null and empty string scenarios")
        void testNullAndEmptyStrings() {
            assertNull(CharSetUtils.keep(null, (String) null), "Null string and null charset should return null");
            assertNull(CharSetUtils.keep(null, ""), "Null string and empty charset should return null");

            assertEquals("", CharSetUtils.keep("", (String) null), "Empty string and null charset should return empty string");
            assertEquals("", CharSetUtils.keep("", ""), "Empty string and empty charset should return empty string");
            assertEquals("", CharSetUtils.keep("", "a-e"), "Empty string and non-empty charset should return empty string");

            assertEquals("", CharSetUtils.keep("hello", (String) null), "String and null charset should return empty string");
            assertEquals("", CharSetUtils.keep("hello", ""), "String and empty charset should return empty string");
        }

        @Test
        @DisplayName("Character keeping")
        void testCharacterKeeping() {
            assertEquals("", CharSetUtils.keep("hello", "xyz"), "Should return empty string if no characters match");
            assertEquals("hello", CharSetUtils.keep("hello", "a-z"), "Should keep all characters that match");
            assertEquals("hello", CharSetUtils.keep("hello", "oleh"), "Should keep all characters that match");
            assertEquals("ell", CharSetUtils.keep("hello", "el"), "Should keep all characters that match");
        }
    }

    @Nested
    @DisplayName("keep(String, String...) tests")
    class KeepStringStringArray {
        @Test
        @DisplayName("Null and empty string scenarios")
        void testNullAndEmptyStrings() {
            assertNull(CharSetUtils.keep(null, (String[]) null), "Null string and null charset array should return null");
            assertNull(CharSetUtils.keep(null), "Null string and empty charset array should return null");
            assertNull(CharSetUtils.keep(null, (String) null), "Null string and null charset should return null");
            assertNull(CharSetUtils.keep(null, "a-e"), "Null string and non-empty charset should return null");

            assertEquals("", CharSetUtils.keep("", (String[]) null), "Empty string and null charset array should return empty string");
            assertEquals("", CharSetUtils.keep(""), "Empty string and empty charset array should return empty string");
            assertEquals("", CharSetUtils.keep("", (String) null), "Empty string and null charset should return empty string");
            assertEquals("", CharSetUtils.keep("", "a-e"), "Empty string and non-empty charset should return empty string");

            assertEquals("", CharSetUtils.keep("hello", (String[]) null), "String and null charset array should return empty string");
            assertEquals("", CharSetUtils.keep("hello"), "String and empty charset array should return empty string");
            assertEquals("", CharSetUtils.keep("hello", (String) null), "String and null charset should return empty string");
        }

        @Test
        @DisplayName("Character keeping")
        void testCharacterKeeping() {
            assertEquals("e", CharSetUtils.keep("hello", "a-e"), "Should keep matching characters");
            assertEquals("ell", CharSetUtils.keep("hello", "el"), "Should keep matching characters");
            assertEquals("hello", CharSetUtils.keep("hello", "elho"), "Should keep matching characters");
            assertEquals("hello", CharSetUtils.keep("hello", "a-z"), "Should keep all characters");
            assertEquals("----", CharSetUtils.keep("----", "-"), "Should keep all occurrences of the specified character");
            assertEquals("ll", CharSetUtils.keep("hello", "l"), "Should keep specified character");
        }
    }

    @Nested
    @DisplayName("squeeze(String, String) tests")
    class SqueezeStringString {
        @Test
        @DisplayName("Null and empty string scenarios")
        void testNullAndEmptyStrings() {
            assertNull(CharSetUtils.squeeze(null, (String) null), "Null string and null charset should return null");
            assertNull(CharSetUtils.squeeze(null, ""), "Null string and empty charset should return null");

            assertEquals("", CharSetUtils.squeeze("", (String) null), "Empty string and null charset should return empty string");
            assertEquals("", CharSetUtils.squeeze("", ""), "Empty string and empty charset should return empty string");
            assertEquals("", CharSetUtils.squeeze("", "a-e"), "Empty string and non-empty charset should return empty string");

            assertEquals("hello", CharSetUtils.squeeze("hello", (String) null), "String and null charset should return original string");
            assertEquals("hello", CharSetUtils.squeeze("hello", ""), "String and empty charset should return original string");
        }

        @Test
        @DisplayName("Character squeezing")
        void testCharacterSqueezing() {
            assertEquals("hello", CharSetUtils.squeeze("hello", "a-e"), "Should not squeeze if characters not repeated");
            assertEquals("helo", CharSetUtils.squeeze("hello", "l-p"), "Should squeeze repeated characters in charset");
            assertEquals("heloo", CharSetUtils.squeeze("helloo", "l"), "Should squeeze repeated character");
            assertEquals("hello", CharSetUtils.squeeze("helloo", "^l"), "Should not squeeze character that is not in charset");
        }
    }

    @Nested
    @DisplayName("squeeze(String, String...) tests")
    class SqueezeStringStringArray {
        @Test
        @DisplayName("Null and empty string scenarios")
        void testNullAndEmptyStrings() {
            assertNull(CharSetUtils.squeeze(null, (String[]) null), "Null string and null charset array should return null");
            assertNull(CharSetUtils.squeeze(null), "Null string and empty charset array should return null");
            assertNull(CharSetUtils.squeeze(null, (String) null), "Null string and null charset should return null");
            assertNull(CharSetUtils.squeeze(null, "el"), "Null string and non-empty charset should return null");

            assertEquals("", CharSetUtils.squeeze("", (String[]) null), "Empty string and null charset array should return empty string");
            assertEquals("", CharSetUtils.squeeze(""), "Empty string and empty charset array should return empty string");
            assertEquals("", CharSetUtils.squeeze("", (String) null), "Empty string and null charset should return empty string");
            assertEquals("", CharSetUtils.squeeze("", "a-e"), "Empty string and non-empty charset should return empty string");

            assertEquals("hello", CharSetUtils.squeeze("hello", (String[]) null), "String and null charset array should return original string");
            assertEquals("hello", CharSetUtils.squeeze("hello"), "String and empty charset array should return original string");
            assertEquals("hello", CharSetUtils.squeeze("hello", (String) null), "String and null charset should return original string");
            assertEquals("hello", CharSetUtils.squeeze("hello", "a-e"), "String and empty charset should return original string if nothing to squeeze");
        }

        @Test
        @DisplayName("Character squeezing")
        void testCharacterSqueezing() {
            assertEquals("helo", CharSetUtils.squeeze("hello", "el"), "Should squeeze specified characters");
            assertEquals("hello", CharSetUtils.squeeze("hello", "e"), "Should squeeze specified character");
            assertEquals("fofof", CharSetUtils.squeeze("fooffooff", "of"), "Should squeeze multiple characters");
            assertEquals("fof", CharSetUtils.squeeze("fooooff", "fo"), "Should squeeze characters appearing together");
        }
    }
}
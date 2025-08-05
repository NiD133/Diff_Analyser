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

import org.junit.jupiter.api.Test;

/**
 * Tests {@link CharSetUtils}.
 */
class CharSetUtilsTest extends AbstractLangTest {

    // Test data constants for better readability and reusability
    private static final String HELLO = "hello";
    private static final String EMPTY_STRING = "";
    private static final String RANGE_A_TO_E = "a-e";
    private static final String RANGE_L_TO_P = "l-p";
    private static final String RANGE_A_TO_Z = "a-z";
    private static final String CHARS_EL = "el";
    private static final String CHAR_X = "x";

    @Test
    void testConstructor() {
        // Verify that CharSetUtils can be instantiated (though it's a utility class)
        assertNotNull(new CharSetUtils());
        
        // Verify class structure - should have exactly one public constructor
        final Constructor<?>[] constructors = CharSetUtils.class.getDeclaredConstructors();
        assertEquals(1, constructors.length, "CharSetUtils should have exactly one constructor");
        assertTrue(Modifier.isPublic(constructors[0].getModifiers()), "Constructor should be public");
        
        // Verify class modifiers
        assertTrue(Modifier.isPublic(CharSetUtils.class.getModifiers()), "Class should be public");
        assertFalse(Modifier.isFinal(CharSetUtils.class.getModifiers()), "Class should not be final");
    }

    @Test
    void testContainsAny_WithSingleCharSet() {
        // Test null input handling
        assertFalse(CharSetUtils.containsAny(null, (String) null), 
                   "Should return false when both string and charset are null");
        assertFalse(CharSetUtils.containsAny(null, EMPTY_STRING), 
                   "Should return false when string is null");

        // Test empty string handling
        assertFalse(CharSetUtils.containsAny(EMPTY_STRING, (String) null), 
                   "Should return false when charset is null");
        assertFalse(CharSetUtils.containsAny(EMPTY_STRING, EMPTY_STRING), 
                   "Should return false when both string and charset are empty");
        assertFalse(CharSetUtils.containsAny(EMPTY_STRING, RANGE_A_TO_E), 
                   "Should return false when string is empty");

        // Test normal operation
        assertFalse(CharSetUtils.containsAny(HELLO, (String) null), 
                   "Should return false when charset is null");
        assertFalse(CharSetUtils.containsAny(HELLO, EMPTY_STRING), 
                   "Should return false when charset is empty");
        assertTrue(CharSetUtils.containsAny(HELLO, RANGE_A_TO_E), 
                  "Should find 'e' in 'hello' using range 'a-e'");
        assertTrue(CharSetUtils.containsAny(HELLO, RANGE_L_TO_P), 
                  "Should find 'l' and 'o' in 'hello' using range 'l-p'");
    }

    @Test
    void testContainsAny_WithMultipleCharSets() {
        // Test null and empty array handling
        assertFalse(CharSetUtils.containsAny(null, (String[]) null), 
                   "Should return false when string is null and charset array is null");
        assertFalse(CharSetUtils.containsAny(null), 
                   "Should return false when string is null and no charsets provided");
        assertFalse(CharSetUtils.containsAny(null, (String) null), 
                   "Should return false when both string and charset are null");
        assertFalse(CharSetUtils.containsAny(null, RANGE_A_TO_E), 
                   "Should return false when string is null");

        // Test empty string with various charset inputs
        assertFalse(CharSetUtils.containsAny(EMPTY_STRING, (String[]) null), 
                   "Should return false when string is empty and charset array is null");
        assertFalse(CharSetUtils.containsAny(EMPTY_STRING), 
                   "Should return false when string is empty and no charsets provided");
        assertFalse(CharSetUtils.containsAny(EMPTY_STRING, (String) null), 
                   "Should return false when string is empty and charset is null");
        assertFalse(CharSetUtils.containsAny(EMPTY_STRING, RANGE_A_TO_E), 
                   "Should return false when string is empty");

        // Test normal string with various charset inputs
        assertFalse(CharSetUtils.containsAny(HELLO, (String[]) null), 
                   "Should return false when charset array is null");
        assertFalse(CharSetUtils.containsAny(HELLO), 
                   "Should return false when no charsets provided");
        assertFalse(CharSetUtils.containsAny(HELLO, (String) null), 
                   "Should return false when charset is null");
        assertTrue(CharSetUtils.containsAny(HELLO, RANGE_A_TO_E), 
                  "Should find 'e' in 'hello' using range 'a-e'");

        // Test with specific character sets
        assertTrue(CharSetUtils.containsAny(HELLO, CHARS_EL), 
                  "Should find 'e' and 'l' characters in 'hello'");
        assertFalse(CharSetUtils.containsAny(HELLO, CHAR_X), 
                   "Should not find 'x' in 'hello'");
        assertTrue(CharSetUtils.containsAny(HELLO, "e-i"), 
                  "Should find 'e' and 'h' in 'hello' using range 'e-i'");
        assertTrue(CharSetUtils.containsAny(HELLO, RANGE_A_TO_Z), 
                  "Should find all letters in 'hello' using range 'a-z'");
        assertFalse(CharSetUtils.containsAny(HELLO, EMPTY_STRING), 
                   "Should return false when charset is empty");
    }

    @Test
    void testCount_WithSingleCharSet() {
        // Test null input handling
        assertEquals(0, CharSetUtils.count(null, (String) null), 
                    "Should return 0 when both string and charset are null");
        assertEquals(0, CharSetUtils.count(null, EMPTY_STRING), 
                    "Should return 0 when string is null");

        // Test empty string handling
        assertEquals(0, CharSetUtils.count(EMPTY_STRING, (String) null), 
                    "Should return 0 when charset is null");
        assertEquals(0, CharSetUtils.count(EMPTY_STRING, EMPTY_STRING), 
                    "Should return 0 when both string and charset are empty");
        assertEquals(0, CharSetUtils.count(EMPTY_STRING, RANGE_A_TO_E), 
                    "Should return 0 when string is empty");

        // Test normal operation
        assertEquals(0, CharSetUtils.count(HELLO, (String) null), 
                    "Should return 0 when charset is null");
        assertEquals(0, CharSetUtils.count(HELLO, EMPTY_STRING), 
                    "Should return 0 when charset is empty");
        assertEquals(1, CharSetUtils.count(HELLO, RANGE_A_TO_E), 
                    "Should count 1 character ('e') in 'hello' using range 'a-e'");
        assertEquals(3, CharSetUtils.count(HELLO, RANGE_L_TO_P), 
                    "Should count 3 characters ('l', 'l', 'o') in 'hello' using range 'l-p'");
    }

    @Test
    void testCount_WithMultipleCharSets() {
        // Test null and empty inputs
        assertEquals(0, CharSetUtils.count(null, (String[]) null), 
                    "Should return 0 when string is null and charset array is null");
        assertEquals(0, CharSetUtils.count(null), 
                    "Should return 0 when string is null and no charsets provided");
        assertEquals(0, CharSetUtils.count(null, (String) null), 
                    "Should return 0 when both string and charset are null");
        assertEquals(0, CharSetUtils.count(null, RANGE_A_TO_E), 
                    "Should return 0 when string is null");

        assertEquals(0, CharSetUtils.count(EMPTY_STRING, (String[]) null), 
                    "Should return 0 when string is empty and charset array is null");
        assertEquals(0, CharSetUtils.count(EMPTY_STRING), 
                    "Should return 0 when string is empty and no charsets provided");
        assertEquals(0, CharSetUtils.count(EMPTY_STRING, (String) null), 
                    "Should return 0 when string is empty and charset is null");
        assertEquals(0, CharSetUtils.count(EMPTY_STRING, RANGE_A_TO_E), 
                    "Should return 0 when string is empty");

        // Test normal operation
        assertEquals(0, CharSetUtils.count(HELLO, (String[]) null), 
                    "Should return 0 when charset array is null");
        assertEquals(0, CharSetUtils.count(HELLO), 
                    "Should return 0 when no charsets provided");
        assertEquals(0, CharSetUtils.count(HELLO, (String) null), 
                    "Should return 0 when charset is null");
        assertEquals(1, CharSetUtils.count(HELLO, RANGE_A_TO_E), 
                    "Should count 1 character ('e') in 'hello' using range 'a-e'");

        // Test with specific character sets
        assertEquals(3, CharSetUtils.count(HELLO, CHARS_EL), 
                    "Should count 3 characters ('e', 'l', 'l') in 'hello'");
        assertEquals(0, CharSetUtils.count(HELLO, CHAR_X), 
                    "Should count 0 characters ('x') in 'hello'");
        assertEquals(2, CharSetUtils.count(HELLO, "e-i"), 
                    "Should count 2 characters ('e', 'h') in 'hello' using range 'e-i'");
        assertEquals(5, CharSetUtils.count(HELLO, RANGE_A_TO_Z), 
                    "Should count all 5 characters in 'hello' using range 'a-z'");
        assertEquals(0, CharSetUtils.count(HELLO, EMPTY_STRING), 
                    "Should return 0 when charset is empty");
    }

    @Test
    void testDelete_WithSingleCharSet() {
        // Test null input handling
        assertNull(CharSetUtils.delete(null, (String) null), 
                  "Should return null when string is null");
        assertNull(CharSetUtils.delete(null, EMPTY_STRING), 
                  "Should return null when string is null");

        // Test empty string handling
        assertEquals(EMPTY_STRING, CharSetUtils.delete(EMPTY_STRING, (String) null), 
                    "Should return empty string when input is empty");
        assertEquals(EMPTY_STRING, CharSetUtils.delete(EMPTY_STRING, EMPTY_STRING), 
                    "Should return empty string when input is empty");
        assertEquals(EMPTY_STRING, CharSetUtils.delete(EMPTY_STRING, RANGE_A_TO_E), 
                    "Should return empty string when input is empty");

        // Test normal operation
        assertEquals(HELLO, CharSetUtils.delete(HELLO, (String) null), 
                    "Should return original string when charset is null");
        assertEquals(HELLO, CharSetUtils.delete(HELLO, EMPTY_STRING), 
                    "Should return original string when charset is empty");
        assertEquals("hllo", CharSetUtils.delete(HELLO, RANGE_A_TO_E), 
                    "Should delete 'e' from 'hello' using range 'a-e'");
        assertEquals("he", CharSetUtils.delete(HELLO, RANGE_L_TO_P), 
                    "Should delete 'l', 'l', 'o' from 'hello' using range 'l-p'");
        assertEquals(HELLO, CharSetUtils.delete(HELLO, "z"), 
                    "Should return original string when no matching characters found");
    }

    @Test
    void testDelete_WithMultipleCharSets() {
        // Test null input handling
        assertNull(CharSetUtils.delete(null, (String[]) null), 
                  "Should return null when string is null");
        assertNull(CharSetUtils.delete(null), 
                  "Should return null when string is null");
        assertNull(CharSetUtils.delete(null, (String) null), 
                  "Should return null when string is null");
        assertNull(CharSetUtils.delete(null, CHARS_EL), 
                  "Should return null when string is null");

        // Test empty string handling
        assertEquals(EMPTY_STRING, CharSetUtils.delete(EMPTY_STRING, (String[]) null), 
                    "Should return empty string when input is empty");
        assertEquals(EMPTY_STRING, CharSetUtils.delete(EMPTY_STRING), 
                    "Should return empty string when input is empty");
        assertEquals(EMPTY_STRING, CharSetUtils.delete(EMPTY_STRING, (String) null), 
                    "Should return empty string when input is empty");
        assertEquals(EMPTY_STRING, CharSetUtils.delete(EMPTY_STRING, RANGE_A_TO_E), 
                    "Should return empty string when input is empty");

        // Test normal operation
        assertEquals(HELLO, CharSetUtils.delete(HELLO, (String[]) null), 
                    "Should return original string when charset array is null");
        assertEquals(HELLO, CharSetUtils.delete(HELLO), 
                    "Should return original string when no charsets provided");
        assertEquals(HELLO, CharSetUtils.delete(HELLO, (String) null), 
                    "Should return original string when charset is null");
        assertEquals(HELLO, CharSetUtils.delete(HELLO, "xyz"), 
                    "Should return original string when no matching characters found");

        // Test with specific character sets
        assertEquals("ho", CharSetUtils.delete(HELLO, CHARS_EL), 
                    "Should delete 'e', 'l', 'l' from 'hello'");
        assertEquals(EMPTY_STRING, CharSetUtils.delete(HELLO, "elho"), 
                    "Should delete all characters from 'hello'");
        assertEquals(HELLO, CharSetUtils.delete(HELLO, EMPTY_STRING), 
                    "Should return original string when charset is empty");
        assertEquals(EMPTY_STRING, CharSetUtils.delete(HELLO, RANGE_A_TO_Z), 
                    "Should delete all letters from 'hello'");
        assertEquals(EMPTY_STRING, CharSetUtils.delete("----", "-"), 
                    "Should delete all dashes");
        assertEquals("heo", CharSetUtils.delete(HELLO, "l"), 
                    "Should delete all 'l' characters from 'hello'");
    }

    @Test
    void testKeep_WithSingleCharSet() {
        // Test null input handling
        assertNull(CharSetUtils.keep(null, (String) null), 
                  "Should return null when string is null");
        assertNull(CharSetUtils.keep(null, EMPTY_STRING), 
                  "Should return null when string is null");

        // Test empty string handling
        assertEquals(EMPTY_STRING, CharSetUtils.keep(EMPTY_STRING, (String) null), 
                    "Should return empty string when input is empty");
        assertEquals(EMPTY_STRING, CharSetUtils.keep(EMPTY_STRING, EMPTY_STRING), 
                    "Should return empty string when input is empty");
        assertEquals(EMPTY_STRING, CharSetUtils.keep(EMPTY_STRING, RANGE_A_TO_E), 
                    "Should return empty string when input is empty");

        // Test normal operation
        assertEquals(EMPTY_STRING, CharSetUtils.keep(HELLO, (String) null), 
                    "Should return empty string when charset is null");
        assertEquals(EMPTY_STRING, CharSetUtils.keep(HELLO, EMPTY_STRING), 
                    "Should return empty string when charset is empty");
        assertEquals(EMPTY_STRING, CharSetUtils.keep(HELLO, "xyz"), 
                    "Should return empty string when no matching characters found");
        assertEquals(HELLO, CharSetUtils.keep(HELLO, RANGE_A_TO_Z), 
                    "Should keep all letters in 'hello'");
        assertEquals(HELLO, CharSetUtils.keep(HELLO, "oleh"), 
                    "Should keep all characters that match 'oleh' in 'hello'");
        assertEquals("ell", CharSetUtils.keep(HELLO, CHARS_EL), 
                    "Should keep only 'e', 'l', 'l' from 'hello'");
    }

    @Test
    void testKeep_WithMultipleCharSets() {
        // Test null input handling
        assertNull(CharSetUtils.keep(null, (String[]) null), 
                  "Should return null when string is null");
        assertNull(CharSetUtils.keep(null), 
                  "Should return null when string is null");
        assertNull(CharSetUtils.keep(null, (String) null), 
                  "Should return null when string is null");
        assertNull(CharSetUtils.keep(null, RANGE_A_TO_E), 
                  "Should return null when string is null");

        // Test empty string handling
        assertEquals(EMPTY_STRING, CharSetUtils.keep(EMPTY_STRING, (String[]) null), 
                    "Should return empty string when input is empty");
        assertEquals(EMPTY_STRING, CharSetUtils.keep(EMPTY_STRING), 
                    "Should return empty string when input is empty");
        assertEquals(EMPTY_STRING, CharSetUtils.keep(EMPTY_STRING, (String) null), 
                    "Should return empty string when input is empty");
        assertEquals(EMPTY_STRING, CharSetUtils.keep(EMPTY_STRING, RANGE_A_TO_E), 
                    "Should return empty string when input is empty");

        // Test normal operation
        assertEquals(EMPTY_STRING, CharSetUtils.keep(HELLO, (String[]) null), 
                    "Should return empty string when charset array is null");
        assertEquals(EMPTY_STRING, CharSetUtils.keep(HELLO), 
                    "Should return empty string when no charsets provided");
        assertEquals(EMPTY_STRING, CharSetUtils.keep(HELLO, (String) null), 
                    "Should return empty string when charset is null");
        assertEquals("e", CharSetUtils.keep(HELLO, RANGE_A_TO_E), 
                    "Should keep only 'e' from 'hello' using range 'a-e'");

        // Test with specific character sets
        assertEquals("e", CharSetUtils.keep(HELLO, RANGE_A_TO_E), 
                    "Should keep only 'e' from 'hello' using range 'a-e'");
        assertEquals("ell", CharSetUtils.keep(HELLO, CHARS_EL), 
                    "Should keep 'e', 'l', 'l' from 'hello'");
        assertEquals(HELLO, CharSetUtils.keep(HELLO, "elho"), 
                    "Should keep all characters from 'hello' that match 'elho'");
        assertEquals(HELLO, CharSetUtils.keep(HELLO, RANGE_A_TO_Z), 
                    "Should keep all letters from 'hello'");
        assertEquals("----", CharSetUtils.keep("----", "-"), 
                    "Should keep all dashes");
        assertEquals("ll", CharSetUtils.keep(HELLO, "l"), 
                    "Should keep only 'l' characters from 'hello'");
    }

    @Test
    void testSqueeze_WithSingleCharSet() {
        // Test null input handling
        assertNull(CharSetUtils.squeeze(null, (String) null), 
                  "Should return null when string is null");
        assertNull(CharSetUtils.squeeze(null, EMPTY_STRING), 
                  "Should return null when string is null");

        // Test empty string handling
        assertEquals(EMPTY_STRING, CharSetUtils.squeeze(EMPTY_STRING, (String) null), 
                    "Should return empty string when input is empty");
        assertEquals(EMPTY_STRING, CharSetUtils.squeeze(EMPTY_STRING, EMPTY_STRING), 
                    "Should return empty string when input is empty");
        assertEquals(EMPTY_STRING, CharSetUtils.squeeze(EMPTY_STRING, RANGE_A_TO_E), 
                    "Should return empty string when input is empty");

        // Test normal operation
        assertEquals(HELLO, CharSetUtils.squeeze(HELLO, (String) null), 
                    "Should return original string when charset is null");
        assertEquals(HELLO, CharSetUtils.squeeze(HELLO, EMPTY_STRING), 
                    "Should return original string when charset is empty");
        assertEquals(HELLO, CharSetUtils.squeeze(HELLO, RANGE_A_TO_E), 
                    "Should return original string when no consecutive duplicates in charset");
        assertEquals("helo", CharSetUtils.squeeze(HELLO, RANGE_L_TO_P), 
                    "Should squeeze consecutive 'l' characters in 'hello'");
        assertEquals("heloo", CharSetUtils.squeeze("helloo", "l"), 
                    "Should squeeze consecutive 'l' characters but not 'o'");
        assertEquals("hello", CharSetUtils.squeeze("helloo", "^l"), 
                    "Should squeeze consecutive characters except 'l' (so 'oo' becomes 'o')");
    }

    @Test
    void testSqueeze_WithMultipleCharSets() {
        // Test null input handling
        assertNull(CharSetUtils.squeeze(null, (String[]) null), 
                  "Should return null when string is null");
        assertNull(CharSetUtils.squeeze(null), 
                  "Should return null when string is null");
        assertNull(CharSetUtils.squeeze(null, (String) null), 
                  "Should return null when string is null");
        assertNull(CharSetUtils.squeeze(null, CHARS_EL), 
                  "Should return null when string is null");

        // Test empty string handling
        assertEquals(EMPTY_STRING, CharSetUtils.squeeze(EMPTY_STRING, (String[]) null), 
                    "Should return empty string when input is empty");
        assertEquals(EMPTY_STRING, CharSetUtils.squeeze(EMPTY_STRING), 
                    "Should return empty string when input is empty");
        assertEquals(EMPTY_STRING, CharSetUtils.squeeze(EMPTY_STRING, (String) null), 
                    "Should return empty string when input is empty");
        assertEquals(EMPTY_STRING, CharSetUtils.squeeze(EMPTY_STRING, RANGE_A_TO_E), 
                    "Should return empty string when input is empty");

        // Test normal operation
        assertEquals(HELLO, CharSetUtils.squeeze(HELLO, (String[]) null), 
                    "Should return original string when charset array is null");
        assertEquals(HELLO, CharSetUtils.squeeze(HELLO), 
                    "Should return original string when no charsets provided");
        assertEquals(HELLO, CharSetUtils.squeeze(HELLO, (String) null), 
                    "Should return original string when charset is null");
        assertEquals(HELLO, CharSetUtils.squeeze(HELLO, RANGE_A_TO_E), 
                    "Should return original string when no consecutive duplicates in charset");

        // Test with specific character sets
        assertEquals("helo", CharSetUtils.squeeze(HELLO, CHARS_EL), 
                    "Should squeeze consecutive 'l' characters in 'hello'");
        assertEquals(HELLO, CharSetUtils.squeeze(HELLO, "e"), 
                    "Should return original string when no consecutive 'e' characters");
        assertEquals("fofof", CharSetUtils.squeeze("fooffooff", "of"), 
                    "Should squeeze consecutive 'o' and 'f' characters");
        assertEquals("fof", CharSetUtils.squeeze("fooooff", "fo"), 
                    "Should squeeze consecutive 'f' and 'o' characters");
    }
}
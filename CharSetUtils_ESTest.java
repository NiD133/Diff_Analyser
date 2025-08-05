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

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSetUtils}.
 */
public class CharSetUtilsTest {

    //--- Squeeze Tests ----------------------------------------------------

    @Test
    public void squeeze_withNullString_shouldReturnNull() {
        assertNull(CharSetUtils.squeeze(null, "a-z"));
    }

    @Test
    public void squeeze_withEmptyString_shouldReturnEmptyString() {
        assertEquals("", CharSetUtils.squeeze("", "a-z"));
    }

    @Test
    public void squeeze_withNullSet_shouldReturnOriginalString() {
        assertEquals("hello", CharSetUtils.squeeze("hello", (String[]) null));
    }

    @Test
    public void squeeze_withEmptySet_shouldReturnOriginalString() {
        assertEquals("hello", CharSetUtils.squeeze("hello", ""));
    }

    @Test
    public void squeeze_withRepeatingCharsInSet_shouldSqueezeThem() {
        assertEquals("helo", CharSetUtils.squeeze("hello", "l"));
        assertEquals("ofset canot be negative", CharSetUtils.squeeze("offset cannot be negative", "fon"));
    }

    @Test
    public void squeeze_withRepeatingCharsNotInSet_shouldNotChangeString() {
        assertEquals("hello", CharSetUtils.squeeze("hello", "x"));
        assertEquals("...", CharSetUtils.squeeze("...", "a-z"));
    }
    
    @Test
    public void squeeze_withNegatedSet_shouldSqueezeCharsNotInSet() {
        // Squeezes all characters that are NOT 'l' or space.
        assertEquals("h llo worl d", CharSetUtils.squeeze("hello   world", "^l "));
    }

    //--- Delete Tests -----------------------------------------------------

    @Test
    public void delete_withNullString_shouldReturnNull() {
        assertNull(CharSetUtils.delete(null, "a-z"));
    }

    @Test
    public void delete_withEmptyString_shouldReturnEmptyString() {
        assertEquals("", CharSetUtils.delete("", "a-z"));
    }

    @Test
    public void delete_withNullSet_shouldReturnOriginalString() {
        assertEquals("hello", CharSetUtils.delete("hello", (String[]) null));
    }

    @Test
    public void delete_withEmptySet_shouldReturnOriginalString() {
        assertEquals("hello", CharSetUtils.delete("hello", ""));
    }

    @Test
    public void delete_withMatchingChars_shouldDeleteThem() {
        assertEquals("heo", CharSetUtils.delete("hello", "l"));
        assertEquals("heo world", CharSetUtils.delete("hello world", "l"));
    }

    @Test
    public void delete_withRangeSet_shouldDeleteMatchingChars() {
        assertEquals("-", CharSetUtils.delete("A-Z", "A-Z"));
        assertEquals("hllo", CharSetUtils.delete("hello", "a-e"));
    }

    @Test
    public void delete_withNegatedSet_shouldDeleteCharsNotInSet() {
        // Deletes all characters that are NOT 'l' or space.
        assertEquals("ll o", CharSetUtils.delete("hello world", "^l o"));
    }

    //--- Keep Tests -------------------------------------------------------

    @Test
    public void keep_withNullString_shouldReturnNull() {
        assertNull(CharSetUtils.keep(null, "a-z"));
    }

    @Test
    public void keep_withEmptyString_shouldReturnEmptyString() {
        assertEquals("", CharSetUtils.keep("", "a-z"));
    }

    @Test
    public void keep_withNullSet_shouldReturnEmptyString() {
        assertEquals("", CharSetUtils.keep("hello", (String[]) null));
    }

    @Test
    public void keep_withEmptySet_shouldReturnEmptyString() {
        assertEquals("", CharSetUtils.keep("hello", ""));
    }

    @Test
    public void keep_withMatchingChars_shouldKeepThem() {
        assertEquals("ll", CharSetUtils.keep("hello", "l"));
    }

    @Test
    public void keep_withRangeSet_shouldKeepMatchingChars() {
        assertEquals("AZ", CharSetUtils.keep("A-Z", "A-Z"));
        assertEquals("e", CharSetUtils.keep("hello", "a-e"));
    }

    //--- Count Tests ------------------------------------------------------

    @Test
    public void count_withNullString_shouldReturnZero() {
        assertEquals(0, CharSetUtils.count(null, "a-z"));
    }

    @Test
    public void count_withEmptyString_shouldReturnZero() {
        assertEquals(0, CharSetUtils.count("", "a-z"));
    }

    @Test
    public void count_withNullSet_shouldReturnZero() {
        assertEquals(0, CharSetUtils.count("hello", (String[]) null));
    }

    @Test
    public void count_withEmptySet_shouldReturnZero() {
        assertEquals(0, CharSetUtils.count("hello", ""));
    }

    @Test
    public void count_withMatchingChars_shouldReturnCorrectCount() {
        assertEquals(2, CharSetUtils.count("hello", "l"));
        assertEquals(3, CharSetUtils.count("hello", "e-l"));
    }

    @Test
    public void count_withNoMatchingChars_shouldReturnZero() {
        assertEquals(0, CharSetUtils.count("hello", "x-z"));
    }

    //--- ContainsAny Tests ------------------------------------------------

    @Test
    public void containsAny_withNullString_shouldReturnFalse() {
        assertFalse(CharSetUtils.containsAny(null, "a"));
    }

    @Test
    public void containsAny_withEmptyString_shouldReturnFalse() {
        assertFalse(CharSetUtils.containsAny("", "a"));
    }

    @Test
    public void containsAny_withNullSet_shouldReturnFalse() {
        assertFalse(CharSetUtils.containsAny("hello", (String[]) null));
    }

    @Test
    public void containsAny_withEmptySet_shouldReturnFalse() {
        assertFalse(CharSetUtils.containsAny("hello", ""));
    }

    @Test
    public void containsAny_withMatchingChar_shouldReturnTrue() {
        assertTrue(CharSetUtils.containsAny("hello", "l"));
        assertTrue(CharSetUtils.containsAny("hello", "a-z"));
    }

    @Test
    public void containsAny_withNoMatchingChar_shouldReturnFalse() {
        assertFalse(CharSetUtils.containsAny("hello", "x"));
        assertFalse(CharSetUtils.containsAny("hello", "A-Z"));
    }

    //--- Constructor Test -------------------------------------------------

    @Test
    public void constructor_shouldBePublicForBeanCompatibility() {
        // The constructor is public to allow tools that require a
        // JavaBean instance to operate. This test improves code coverage.
        new CharSetUtils();
    }
}
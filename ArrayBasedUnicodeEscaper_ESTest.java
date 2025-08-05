/*
 * Copyright (C) 2023 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.escape;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link ArrayBasedUnicodeEscaper}.
 *
 * <p>This test suite uses a concrete inner class, {@code TestEscaper}, to test the
 * functionality of the abstract {@code ArrayBasedUnicodeEscaper}. The {@code TestEscaper}
 * implements the abstract {@code escapeUnsafe} method to return a hex representation
 * of any character deemed unsafe, allowing for clear verification of the escaper's logic.
 */
@RunWith(JUnit4.class)
public class ArrayBasedUnicodeEscaperTest {

    /** A concrete implementation of ArrayBasedUnicodeEscaper for testing purposes. */
    private static class TestEscaper extends ArrayBasedUnicodeEscaper {
        protected TestEscaper(Map<Character, String> replacementMap, int safeMin, int safeMax) {
            // The 'unsafeReplacement' parameter is not used by the base class itself.
            super(replacementMap, safeMin, safeMax, null);
        }

        /**
         * For testing, represents unsafe characters with their uppercase hex value,
         * e.g., the character '!' (decimal 33) becomes "<21>".
         */
        @Override
        protected char[] escapeUnsafe(int cp) {
            return ("<" + Integer.toHexString(cp).toUpperCase() + ">").toCharArray();
        }
    }

    @Test
    public void escape_withSpecificReplacements_replacesChars() {
        // Arrange
        Map<Character, String> replacements = ImmutableMap.of('"', "&quot;", '&', "&amp;");
        // Safe range does not include the replacement characters.
        ArrayBasedUnicodeEscaper escaper = new TestEscaper(replacements, 'a', 'z');
        String input = "a \"quoted\" string & stuff";
        String expected = "a &quot;quoted&quot; string &amp; stuff";

        // Act
        String result = escaper.escape(input);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void escape_withCharsInSafeRange_leavesCharsUnchanged() {
        // Arrange
        ArrayBasedUnicodeEscaper escaper = new TestEscaper(Collections.emptyMap(), ' ', 'z');
        String input = "abc 123 XYZ"; // All characters are within the safe range.

        // Act
        String result = escaper.escape(input);

        // Assert
        assertEquals(input, result);
    }

    @Test
    public void escape_withCharsOutsideSafeRange_usesEscapeUnsafe() {
        // Arrange
        // Safe range is only for lowercase letters.
        ArrayBasedUnicodeEscaper escaper = new TestEscaper(Collections.emptyMap(), 'a', 'z');
        String input = "Hello World!";
        // H, ' ', W, and ! are unsafe. Their hex values are 48, 20, 57, and 21.
        String expected = "<48>ello<20><57>orld<21>";

        // Act
        String result = escaper.escape(input);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void escape_withMixedContent_handlesAllCasesCorrectly() {
        // Arrange
        Map<Character, String> replacements = ImmutableMap.of('<', "&lt;", '>', "&gt;");
        // Safe range is only for lowercase letters.
        ArrayBasedUnicodeEscaper escaper = new TestEscaper(replacements, 'a', 'z');
        String input = "a < b && c > d!";
        // '<' and '>' are replaced via the map.
        // 'a', 'b', 'c', 'd' are safe.
        // ' ' (0x20), '&' (0x26), '!' (0x21) are unsafe.
        String expected = "a<20>&lt;<20>b<20><26><26><20>c<20>&gt;<20>d<21>";

        // Act
        String result = escaper.escape(input);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void escape_withEmptySafeRange_treatsAllUnmappedCharsAsUnsafe() {
        // Arrange
        Map<Character, String> replacements = ImmutableMap.of('z', "ZZZ");
        // Create an empty safe range by making min > max.
        ArrayBasedUnicodeEscaper escaper = new TestEscaper(replacements, 'z', 'a');
        String input = "abc z";
        // 'a', 'b', 'c', ' ' are unsafe. 'z' is mapped.
        String expected = "<61><62><63><20>ZZZ";

        // Act
        String result = escaper.escape(input);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void escape_withSurrogatePair_escapesEntireCodePoint() {
        // Arrange
        // The safe range includes the individual surrogate characters but not the combined code point.
        ArrayBasedUnicodeEscaper escaper = new TestEscaper(Collections.emptyMap(), 0, 0xFFFF);
        // U+1F600 is a smiley face emoji, represented by the surrogate pair \uD83D\uDE00.
        // The code point 0x1F600 is outside the safe range [0, 0xFFFF] and should be escaped.
        String input = "Smile \uD83D\uDE00";
        String expected = "Smile<20><1F600>";

        // Act
        String result = escaper.escape(input);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void escape_withEmptyString_returnsEmptyString() {
        // Arrange
        ArrayBasedUnicodeEscaper escaper = new TestEscaper(Collections.emptyMap(), 0, 255);

        // Act
        String result = escaper.escape("");

        // Assert
        assertEquals("", result);
    }

    @Test
    public void escape_withNullInput_throwsNullPointerException() {
        // Arrange
        ArrayBasedUnicodeEscaper escaper = new TestEscaper(Collections.emptyMap(), 0, 255);

        // Act & Assert
        try {
            escaper.escape(null);
            fail("Expected NullPointerException for null input");
        } catch (NullPointerException expected) {
            // Test passes
        }
    }

    @Test
    public void constructor_withArrayBasedEscaperMap_behavesCorrectly() {
        // Arrange
        Map<Character, String> replacements = ImmutableMap.of('"', "&quot;");
        ArrayBasedEscaperMap escaperMap = ArrayBasedEscaperMap.create(replacements);

        // Use the constructor that accepts an ArrayBasedEscaperMap.
        ArrayBasedUnicodeEscaper escaper = new ArrayBasedUnicodeEscaper(escaperMap, 'a', 'z', null) {
            @Override
            protected char[] escapeUnsafe(int cp) {
                return ("<" + Integer.toHexString(cp).toUpperCase() + ">").toCharArray();
            }
        };

        String input = "a \"b\" c";
        String expected = "a<20>&quot;b&quot;<20>c";

        // Act
        String result = escaper.escape(input);

        // Assert
        assertEquals(expected, result);
    }
}
/*
 * Copyright (C) 2009 The Guava Authors
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

import static com.google.common.escape.ReflectionFreeAssertThrows.assertThrows;
import static com.google.common.truth.Truth.assertThat;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import com.google.common.escape.testing.EscaperAsserts;
import java.io.IOException;
import junit.framework.TestCase;
import org.jspecify.annotations.NullMarked;

/**
 * Tests for {@link ArrayBasedUnicodeEscaper}.
 * 
 * @author David Beaumont
 */
@GwtCompatible
@NullMarked
public class ArrayBasedUnicodeEscaperTest extends TestCase {
  
  // Test constants with descriptive names
  private static final ImmutableMap<Character, String> NO_REPLACEMENTS = ImmutableMap.of();
  private static final ImmutableMap<Character, String> SIMPLE_REPLACEMENTS =
      ImmutableMap.of(
          '\n', "<newline>",
          '\t', "<tab>",
          '&', "<and>");
  private static final char[] EMPTY_REPLACEMENT = new char[0];
  
  // Common test strings
  private static final String SAFE_UNICODE_CHARS = "\0\u0100\uD800\uDC00\uFFFF";
  private static final String MALFORMED_UNICODE = "\uDC00\uD800"; // Low surrogate before high
  
  public void testEscaper_withReplacementMap_escapesSpecifiedCharacters() throws IOException {
    // Given: An escaper with specific character replacements
    UnicodeEscaper escaper = createEscaperWithReplacements(SIMPLE_REPLACEMENTS);
    
    // When: Escaping text containing mapped characters
    String input = "\tFish & Chips\n";
    String result = escaper.escape(input);
    
    // Then: Mapped characters are replaced with their values
    assertThat(result).isEqualTo("<tab>Fish <and> Chips<newline>");
    
    // And: Unmapped characters remain unchanged
    assertThat(escaper.escape(SAFE_UNICODE_CHARS)).isEqualTo(SAFE_UNICODE_CHARS);
    
    // And: Basic escaper functionality works
    EscaperAsserts.assertBasic(escaper);
  }
  
  public void testEscaper_withMalformedUnicode_throwsIllegalArgumentException() {
    // Given: An escaper
    UnicodeEscaper escaper = createEscaperWithReplacements(SIMPLE_REPLACEMENTS);
    
    // When/Then: Escaping malformed Unicode throws exception
    assertThrows(IllegalArgumentException.class, () -> escaper.escape(MALFORMED_UNICODE));
  }

  public void testEscaper_withSafeRange_onlyEscapesCharactersOutsideRange() throws IOException {
    // Given: An escaper with safe range A-Z that wraps unsafe chars in braces
    final char safeRangeStart = 'A';
    final char safeRangeEnd = 'Z';
    UnicodeEscaper escaper = new ArrayBasedUnicodeEscaper(
        NO_REPLACEMENTS, safeRangeStart, safeRangeEnd, null) {
      @Override
      protected char[] escapeUnsafe(int codePoint) {
        return ("{" + (char) codePoint + "}").toCharArray();
      }
    };
    
    // When: Escaping text with characters inside and outside safe range
    String input = "[FOO@BAR]"; // '[' < 'A' and '@' < 'A', so both are unsafe
    String result = escaper.escape(input);
    
    // Then: Only unsafe characters are wrapped
    assertThat(result).isEqualTo("{[}FOO{@}BAR{]}");
    
    EscaperAsserts.assertBasic(escaper);
  }

  public void testEscaper_withDeletion_removesUnsafeCharacters() throws IOException {
    // Given: An escaper that deletes characters outside printable ASCII range (space to tilde)
    final char printableAsciiStart = ' ';
    final char printableAsciiEnd = '~';
    UnicodeEscaper escaper = new ArrayBasedUnicodeEscaper(
        NO_REPLACEMENTS, printableAsciiStart, printableAsciiEnd, null) {
      @Override
      protected char[] escapeUnsafe(int codePoint) {
        return EMPTY_REPLACEMENT; // Delete unsafe characters
      }
    };
    
    // When: Escaping text with non-printable characters
    String input = "\tEverything\0 outside the\uD800\uDC00 " +
                   "printable ASCII \uFFFFrange is \u007Fdeleted.\n";
    String result = escaper.escape(input);
    
    // Then: Non-printable characters are removed
    assertThat(result).isEqualTo("Everything outside the printable ASCII range is deleted.");
    
    EscaperAsserts.assertBasic(escaper);
  }

  public void testEscaper_withBothReplacementsAndSafeRange_prioritizesReplacements() 
      throws IOException {
    // Given: An escaper with both replacements and a safe range
    final char safeRangeStart = ' ';
    final char safeRangeEnd = '~';
    UnicodeEscaper escaper = new ArrayBasedUnicodeEscaper(
        SIMPLE_REPLACEMENTS, safeRangeStart, safeRangeEnd, null) {
      @Override
      protected char[] escapeUnsafe(int codePoint) {
        return new char[] {'?'}; // Replace unsafe chars with '?'
      }
    };
    
    // When: Escaping text with both mapped and unmapped characters
    // Note: '&' is within safe range but has a replacement mapping
    //       '\t' and '\n' are outside safe range and have replacements
    //       '\0' and '\r' are outside safe range without replacements
    String input = "\tFish &\0 Chips\r\n";
    String result = escaper.escape(input);
    
    // Then: Replacements take priority over safe range logic
    assertThat(result).isEqualTo("<tab>Fish <and>? Chips?<newline>");
    
    EscaperAsserts.assertBasic(escaper);
  }

  public void testEscaper_withSurrogatePairs_handlesCodePointsCorrectly() throws IOException {
    // Given: An escaper with safe range up to 0x20000 (beyond BMP)
    final int safeRangeStart = 0;
    final int safeRangeEnd = 0x20000;
    UnicodeEscaper escaper = new ArrayBasedUnicodeEscaper(
        NO_REPLACEMENTS, safeRangeStart, safeRangeEnd, null) {
      @Override
      protected char[] escapeUnsafe(int codePoint) {
        return new char[] {'X'};
      }
    };
    
    // Test case 1: Surrogate pair representing code point within safe range
    String safeCodePoint = "\uD800\uDC00"; // Represents U+10000
    assertThat(escaper.escape(safeCodePoint)).isEqualTo(safeCodePoint);
    
    // Test case 2: Surrogate pair representing code point outside safe range
    // Important: Both surrogate chars are < 0x20000, but the code point they
    // represent (U+10FFFF) is > 0x20000
    String unsafeCodePoint = "\uDBFF\uDFFF"; // Represents U+10FFFF
    assertThat(escaper.escape(unsafeCodePoint)).isEqualTo("X");
    
    EscaperAsserts.assertBasic(escaper);
  }
  
  // Helper method to create a simple escaper with replacements
  private static UnicodeEscaper createEscaperWithReplacements(
      ImmutableMap<Character, String> replacements) {
    return new ArrayBasedUnicodeEscaper(
        replacements, Character.MIN_VALUE, Character.MAX_CODE_POINT, null) {
      @Override
      protected char[] escapeUnsafe(int codePoint) {
        return EMPTY_REPLACEMENT;
      }
    };
  }
}
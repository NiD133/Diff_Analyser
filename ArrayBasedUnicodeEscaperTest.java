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
 * @author David Beaumont
 */
@GwtCompatible
@NullMarked
public class ArrayBasedUnicodeEscaperTest extends TestCase {
  private static final ImmutableMap<Character, String> NO_REPLACEMENTS = ImmutableMap.of();
  private static final ImmutableMap<Character, String> SIMPLE_REPLACEMENTS =
      ImmutableMap.of(
          '\n', "<newline>",
          '\t', "<tab>",
          '&', "<and>");
  private static final char[] NO_CHARS = new char[0];
  
  // Test constants
  private static final String SAFE_UNICODE_CHARS = "\0\u0100\uD800\uDC00\uFFFF";
  private static final String INVALID_SURROGATE_PAIR = "\uDC00\uD800";
  private static final String SAFE_SURROGATE_PAIR = "\uD800\uDC00";  // U+10000
  private static final String UNSAFE_SURROGATE_PAIR = "\uDBFF\uDFFF"; // U+10FFFF

  public void testSimpleReplacementsAndSafeChars() throws IOException {
    UnicodeEscaper escaper = createSimpleReplacementEscaper();
    EscaperAsserts.assertBasic(escaper);

    // Verify map replacements
    assertThat(escaper.escape("\tFish & Chips\n")).isEqualTo("<tab>Fish <and> Chips<newline>");

    // Verify safe characters remain unchanged
    assertThat(escaper.escape(SAFE_UNICODE_CHARS)).isEqualTo(SAFE_UNICODE_CHARS);

    // Verify invalid Unicode handling
    assertThrows(IllegalArgumentException.class, () -> escaper.escape(INVALID_SURROGATE_PAIR));
  }

  public void testSafeRangeHandling() throws IOException {
    UnicodeEscaper escaper = createWrappingEscaper();
    EscaperAsserts.assertBasic(escaper);

    // Characters outside [A-Z] should be wrapped, others remain unchanged
    assertThat(escaper.escape("[FOO@BAR]")).isEqualTo("{[}FOO{@}BAR{]}");
  }

  public void testUnsafeCharacterDeletion() throws IOException {
    UnicodeEscaper escaper = createDeletingEscaper();
    EscaperAsserts.assertBasic(escaper);

    String input = "\tEverything\0 outside the\uD800\uDC00 " 
        + "printable ASCII \uFFFFrange is \u007Fdeleted.\n";
    String expected = "Everything outside the printable ASCII range is deleted.";
    assertThat(escaper.escape(input)).isEqualTo(expected);
  }

  public void testReplacementPriority() throws IOException {
    UnicodeEscaper escaper = createPriorityTestEscaper();
    EscaperAsserts.assertBasic(escaper);

    // Verify replacements take priority over safe-range checks
    String input = "\tFish &\0 Chips\r\n";
    String expected = "<tab>Fish <and>? Chips?<newline>";
    assertThat(escaper.escape(input)).isEqualTo(expected);
  }

  public void testSurrogatePairHandling() throws IOException {
    UnicodeEscaper escaper = createSurrogateEscaper();
    EscaperAsserts.assertBasic(escaper);

    // Safe surrogate pair should remain unchanged
    assertThat(escaper.escape(SAFE_SURROGATE_PAIR)).isEqualTo(SAFE_SURROGATE_PAIR);
    
    // Unsafe surrogate pair should be escaped
    assertThat(escaper.escape(UNSAFE_SURROGATE_PAIR)).isEqualTo("X");
  }

  // Helper methods for test setup
  private UnicodeEscaper createSimpleReplacementEscaper() {
    return new ArrayBasedUnicodeEscaper(
        SIMPLE_REPLACEMENTS, Character.MIN_VALUE, Character.MAX_CODE_POINT, null) {
      @Override
      protected char[] escapeUnsafe(int c) {
        return NO_CHARS;
      }
    };
  }

  private UnicodeEscaper createWrappingEscaper() {
    return new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, 'A', 'Z', null) {
      @Override
      protected char[] escapeUnsafe(int c) {
        return ("{" + (char) c + "}").toCharArray();
      }
    };
  }

  private UnicodeEscaper createDeletingEscaper() {
    return new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, ' ', '~', null) {
      @Override
      protected char[] escapeUnsafe(int c) {
        return NO_CHARS;
      }
    };
  }

  private UnicodeEscaper createPriorityTestEscaper() {
    return new ArrayBasedUnicodeEscaper(SIMPLE_REPLACEMENTS, ' ', '~', null) {
      private final char[] unknown = {'?'};

      @Override
      protected char[] escapeUnsafe(int c) {
        return unknown;
      }
    };
  }

  private UnicodeEscaper createSurrogateEscaper() {
    return new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, 0, 0x20000, null) {
      private final char[] escaped = {'X'};

      @Override
      protected char[] escapeUnsafe(int c) {
        return escaped;
      }
    };
  }
}
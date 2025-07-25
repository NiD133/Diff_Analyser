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
 * <p>This class focuses on testing the core functionality of {@link ArrayBasedUnicodeEscaper},
 * including handling replacements, safe ranges, and surrogate pairs.  It uses anonymous subclasses
 * to define the escaping behavior for specific test cases.
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

  /**
   * Tests that characters specified in the replacement map are correctly escaped, and that
   * other characters are left unescaped.  Also verifies correct handling of badly formed
   * Unicode input (surrogate pairs).
   */
  public void testReplacements() throws IOException {
    UnicodeEscaper escaper =
        new ArrayBasedUnicodeEscaper(
            SIMPLE_REPLACEMENTS, Character.MIN_VALUE, Character.MAX_CODE_POINT, null) {
          @Override
          protected char[] escapeUnsafe(int c) {
            return NO_CHARS; // Don't escape anything else.
          }
        };

    EscaperAsserts.assertBasic(escaper); // Sanity check the escaper.
    assertThat(escaper.escape("\tFish & Chips\n"))
        .isEqualTo("<tab>Fish <and> Chips<newline>");

    // Verify that everything else is left unescaped.
    String safeChars = "\0\u0100\uD800\uDC00\uFFFF";
    assertThat(escaper.escape(safeChars)).isEqualTo(safeChars);

    // Ensure that Unicode escapers behave correctly wrt badly formed input.
    String badUnicode = "\uDC00\uD800";
    assertThrows(
        IllegalArgumentException.class,
        () -> escaper.escape(badUnicode)); // Surrogate pairs must be valid.
  }

  /**
   * Tests that the safe range functionality works as expected.  Characters within the safe
   * range are left unescaped, while others are escaped using the {@link #escapeUnsafe} method.
   */
  public void testSafeRange() throws IOException {
    // Basic escaping of unsafe chars (wrap them in {,}'s)
    UnicodeEscaper wrappingEscaper =
        new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, 'A', 'Z', null) {
          @Override
          protected char[] escapeUnsafe(int c) {
            return ("{" + (char) c + "}").toCharArray();
          }
        };
    EscaperAsserts.assertBasic(wrappingEscaper);
    // '[' and '@' lie either side of [A-Z].
    assertThat(wrappingEscaper.escape("[FOO@BAR]")).isEqualTo("{[}FOO{@}BAR{]}");
  }

  /**
   * Tests that characters outside the safe range can be deleted by returning an empty char array
   * from {@link #escapeUnsafe}.
   */
  public void testDeleteUnsafeChars() throws IOException {
    UnicodeEscaper deletingEscaper =
        new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, ' ', '~', null) {
          @Override
          protected char[] escapeUnsafe(int c) {
            return NO_CHARS; // Delete unsafe characters.
          }
        };
    EscaperAsserts.assertBasic(deletingEscaper);
    assertThat(
            deletingEscaper.escape(
                "\tEverything\0 outside the\uD800\uDC00 "
                    + "printable ASCII \uFFFFrange is \u007Fdeleted.\n"))
        .isEqualTo("Everything outside the printable ASCII range is deleted.");
  }

  /**
   * Tests the priority of replacements. Characters that have explicit replacements in the
   * replacement map should be replaced *before* any safe range checks.
   */
  public void testReplacementPriority() throws IOException {
    UnicodeEscaper replacingEscaper =
        new ArrayBasedUnicodeEscaper(SIMPLE_REPLACEMENTS, ' ', '~', null) {
          private final char[] unknown = new char[] {'?'};

          @Override
          protected char[] escapeUnsafe(int c) {
            return unknown; // Replace all other unsafe characters with '?'.
          }
        };
    EscaperAsserts.assertBasic(replacingEscaper);

    // Replacements are applied first regardless of whether the character is in
    // the safe range or not ('&' is a safe char while '\t' and '\n' are not).
    assertThat(replacingEscaper.escape("\tFish &\0 Chips\r\n"))
        .isEqualTo("<tab>Fish <and>? Chips?<newline>");
  }

  /**
   * Tests that the escaper correctly handles code points represented by surrogate pairs.
   * It verifies that surrogate pairs within the safe range are not escaped, and those
   * outside the safe range are escaped correctly.
   */
  public void testCodePointsFromSurrogatePairs() throws IOException {
    UnicodeEscaper surrogateEscaper =
        new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, 0, 0x20000, null) {
          private final char[] escaped = new char[] {'X'};

          @Override
          protected char[] escapeUnsafe(int c) {
            return escaped; // Replace all unsafe characters with 'X'.
          }
        };
    EscaperAsserts.assertBasic(surrogateEscaper);

    // A surrogate pair defining a code point within the safe range.
    String safeInput = "\uD800\uDC00"; // 0x10000
    assertThat(surrogateEscaper.escape(safeInput)).isEqualTo(safeInput);

    // A surrogate pair defining a code point outside the safe range (but both
    // of the surrogate characters lie within the safe range). It is important
    // not to accidentally treat this as a sequence of safe characters.
    String unsafeInput = "\uDBFF\uDFFF"; // 0x10FFFF
    assertThat(surrogateEscaper.escape(unsafeInput)).isEqualTo("X");
  }
}
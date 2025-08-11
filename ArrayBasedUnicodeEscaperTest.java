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
import java.util.Map;
import org.jspecify.annotations.NullMarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link ArrayBasedUnicodeEscaper}.
 *
 * @author David Beaumont
 */
@GwtCompatible
@NullMarked
@RunWith(JUnit4.class)
public class ArrayBasedUnicodeEscaperTest {
  private static final ImmutableMap<Character, String> NO_REPLACEMENTS = ImmutableMap.of();
  private static final ImmutableMap<Character, String> SIMPLE_REPLACEMENTS =
      ImmutableMap.of(
          '\n', "<newline>",
          '\t', "<tab>",
          '&', "<and>");
  private static final char[] NO_CHARS = new char[0];

  // The original test combined three distinct assertions. They are now separated for clarity.

  @Test
  public void escape_withReplacements_replacesDefinedCharacters() {
    // This escaper has a wide safe range, so only the replacement map is ever used.
    UnicodeEscaper escaper = createReplacementsOnlyEscaper(SIMPLE_REPLACEMENTS);

    assertThat(escaper.escape("\tFish & Chips\n"))
        .isEqualTo("<tab>Fish <and> Chips<newline>");
  }

  @Test
  public void escape_withReplacements_leavesOtherCharactersUnchanged() {
    UnicodeEscaper escaper = createReplacementsOnlyEscaper(SIMPLE_REPLACEMENTS);

    // A mix of characters not in the replacement map, including null, non-ASCII,
    // and a surrogate pair forming a supplementary code point. All should be unaffected.
    String safeChars = "\0\u0100\uD800\uDC00\uFFFF";
    assertThat(escaper.escape(safeChars)).isEqualTo(safeChars);
  }

  @Test
  public void escape_withMalformedUnicode_throwsException() {
    // Any escaper instance can be used to test this universal behavior.
    UnicodeEscaper escaper = createReplacementsOnlyEscaper(NO_REPLACEMENTS);

    // An isolated low surrogate followed by a high one is invalid.
    String badUnicode = "\uDC00\uD800";
    assertThrows(IllegalArgumentException.class, () -> escaper.escape(badUnicode));
  }

  @Test
  public void escape_withSafeRange_escapesUnsafeCharactersAndLeavesSafeCharacters() {
    // This escaper wraps any character outside the 'A'-'Z' range in braces.
    UnicodeEscaper escaper = createWrappingEscaper('A', 'Z');

    // '[' and '@' are adjacent to the safe range 'A'-'Z' and should be escaped.
    String input = "[FOO@BAR]";
    String expected = "{[}FOO{@}BAR{]}";

    assertThat(escaper.escape(input)).isEqualTo(expected);
  }

  @Test
  public void escape_withDeletingEscaper_removesUnsafeCharacters() {
    // This escaper has a safe range of printable ASCII (' ' to '~') and deletes other characters.
    UnicodeEscaper escaper = createDeletingEscaper(' ', '~');

    String input =
        "\tEverything\0 outside the\uD800\uDC00 " // U+10000 is unsafe
            + "printable ASCII \uFFFFrange is \u007Fdeleted.\n";
    String expected = "Everything outside the printable ASCII range is deleted.";

    assertThat(escaper.escape(input)).isEqualTo(expected);
  }

  @Test
  public void escape_replacementMapHasPriorityOverSafeRange() {
    // This escaper has:
    // 1. A replacement map for '\t', '\n', '&'.
    // 2. A safe range of printable ASCII (' ' to '~').
    // 3. A fallback to escape other unsafe characters as '?'.
    UnicodeEscaper escaper = createPrioritizedEscaper(SIMPLE_REPLACEMENTS, ' ', '~');

    // We expect '&' to be replaced via the map, even though it is in the safe range.
    // We expect '\t' and '\n' to be replaced via the map because they are unsafe.
    // We expect '\0' and '\r' to be escaped to '?' because they are unsafe and not in the map.
    String input = "\tFish &\0 Chips\r\n";
    String expected = "<tab>Fish <and>? Chips?<newline>";

    assertThat(escaper.escape(input)).isEqualTo(expected);
  }

  // The original test for surrogate pairs is split into two focused tests.

  @Test
  public void escape_surrogatePairFormingSafeCodePoint_isNotEscaped() {
    // The safe range is up to 0x20000. Unsafe code points are replaced with 'X'.
    UnicodeEscaper escaper = createSurrogateTestEscaper(0x20000);

    // This surrogate pair forms code point U+10000, which is in the safe range.
    // The escaper must correctly interpret the pair as one code point and not escape it.
    String safeSurrogatePair = "\uD800\uDC00"; // U+10000
    assertThat(escaper.escape(safeSurrogatePair)).isEqualTo(safeSurrogatePair);
  }

  @Test
  public void escape_surrogatePairFormingUnsafeCodePoint_isEscaped() {
    // The safe range is up to 0x20000. Unsafe code points are replaced with 'X'.
    UnicodeEscaper escaper = createSurrogateTestEscaper(0x20000);

    // This surrogate pair forms code point U+10FFFF, which is outside the safe range.
    // It's critical that the escaper doesn't treat the individual char values
    // (\uDBFF and \uDFFF), which are technically in the safe range, as safe.
    String unsafeSurrogatePair = "\uDBFF\uDFFF"; // U+10FFFF
    assertThat(escaper.escape(unsafeSurrogatePair)).isEqualTo("X");
  }

  // Helper methods to create specific escaper instances for tests.

  /**
   * Returns an escaper that only uses the replacement map. The safe range is all-inclusive, so
   * {@code escapeUnsafe} is never called. This isolates tests to the replacement map behavior.
   */
  private static UnicodeEscaper createReplacementsOnlyEscaper(Map<Character, String> replacements) {
    return new ArrayBasedUnicodeEscaper(
        replacements, Character.MIN_VALUE, Character.MAX_CODE_POINT, null) {
      @Override
      protected char[] escapeUnsafe(int c) {
        // This should not be called given the all-inclusive safe range.
        throw new AssertionError("escapeUnsafe should not be called");
      }
    };
  }

  /** Returns an escaper that wraps unsafe characters in curly braces. */
  private static UnicodeEscaper createWrappingEscaper(char safeMin, char safeMax) {
    return new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, safeMin, safeMax, null) {
      @Override
      protected char[] escapeUnsafe(int c) {
        return ("{" + (char) c + "}").toCharArray();
      }
    };
  }

  /** Returns an escaper that deletes unsafe characters. */
  private static UnicodeEscaper createDeletingEscaper(char safeMin, char safeMax) {
    return new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, safeMin, safeMax, null) {
      @Override
      protected char[] escapeUnsafe(int c) {
        return NO_CHARS; // Escape by deleting
      }
    };
  }

  /**
   * Returns an escaper that uses a replacement map, a safe range, and a fallback replacement ('?')
   * for other unsafe characters.
   */
  private static UnicodeEscaper createPrioritizedEscaper(
      Map<Character, String> replacements, char safeMin, char safeMax) {
    return new ArrayBasedUnicodeEscaper(replacements, safeMin, safeMax, null) {
      private final char[] UNKNOWN_REPLACEMENT = {'?'};

      @Override
      protected char[] escapeUnsafe(int c) {
        return UNKNOWN_REPLACEMENT;
      }
    };
  }

  /**
   * Returns an escaper with a specific safe range for testing surrogate pair handling, replacing
   * unsafe code points with 'X'.
   */
  private static UnicodeEscaper createSurrogateTestEscaper(int safeMax) {
    return new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, 0, safeMax, null) {
      private final char[] REPLACEMENT = {'X'};

      @Override
      protected char[] escapeUnsafe(int c) {
        return REPLACEMENT;
      }
    };
  }
}
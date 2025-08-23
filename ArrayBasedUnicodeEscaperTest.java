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
 * Tests for ArrayBasedUnicodeEscaper focused on:
 * - applying explicit replacements
 * - honoring safe ranges
 * - behavior of the fallback escaping strategy
 * - correct handling of surrogate pairs (well-formed and ill-formed)
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
  private static final char[] QUESTION_MARK = new char[] {'?'};
  private static final char[] LITERAL_X = new char[] {'X'};

  public void testReplacements() throws IOException {
    // An escaper whose entire Unicode range is considered safe. Only explicit replacements apply.
    UnicodeEscaper escaper =
        newEscaperWith(SIMPLE_REPLACEMENTS, Character.MIN_VALUE, Character.MAX_CODE_POINT, NO_CHARS);

    EscaperAsserts.assertBasic(escaper);

    // Explicit replacements should always apply, even if the char is within the safe range.
    assertThat(escaper.escape("\tFish & Chips\n")).isEqualTo("<tab>Fish <and> Chips<newline>");

    // Everything else stays unchanged (safe range covers all code points).
    String unchanged = "\0\u0100\uD800\uDC00\uFFFF";
    assertThat(escaper.escape(unchanged)).isEqualTo(unchanged);

    // Ill-formed surrogate pair must throw.
    String illFormedSurrogatePair = "\uDC00\uD800";
    assertThrows(IllegalArgumentException.class, () -> escaper.escape(illFormedSurrogatePair));
  }

  public void testSafeRange_wrapUnsafeChars() throws IOException {
    // Safe range: 'A'..'Z'. Anything outside gets wrapped in { }.
    UnicodeEscaper wrappingEscaper = wrappingEscaper('A', 'Z');

    EscaperAsserts.assertBasic(wrappingEscaper);

    // '[' and '@' are unsafe; characters in [A-Z] are safe; ']' is unsafe.
    assertThat(wrappingEscaper.escape("[FOO@BAR]")).isEqualTo("{[}FOO{@}BAR{]}");
  }

  public void testDeleteUnsafeChars() throws IOException {
    // Safe range: printable ASCII (' ' .. '~'). Anything else is deleted.
    UnicodeEscaper deletingEscaper = newEscaperWith(NO_REPLACEMENTS, ' ', '~', NO_CHARS);

    EscaperAsserts.assertBasic(deletingEscaper);

    String input =
        "\t" // delete (tab)
            + "Everything"
            + "\0" // delete (NUL)
            + " outside the"
            + "\uD800\uDC00" // delete (U+10000, outside printable ASCII)
            + " printable ASCII "
            + "\uFFFF" // delete
            + "range is "
            + "\u007F" // delete (DEL)
            + "deleted."
            + "\n"; // delete (LF)

    assertThat(deletingEscaper.escape(input))
        .isEqualTo("Everything outside the printable ASCII range is deleted.");
  }

  public void testReplacementPriority() throws IOException {
    // Safe range: printable ASCII (' ' .. '~').
    // Fallback: replace unsafe characters with '?'.
    UnicodeEscaper replacingEscaper =
        newEscaperWith(SIMPLE_REPLACEMENTS, ' ', '~', QUESTION_MARK);

    EscaperAsserts.assertBasic(replacingEscaper);

    // Replacements take priority over safe range checks. '&' is safe but still replaced.
    // '\t' and '\n' replaced by map; NUL and CR replaced by fallback '?'.
    assertThat(replacingEscaper.escape("\tFish &\0 Chips\r\n"))
        .isEqualTo("<tab>Fish <and>? Chips?<newline>");
  }

  public void testCodePointsFromSurrogatePairs() throws IOException {
    // Safe range: [0 .. 0x20000]. Fallback: 'X'.
    UnicodeEscaper surrogateEscaper = newEscaperWith(NO_REPLACEMENTS, 0, 0x20000, LITERAL_X);

    EscaperAsserts.assertBasic(surrogateEscaper);

    // Well-formed surrogate pair that maps to a safe code point (U+10000).
    String safePair = "\uD800\uDC00";
    assertThat(surrogateEscaper.escape(safePair)).isEqualTo(safePair);

    // Well-formed surrogate pair that maps to an unsafe code point (U+10FFFF).
    // Even though each surrogate char is within the char-range check, the pair must be treated
    // as a single code point and escaped by the fallback.
    String unsafePair = "\uDBFF\uDFFF";
    assertThat(surrogateEscaper.escape(unsafePair)).isEqualTo("X");
  }

  // Helpers

  private static UnicodeEscaper newEscaperWith(
      ImmutableMap<Character, String> replacements, int safeMin, int safeMax, char[] unsafeReplacement) {
    return new ArrayBasedUnicodeEscaper(replacements, safeMin, safeMax, null) {
      @Override
      protected char[] escapeUnsafe(int c) {
        return unsafeReplacement;
      }
    };
  }

  private static UnicodeEscaper wrappingEscaper(int safeMin, int safeMax) {
    return new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, safeMin, safeMax, null) {
      @Override
      protected char[] escapeUnsafe(int c) {
        return ("{" + (char) c + "}").toCharArray();
      }
    };
  }
}
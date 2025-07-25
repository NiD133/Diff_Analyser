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
 * Unit tests for the ArrayBasedUnicodeEscaper class.
 */
@GwtCompatible
@NullMarked
public class ArrayBasedUnicodeEscaperTest extends TestCase {

  // Constants for test cases
  private static final ImmutableMap<Character, String> NO_REPLACEMENTS = ImmutableMap.of();
  private static final ImmutableMap<Character, String> SIMPLE_REPLACEMENTS = ImmutableMap.of(
      '\n', "<newline>",
      '\t', "<tab>",
      '&', "<and>"
  );
  private static final char[] EMPTY_CHAR_ARRAY = new char[0];

  /**
   * Tests the basic replacement functionality of the escaper.
   */
  public void testBasicReplacements() throws IOException {
    UnicodeEscaper escaper = createEscaperWithSimpleReplacements();

    // Verify basic replacements
    assertThat(escaper.escape("\tFish & Chips\n")).isEqualTo("<tab>Fish <and> Chips<newline>");

    // Verify that characters not in the replacement map remain unchanged
    String safeChars = "\0\u0100\uD800\uDC00\uFFFF";
    assertThat(escaper.escape(safeChars)).isEqualTo(safeChars);

    // Verify behavior with malformed Unicode input
    String malformedUnicode = "\uDC00\uD800";
    assertThrows(IllegalArgumentException.class, () -> escaper.escape(malformedUnicode));
  }

  /**
   * Tests the behavior of the escaper with a defined safe range.
   */
  public void testSafeRangeEscaping() throws IOException {
    UnicodeEscaper escaper = createEscaperWithSafeRange('A', 'Z');

    // Verify that characters outside the safe range are wrapped
    assertThat(escaper.escape("[FOO@BAR]")).isEqualTo("{[}FOO{@}BAR{]}");
  }

  /**
   * Tests the deletion of unsafe characters.
   */
  public void testDeleteUnsafeCharacters() throws IOException {
    UnicodeEscaper escaper = createEscaperDeletingUnsafeChars();

    // Verify that unsafe characters are deleted
    assertThat(escaper.escape("\tEverything\0 outside the\uD800\uDC00 printable ASCII \uFFFFrange is \u007Fdeleted.\n"))
        .isEqualTo("Everything outside the printable ASCII range is deleted.");
  }

  /**
   * Tests the priority of replacements over safe range checks.
   */
  public void testReplacementPriority() throws IOException {
    UnicodeEscaper escaper = createEscaperWithReplacementPriority();

    // Verify that replacements take priority over safe range checks
    assertThat(escaper.escape("\tFish &\0 Chips\r\n"))
        .isEqualTo("<tab>Fish <and>? Chips?<newline>");
  }

  /**
   * Tests the handling of surrogate pairs in code points.
   */
  public void testSurrogatePairHandling() throws IOException {
    UnicodeEscaper escaper = createEscaperForSurrogatePairs();

    // Verify that surrogate pairs within the safe range are not escaped
    String safeInput = "\uD800\uDC00"; // 0x10000
    assertThat(escaper.escape(safeInput)).isEqualTo(safeInput);

    // Verify that surrogate pairs outside the safe range are escaped
    String unsafeInput = "\uDBFF\uDFFF"; // 0x10FFFF
    assertThat(escaper.escape(unsafeInput)).isEqualTo("X");
  }

  // Helper methods to create escapers for different test scenarios

  private UnicodeEscaper createEscaperWithSimpleReplacements() {
    return new ArrayBasedUnicodeEscaper(SIMPLE_REPLACEMENTS, Character.MIN_VALUE, Character.MAX_CODE_POINT, null) {
      @Override
      protected char[] escapeUnsafe(int c) {
        return EMPTY_CHAR_ARRAY;
      }
    };
  }

  private UnicodeEscaper createEscaperWithSafeRange(char safeMin, char safeMax) {
    return new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, safeMin, safeMax, null) {
      @Override
      protected char[] escapeUnsafe(int c) {
        return ("{" + (char) c + "}").toCharArray();
      }
    };
  }

  private UnicodeEscaper createEscaperDeletingUnsafeChars() {
    return new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, ' ', '~', null) {
      @Override
      protected char[] escapeUnsafe(int c) {
        return EMPTY_CHAR_ARRAY;
      }
    };
  }

  private UnicodeEscaper createEscaperWithReplacementPriority() {
    return new ArrayBasedUnicodeEscaper(SIMPLE_REPLACEMENTS, ' ', '~', null) {
      private final char[] unknown = new char[] {'?'};

      @Override
      protected char[] escapeUnsafe(int c) {
        return unknown;
      }
    };
  }

  private UnicodeEscaper createEscaperForSurrogatePairs() {
    return new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, 0, 0x20000, null) {
      private final char[] escaped = new char[] {'X'};

      @Override
      protected char[] escapeUnsafe(int c) {
        return escaped;
      }
    };
  }
}
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
 * Unit tests for {@link ArrayBasedUnicodeEscaper}.
 * 
 * Tests various scenarios such as replacements, safe range handling,
 * deletion of unsafe characters, replacement priority, and handling of
 * surrogate pairs.
 * 
 * Author: David Beaumont
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
  private static final char[] NO_CHARS = new char[0];

  /**
   * Tests that the escaper correctly applies character replacements
   * from the provided map.
   */
  public void testReplacements() throws IOException {
    // Create an escaper with simple replacements
    UnicodeEscaper escaper = new ArrayBasedUnicodeEscaper(
        SIMPLE_REPLACEMENTS, Character.MIN_VALUE, Character.MAX_CODE_POINT, null) {
      @Override
      protected char[] escapeUnsafe(int c) {
        return NO_CHARS;
      }
    };

    // Verify basic escaper functionality
    EscaperAsserts.assertBasic(escaper);

    // Test escaping with replacements
    String input = "\tFish & Chips\n";
    String expectedOutput = "<tab>Fish <and> Chips<newline>";
    assertThat(escaper.escape(input)).isEqualTo(expectedOutput);

    // Test that unescaped characters remain unchanged
    String safeChars = "\0\u0100\uD800\uDC00\uFFFF";
    assertThat(escaper.escape(safeChars)).isEqualTo(safeChars);

    // Test handling of malformed Unicode input
    String badUnicode = "\uDC00\uD800";
    assertThrows(IllegalArgumentException.class, () -> escaper.escape(badUnicode));
  }

  /**
   * Tests that characters outside the safe range are escaped.
   */
  public void testSafeRange() throws IOException {
    // Create an escaper that wraps unsafe characters in curly braces
    UnicodeEscaper wrappingEscaper = new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, 'A', 'Z', null) {
      @Override
      protected char[] escapeUnsafe(int c) {
        return ("{" + (char) c + "}").toCharArray();
      }
    };

    // Verify basic escaper functionality
    EscaperAsserts.assertBasic(wrappingEscaper);

    // Test escaping of characters outside the safe range
    String input = "[FOO@BAR]";
    String expectedOutput = "{[}FOO{@}BAR{]}";
    assertThat(wrappingEscaper.escape(input)).isEqualTo(expectedOutput);
  }

  /**
   * Tests that unsafe characters are deleted when no replacement is provided.
   */
  public void testDeleteUnsafeChars() throws IOException {
    // Create an escaper that deletes unsafe characters
    UnicodeEscaper deletingEscaper = new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, ' ', '~', null) {
      @Override
      protected char[] escapeUnsafe(int c) {
        return NO_CHARS;
      }
    };

    // Verify basic escaper functionality
    EscaperAsserts.assertBasic(deletingEscaper);

    // Test deletion of unsafe characters
    String input = "\tEverything\0 outside the\uD800\uDC00 printable ASCII \uFFFFrange is \u007Fdeleted.\n";
    String expectedOutput = "Everything outside the printable ASCII range is deleted.";
    assertThat(deletingEscaper.escape(input)).isEqualTo(expectedOutput);
  }

  /**
   * Tests that replacements take priority over safe range checks.
   */
  public void testReplacementPriority() throws IOException {
    // Create an escaper with replacements and a safe range
    UnicodeEscaper replacingEscaper = new ArrayBasedUnicodeEscaper(SIMPLE_REPLACEMENTS, ' ', '~', null) {
      private final char[] unknown = new char[] {'?'};

      @Override
      protected char[] escapeUnsafe(int c) {
        return unknown;
      }
    };

    // Verify basic escaper functionality
    EscaperAsserts.assertBasic(replacingEscaper);

    // Test that replacements are applied before safe range checks
    String input = "\tFish &\0 Chips\r\n";
    String expectedOutput = "<tab>Fish <and>? Chips?<newline>";
    assertThat(replacingEscaper.escape(input)).isEqualTo(expectedOutput);
  }

  /**
   * Tests handling of surrogate pairs and code points.
   */
  public void testCodePointsFromSurrogatePairs() throws IOException {
    // Create an escaper with a safe range for surrogate pairs
    UnicodeEscaper surrogateEscaper = new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, 0, 0x20000, null) {
      private final char[] escaped = new char[] {'X'};

      @Override
      protected char[] escapeUnsafe(int c) {
        return escaped;
      }
    };

    // Verify basic escaper functionality
    EscaperAsserts.assertBasic(surrogateEscaper);

    // Test that surrogate pairs within the safe range are not escaped
    String safeInput = "\uD800\uDC00"; // 0x10000
    assertThat(surrogateEscaper.escape(safeInput)).isEqualTo(safeInput);

    // Test that surrogate pairs outside the safe range are escaped
    String unsafeInput = "\uDBFF\uDFFF"; // 0x10FFFF
    assertThat(surrogateEscaper.escape(unsafeInput)).isEqualTo("X");
  }
}
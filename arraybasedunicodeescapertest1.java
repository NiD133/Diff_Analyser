package com.google.common.escape;

import static com.google.common.escape.ReflectionFreeAssertThrows.assertThrows;
import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableMap;
import com.google.common.escape.testing.EscaperAsserts;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link ArrayBasedUnicodeEscaper}.
 *
 * <p>This test focuses on an escaper configured with a specific replacement map and a wide "safe"
 * range, verifying how it handles mapped characters, safe characters, and malformed input.
 */
@RunWith(JUnit4.class)
public class ArrayBasedUnicodeEscaperTest {

  private static final ImmutableMap<Character, String> SIMPLE_REPLACEMENTS =
      ImmutableMap.of('\n', "<newline>", '\t', "<tab>", '&', "<and>");

  /**
   * Creates an escaper that replaces a few specific characters and considers all others safe.
   *
   * <p>This is not a realistic escaper configuration; its purpose is to test that characters
   * falling within the "safe" range are not passed to {@code escapeUnsafe}.
   */
  private static UnicodeEscaper createTestEscaper() {
    return new ArrayBasedUnicodeEscaper(
        SIMPLE_REPLACEMENTS, Character.MIN_VALUE, Character.MAX_CODE_POINT, null) {
      @Override
      protected char[] escapeUnsafe(int c) {
        // This should not be called in these tests, as all characters are either in the
        // replacement map or the safe range. Returning an empty array would effectively
        // delete the character, causing the "safe characters" test to fail if called.
        return new char[0];
      }
    };
  }

  @Test
  public void escape_withMappedCharacters_replacesThemCorrectly() {
    // Arrange
    UnicodeEscaper escaper = createTestEscaper();
    String input = "\tFish & Chips\n";
    String expected = "<tab>Fish <and> Chips<newline>";

    // Act
    String result = escaper.escape(input);

    // Assert
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void escape_withUnmappedSafeCharacters_leavesThemUnchanged() {
    // Arrange
    UnicodeEscaper escaper = createTestEscaper();
    // A string of characters not in the replacement map but within the configured safe range.
    // Includes null, a BMP char, a surrogate pair, and the max BMP char.
    String safeChars = "\0\u0100\uD800\uDC00\uFFFF";

    // Act
    String result = escaper.escape(safeChars);

    // Assert
    // Verifies that these characters are considered safe and not passed to escapeUnsafe.
    assertThat(result).isEqualTo(safeChars);
  }

  @Test
  public void escape_withMalformedUnicodeInput_throwsIllegalArgumentException() {
    // Arrange
    UnicodeEscaper escaper = createTestEscaper();
    // A low surrogate followed by a high surrogate is invalid.
    String badUnicode = "\uDC00\uD800";

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> escaper.escape(badUnicode));
  }

  @Test
  public void escaper_passesBasicSanityChecks() throws IOException {
    // Arrange
    UnicodeEscaper escaper = createTestEscaper();

    // Act & Assert
    // EscaperAsserts performs a series of generic assertions to ensure
    // the escaper behaves consistently (e.g., null handling).
    EscaperAsserts.assertBasic(escaper);
  }
}
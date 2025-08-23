package com.google.common.escape;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import com.google.common.escape.testing.EscaperAsserts;
import java.io.IOException;
import junit.framework.TestCase;

/**
 * Tests for {@link ArrayBasedUnicodeEscaper}, focusing on the priority of escaping rules.
 */
@GwtCompatible
public class ArrayBasedUnicodeEscaperTestTest4 extends TestCase {

  private static final ImmutableMap<Character, String> REPLACEMENTS =
      ImmutableMap.of(
          '\n', "<newline>",
          '\t', "<tab>",
          '&', "<and>");

  /**
   * This test verifies that replacements from the provided map take precedence over all other
   * escaping rules (safe range and unsafe character handling).
   */
  public void testEscape_replacementMapHasHighestPriority() throws IOException {
    // ARRANGE
    // Create an escaper with three distinct behaviors:
    // 1. A map of specific character replacements.
    // 2. A "safe range" of characters (' ' to '~') that should not be escaped.
    // 3. A fallback for any "unsafe" character (not in the map or safe range) to be replaced by '?'.
    UnicodeEscaper escaper = createEscaperWithCustomRules();

    // This input string contains characters that test each behavior:
    // - In replacement map (and outside safe range): '\t', '\n'
    // - In replacement map (and inside safe range): '&'
    // - Not in map and outside safe range (unsafe): '\0', '\r'
    // - Not in map and inside safe range (safe): "Fish", "Chips"
    String input = "\tFish &\0 Chips\r\n";

    // ACT
    String escapedString = escaper.escape(input);

    // ASSERT
    // The expected string is constructed to demonstrate the priority of the rules.
    String expectedString =
        "<tab>"      // From map (unsafe char)
      + "Fish "      // Safe, no change
      + "<and>"      // From map (despite being a 'safe' char)
      + "?"          // Unsafe fallback
      + " Chips"     // Safe, no change
      + "?"          // Unsafe fallback
      + "<newline>"; // From map (unsafe char)

    assertThat(escapedString).isEqualTo(expectedString);

    // Additionally, run a standard set of assertions to ensure the escaper is generally well-behaved.
    EscaperAsserts.assertBasic(escaper);
  }

  /**
   * Creates a custom escaper for testing rule priority.
   *
   * <p>This escaper has:
   * <ul>
   *   <li>A specific set of replacements for '\n', '\t', and '&'.
   *   <li>A safe range of printable ASCII characters (' ' to '~').
   *   <li>A fallback mechanism that escapes any other "unsafe" character to '?'.
   * </ul>
   */
  private static UnicodeEscaper createEscaperWithCustomRules() {
    return new ArrayBasedUnicodeEscaper(REPLACEMENTS, ' ', '~', null) {
      private final char[] unsafeCharReplacement = {'?'};

      @Override
      protected char[] escapeUnsafe(int c) {
        return unsafeCharReplacement;
      }
    };
  }
}
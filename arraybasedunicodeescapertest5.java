package com.google.common.escape;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link ArrayBasedUnicodeEscaper} focusing on its handling of Unicode surrogate pairs.
 */
@RunWith(JUnit4.class)
public class ArrayBasedUnicodeEscaperSurrogatePairTest {

  private static final String UNSAFE_REPLACEMENT = "X";

  // This escaper considers code points from 0 to 0x20000 as safe. Any code point outside this
  // range is considered unsafe and will be replaced by "X".
  private final UnicodeEscaper escaper =
      new ArrayBasedUnicodeEscaper(ImmutableMap.of(), 0, 0x20000, null) {
        @Override
        protected char[] escapeUnsafe(int c) {
          return UNSAFE_REPLACEMENT.toCharArray();
        }
      };

  @Test
  public void escape_withSafeSurrogateCodePoint_doesNotChangeInput() {
    // Arrange: U+10000 is a code point that requires a surrogate pair for representation in a
    // String, but it falls within the defined safe range (0 - 0x20000).
    int safeCodePoint = 0x10000;
    String inputWithSafeSurrogate = new String(Character.toChars(safeCodePoint));

    // Act
    String escaped = escaper.escape(inputWithSafeSurrogate);

    // Assert: The escaper should correctly identify the code point as safe and not escape it.
    assertThat(escaped).isEqualTo(inputWithSafeSurrogate);
  }

  @Test
  public void escape_withUnsafeSurrogateCodePoint_replacesEntirePair() {
    // Arrange: U+10FFFF is a code point outside the safe range (0 - 0x20000). It is represented
    // by the surrogate pair "\uDBFF\uDFFF".
    int unsafeCodePoint = 0x10FFFF;
    String inputWithUnsafeSurrogate = new String(Character.toChars(unsafeCodePoint));

    // Act
    String escaped = escaper.escape(inputWithUnsafeSurrogate);

    // Assert: This is a crucial test. The individual char values of the surrogate pair
    // (0xDBFF and 0xDFFF) are numerically less than the safe max (0x20000). However, the
    // escaper must correctly process them as a single, unsafe code point and escape the pair
    // as a whole.
    assertThat(escaped).isEqualTo(UNSAFE_REPLACEMENT);
  }
}
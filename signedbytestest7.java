package com.google.common.primitives;

import static com.google.common.primitives.SignedBytes.min;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import com.google.common.annotations.GwtCompatible;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link SignedBytes}.
 *
 * <p>This class is a refactored version of a snippet from a larger test suite. Helper methods
 * potentially used by other tests have been retained.
 */
@RunWith(JUnit4.class)
@GwtCompatible
public class SignedBytesTest {

  private static final byte LEAST = Byte.MIN_VALUE; // -128
  private static final byte GREATEST = Byte.MAX_VALUE; // 127

  private static final byte[] VALUES = {LEAST, -1, 0, 1, GREATEST};

  // Helper methods from the original test class, retained for context.
  private static void assertCastFails(long value) {
    try {
      SignedBytes.checkedCast(value);
      fail("Cast to byte should have failed: " + value);
    } catch (IllegalArgumentException ex) {
      assertWithMessage(value + " not found in exception text: " + ex.getMessage())
          .that(ex.getMessage()
          .contains(String.valueOf(value)))
          .isTrue();
    }
  }

  private static void testSortDescending(byte[] input, byte[] expectedOutput) {
    input = Arrays.copyOf(input, input.length);
    SignedBytes.sortDescending(input);
    assertThat(input).isEqualTo(expectedOutput);
  }

  private static void testSortDescending(byte[] input, int fromIndex, int toIndex, byte[] expectedOutput) {
    input = Arrays.copyOf(input, input.length);
    SignedBytes.sortDescending(input, fromIndex, toIndex);
    assertThat(input).isEqualTo(expectedOutput);
  }
  // End of retained helper methods.

  @Test
  public void min_withSingleElement_returnsThatElement() {
    assertThat(min(LEAST)).isEqualTo(LEAST);
    assertThat(min(GREATEST)).isEqualTo(GREATEST);
    assertThat(min((byte) 42)).isEqualTo((byte) 42);
  }

  @Test
  public void min_withMultipleElements_returnsSmallestValue() {
    // The VALUES array contains a good mix of boundary and typical values.
    // Its smallest element is LEAST (Byte.MIN_VALUE).
    assertThat(min(VALUES)).isEqualTo(LEAST);
  }
}
package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 *
 * <p>This refactored test focuses on the constructor that accepts a size, ensuring it behaves as
 * expected.
 */
public class AtomicDoubleArrayTest {

  /**
   * Asserts that two double values are bitwise equal.
   *
   * <p>This custom assertion is necessary because {@link AtomicDoubleArray} uses {@link
   * Double#doubleToRawLongBits(double)} for its atomic operations (like {@code compareAndSet}).
   * This differs from standard '==' comparison, especially for values like {@code -0.0}, {@code
   * +0.0}, and {@code NaN}.
   *
   * @param expected the expected double value
   * @param actual the actual double value
   */
  private static void assertBitEquals(double expected, double actual) {
    long expectedBits = Double.doubleToRawLongBits(expected);
    long actualBits = Double.doubleToRawLongBits(actual);
    if (expectedBits != actualBits) {
      String errorMessage =
          String.format(
              "Expected bit representation <%s> but was <%s>.",
              Long.toHexString(expectedBits), Long.toHexString(actualBits));
      assertEquals(errorMessage, expectedBits, actualBits);
    }
  }

  @Test
  public void constructor_withSize_initializesArrayWithZeros() {
    // Arrange
    final int arraySize = 10;

    // Act
    AtomicDoubleArray array = new AtomicDoubleArray(arraySize);

    // Assert
    assertEquals("Array length should match the provided size.", arraySize, array.length());
    for (int i = 0; i < arraySize; i++) {
      // The constructor must initialize all elements to 0.0. We use a bitwise
      // comparison because this is the equality contract used by AtomicDoubleArray.
      assertBitEquals(0.0, array.get(i));
    }
  }
}
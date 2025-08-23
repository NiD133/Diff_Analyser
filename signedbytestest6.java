package com.google.common.primitives;

import static com.google.common.primitives.ReflectionFreeAssertThrows.assertThrows;
import static com.google.common.primitives.SignedBytes.min;

import junit.framework.TestCase;

/**
 * Tests for {@link SignedBytes}.
 */
public class SignedBytesTest extends TestCase {

  /**
   * Tests that {@code SignedBytes.min} throws an {@code IllegalArgumentException} when called with
   * an empty array, as the minimum value is undefined for an empty set.
   */
  public void testMin_emptyArray_throwsIllegalArgumentException() {
    // The min(byte...) method is a varargs method.
    // Calling it with no arguments is equivalent to passing an empty array.
    assertThrows(IllegalArgumentException.class, () -> min());
  }
}
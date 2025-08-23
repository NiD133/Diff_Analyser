package com.google.common.util.concurrent;

import static org.junit.Assert.assertThrows;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import org.jspecify.annotations.NullUnmarked;

/**
 * Unit tests for {@link AtomicDoubleArray}.
 */
@GwtIncompatible
@J2ktIncompatible
@NullUnmarked
public class AtomicDoubleArrayTest extends JSR166TestCase {

  /**
   * Tests that the constructor correctly creates an empty array when the specified length is zero.
   */
  public void testConstructor_withZeroLength_createsEmptyArray() {
    // Act: Create an AtomicDoubleArray with a length of 0.
    AtomicDoubleArray array = new AtomicDoubleArray(0);

    // Assert: The array's length should be 0.
    assertEquals(
        "An array created with length 0 should have a reported length of 0.", 0, array.length());

    // Assert: Attempting to access any element should throw an exception.
    assertThrows(
        "Accessing an element in a zero-length array should throw IndexOutOfBoundsException.",
        IndexOutOfBoundsException.class,
        () -> array.get(0));
  }
}
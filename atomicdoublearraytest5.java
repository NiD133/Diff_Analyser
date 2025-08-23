package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;

/**
 * Tests for the constructor of {@link AtomicDoubleArray}.
 */
@GwtIncompatible
@J2ktIncompatible
public class AtomicDoubleArrayTest extends JSR166TestCase {

  /**
   * Verifies that creating an {@code AtomicDoubleArray} from an empty array results in a
   * zero-length array that throws an {@link IndexOutOfBoundsException} upon access.
   */
  public void testConstructor_withEmptyArray_createsEmptyArray() {
    // Arrange
    double[] emptySourceArray = new double[0];

    // Act
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(emptySourceArray);

    // Assert
    assertEquals(
        "An array created from an empty source should have a length of 0.",
        0,
        atomicArray.length());

    assertThrows(
        "Accessing an element in a zero-length array should throw an exception.",
        IndexOutOfBoundsException.class,
        () -> atomicArray.get(0));
  }
}
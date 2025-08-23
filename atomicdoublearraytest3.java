package com.google.common.util.concurrent;

import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
@RunWith(JUnit4.class)
public class AtomicDoubleArrayTest {

  /**
   * Verifies that the constructor throws a NullPointerException when given a null array,
   * as per its contract.
   */
  @Test
  public void constructor_withNullArray_throwsNullPointerException() {
    // The constructor of AtomicDoubleArray is specified to throw a NullPointerException
    // if the input array is null. This test verifies that behavior.

    // Act & Assert
    // Call the constructor with a null argument and assert that a NullPointerException is thrown.
    assertThrows(NullPointerException.class, () -> new AtomicDoubleArray(null));
  }
}
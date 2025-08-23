package com.google.common.util.concurrent;

import static org.junit.Assert.assertThrows;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;

/**
 * Tests for bounds checking in {@link AtomicDoubleArray}.
 */
@GwtIncompatible
@J2ktIncompatible
public class AtomicDoubleArrayBoundsTest extends JSR166TestCase {

  private static final int ARRAY_SIZE = 10;

  /**
   * Verifies that all accessor and mutator methods of {@code AtomicDoubleArray} throw an
   * {@code IndexOutOfBoundsException} when given an index that is out of bounds. The test checks
   * two boundary conditions: -1 (below the lower bound) and the array's size (at the upper bound).
   */
  public void testOperationsOnOutOfBoundsIndicesThrowException() {
    AtomicDoubleArray array = new AtomicDoubleArray(ARRAY_SIZE);
    int[] outOfBoundsIndices = {-1, ARRAY_SIZE};

    for (int invalidIndex : outOfBoundsIndices) {
      // Test read operation
      assertThrows(IndexOutOfBoundsException.class, () -> array.get(invalidIndex));

      // Test write operations
      assertThrows(IndexOutOfBoundsException.class, () -> array.set(invalidIndex, 1.0));
      assertThrows(IndexOutOfBoundsException.class, () -> array.lazySet(invalidIndex, 1.0));

      // Test atomic read-modify-write operations
      assertThrows(
          IndexOutOfBoundsException.class, () -> array.compareAndSet(invalidIndex, 1.0, 2.0));
      assertThrows(
          IndexOutOfBoundsException.class,
          () -> array.weakCompareAndSet(invalidIndex, 1.0, 2.0));
      assertThrows(IndexOutOfBoundsException.class, () -> array.getAndAdd(invalidIndex, 1.0));
      assertThrows(IndexOutOfBoundsException.class, () -> array.addAndGet(invalidIndex, 1.0));
    }
  }
}
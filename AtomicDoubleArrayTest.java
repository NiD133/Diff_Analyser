/*
 * Written by Doug Lea and Martin Buchholz with assistance from
 * members of JCP JSR-166 Expert Group and released to the public
 * domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

/*
 * Source:
 * http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/test/tck-jsr166e/AtomicDoubleArrayTest.java?revision=1.13
 * (Modified to adapt to guava coding conventions and for improved readability)
 */

package com.google.common.util.concurrent;

import static java.lang.Math.max;
import static org.junit.Assert.assertThrows;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.testing.NullPointerTester;
import java.util.Arrays;
import org.jspecify.annotations.NullUnmarked;

/**
 * Unit test for {@link AtomicDoubleArray}.
 *
 * <p>The tests in this suite are derived from the JSR-166 TCK and are designed to be
 * comprehensive, covering edge cases, concurrency, and the full public API.
 */
@NullUnmarked
public class AtomicDoubleArrayTest extends JSR166TestCase {

  /** A diverse set of double values used for comprehensive testing. */
  private static final double[] INTERESTING_DOUBLES = {
    Double.NEGATIVE_INFINITY,
    -Double.MAX_VALUE,
    (double) Long.MIN_VALUE,
    (double) Integer.MIN_VALUE,
    -Math.PI,
    -1.0,
    -Double.MIN_VALUE,
    -0.0,
    +0.0,
    Double.MIN_VALUE,
    1.0,
    Math.PI,
    (double) Integer.MAX_VALUE,
    (double) Long.MAX_VALUE,
    Double.MAX_VALUE,
    Double.POSITIVE_INFINITY,
    Double.NaN,
    Float.MAX_VALUE,
  };

  /**
   * Asserts that two double values are bit-wise equal, which is the equality contract for {@link
   * AtomicDoubleArray}.
   */
  static void assertBitEquals(double expected, double actual) {
    assertEquals(Double.doubleToRawLongBits(expected), Double.doubleToRawLongBits(actual));
  }

  // ==================== Null Safety Tests ====================

  @J2ktIncompatible
  @GwtIncompatible // NullPointerTester
  public void testNulls() {
    new NullPointerTester().testAllPublicStaticMethods(AtomicDoubleArray.class);
    new NullPointerTester().testAllPublicConstructors(AtomicDoubleArray.class);
    new NullPointerTester().testAllPublicInstanceMethods(new AtomicDoubleArray(1));
  }

  // ==================== Constructor Tests ====================

  /** Tests that the constructor with a size argument creates an array of that size with all elements as 0.0. */
  public void constructor_withSize_initializesToZero() {
    // Arrange & Act
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);

    // Assert
    for (int i = 0; i < SIZE; i++) {
      assertBitEquals(0.0, atomicArray.get(i));
    }
  }

  /** Tests that the constructor throws a NullPointerException when given a null array. */
  public void constructor_withNullArray_throwsNullPointerException() {
    double[] initialArray = null;
    assertThrows(NullPointerException.class, () -> new AtomicDoubleArray(initialArray));
  }

  /** Tests that the constructor correctly copies elements from a given source array. */
  public void constructor_withArray_copiesElements() {
    // Arrange & Act
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(INTERESTING_DOUBLES);

    // Assert
    assertEquals(INTERESTING_DOUBLES.length, atomicArray.length());
    for (int i = 0; i < INTERESTING_DOUBLES.length; i++) {
      assertBitEquals(INTERESTING_DOUBLES[i], atomicArray.get(i));
    }
  }

  /** Tests that constructing with an empty array results in a zero-length array. */
  public void constructor_withEmptyArray_createsEmptyAtomicArray() {
    // Arrange & Act
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(new double[0]);

    // Assert
    assertEquals(0, atomicArray.length());
    assertThrows(IndexOutOfBoundsException.class, () -> atomicArray.get(0));
  }

  /** Tests that constructing with a length of zero results in a zero-length array. */
  public void constructor_withZeroLength_createsEmptyAtomicArray() {
    // Arrange & Act
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(0);

    // Assert
    assertEquals(0, atomicArray.length());
    assertThrows(IndexOutOfBoundsException.class, () -> atomicArray.get(0));
  }

  // ==================== Basic Method Tests ====================

  /** Tests that `toString()` returns a string representation matching `Arrays.toString()`. */
  public void toString_returnsCorrectStringRepresentation() {
    AtomicDoubleArray arrayWithValues = new AtomicDoubleArray(INTERESTING_DOUBLES);
    assertEquals(Arrays.toString(INTERESTING_DOUBLES), arrayWithValues.toString());

    AtomicDoubleArray emptyArray = new AtomicDoubleArray(0);
    assertEquals("[]", emptyArray.toString());
  }

  /** Tests that all mutating and accessing methods throw exceptions for out-of-bounds indices. */
  public void allMethods_withInvalidIndex_throwIndexOutOfBoundsException() {
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
    int[] invalidIndices = {-1, SIZE};

    for (int index : invalidIndices) {
      assertThrows(IndexOutOfBoundsException.class, () -> atomicArray.get(index));
      assertThrows(IndexOutOfBoundsException.class, () -> atomicArray.set(index, 1.0));
      assertThrows(IndexOutOfBoundsException.class, () -> atomicArray.lazySet(index, 1.0));
      assertThrows(IndexOutOfBoundsException.class, () -> atomicArray.compareAndSet(index, 1.0, 2.0));
      assertThrows(IndexOutOfBoundsException.class, () -> atomicArray.weakCompareAndSet(index, 1.0, 2.0));
      assertThrows(IndexOutOfBoundsException.class, () -> atomicArray.getAndAdd(index, 1.0));
      assertThrows(IndexOutOfBoundsException.class, () -> atomicArray.addAndGet(index, 1.0));
    }
  }

  // ==================== Atomic Operation Tests ====================

  /** Tests that `get()` returns the value most recently set by `set()`. */
  public void get_returnsLastSetValue() {
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(INTERESTING_DOUBLES.length);

    for (int i = 0; i < INTERESTING_DOUBLES.length; i++) {
      double testValue = INTERESTING_DOUBLES[i];
      atomicArray.set(i, testValue);
      assertBitEquals(testValue, atomicArray.get(i));
    }
  }

  /** Tests that `get()` returns the value most recently set by `lazySet()` on the same thread. */
  public void get_returnsLastLazySetValue() {
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(INTERESTING_DOUBLES.length);

    for (int i = 0; i < INTERESTING_DOUBLES.length; i++) {
      double testValue = INTERESTING_DOUBLES[i];
      atomicArray.lazySet(i, testValue);
      assertBitEquals(testValue, atomicArray.get(i));
    }
  }

  /** Tests that `compareAndSet` only succeeds when the expected value matches the current value. */
  public void compareAndSet_updatesValue_whenExpectationMatches() {
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
    int[] indicesToTest = {0, SIZE - 1};

    for (int index : indicesToTest) {
      double currentValue = 0.0;
      double wrongExpectedValue = Math.E + Math.PI;

      for (double newValue : INTERESTING_DOUBLES) {
        assertBitEquals(currentValue, atomicArray.get(index));

        // Act & Assert: Fail when expectation is wrong
        assertFalse(atomicArray.compareAndSet(index, wrongExpectedValue, newValue));
        assertBitEquals(currentValue, atomicArray.get(index));

        // Act & Assert: Succeed when expectation is correct
        assertTrue(atomicArray.compareAndSet(index, currentValue, newValue));
        assertBitEquals(newValue, atomicArray.get(index));

        currentValue = newValue;
      }
    }
  }

  /**
   * Tests that `weakCompareAndSet` eventually succeeds in a loop when the expected value is correct.
   */
  public void weakCompareAndSet_eventuallyUpdatesValue_whenExpectationMatches() {
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
    int[] indicesToTest = {0, SIZE - 1};

    for (int index : indicesToTest) {
      double currentValue = 0.0;
      double wrongExpectedValue = Math.E + Math.PI;

      for (double newValue : INTERESTING_DOUBLES) {
        assertBitEquals(currentValue, atomicArray.get(index));

        // Act & Assert: Fail when expectation is wrong
        assertFalse(atomicArray.weakCompareAndSet(index, wrongExpectedValue, newValue));
        assertBitEquals(currentValue, atomicArray.get(index));

        // Act & Assert: Loop until success, as weakCompareAndSet can fail spuriously.
        while (!atomicArray.weakCompareAndSet(index, currentValue, newValue)) {
          // This loop demonstrates the correct usage of weakCompareAndSet.
        }
        assertBitEquals(newValue, atomicArray.get(index));
        currentValue = newValue;
      }
    }
  }

  /** Tests that `compareAndSet` correctly distinguishes between +0.0 and -0.0. */
  public void compareAndSet_distinguishesPositiveAndNegativeZero() {
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
    int[] indicesToTest = {0, SIZE - 1};

    for (int index : indicesToTest) {
      // Initially, the value is +0.0
      assertBitEquals(+0.0, atomicArray.get(index));

      // Try to replace -0.0 with 7.0; should fail because current value is +0.0
      assertFalse(atomicArray.compareAndSet(index, -0.0, 7.0));
      assertFalse(atomicArray.weakCompareAndSet(index, -0.0, 7.0));
      assertBitEquals(+0.0, atomicArray.get(index));

      // Replace +0.0 with -0.0; should succeed
      assertTrue(atomicArray.compareAndSet(index, +0.0, -0.0));
      assertBitEquals(-0.0, atomicArray.get(index));

      // Try to replace +0.0 with 7.0; should fail because current value is now -0.0
      assertFalse(atomicArray.compareAndSet(index, +0.0, 7.0));
      assertFalse(atomicArray.weakCompareAndSet(index, +0.0, 7.0));
      assertBitEquals(-0.0, atomicArray.get(index));
    }
  }

  /** Tests that `getAndSet` returns the previous value and updates the element to the new value. */
  public void getAndSet_setsNewValue_andReturnsOldValue() {
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
    int[] indicesToTest = {0, SIZE - 1};

    for (int index : indicesToTest) {
      double previousValue = 0.0;
      for (double newValue : INTERESTING_DOUBLES) {
        double returnedValue = atomicArray.getAndSet(index, newValue);
        assertBitEquals(previousValue, returnedValue);
        previousValue = newValue;
      }
    }
  }

  /** Tests that `getAndAdd` correctly adds a delta and returns the old value. */
  public void getAndAdd_addsValue_andReturnsOldValue() {
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
    int[] indicesToTest = {0, SIZE - 1};

    for (int index : indicesToTest) {
      for (double initialValue : INTERESTING_DOUBLES) {
        for (double delta : INTERESTING_DOUBLES) {
          // Arrange
          atomicArray.set(index, initialValue);

          // Act
          double returnedValue = atomicArray.getAndAdd(index, delta);

          // Assert
          assertBitEquals(initialValue, returnedValue);
          assertBitEquals(initialValue + delta, atomicArray.get(index));
        }
      }
    }
  }

  /** Tests that `addAndGet` correctly adds a delta and returns the new value. */
  public void addAndGet_addsValue_andReturnsNewValue() {
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
    int[] indicesToTest = {0, SIZE - 1};

    for (int index : indicesToTest) {
      for (double initialValue : INTERESTING_DOUBLES) {
        for (double delta : INTERESTING_DOUBLES) {
          // Arrange
          atomicArray.set(index, initialValue);

          // Act
          double result = atomicArray.addAndGet(index, delta);

          // Assert
          double expectedValue = initialValue + delta;
          assertBitEquals(expectedValue, result);
          assertBitEquals(expectedValue, atomicArray.get(index));
        }
      }
    }
  }

  /** Tests `getAndAccumulate` with a sum operation. */
  public void getAndAccumulate_withSum_addsValue_andReturnsOldValue() {
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
    int[] indicesToTest = {0, SIZE - 1};

    for (int index : indicesToTest) {
      for (double initialValue : INTERESTING_DOUBLES) {
        for (double valueToAdd : INTERESTING_DOUBLES) {
          // Arrange
          atomicArray.set(index, initialValue);

          // Act
          double returnedValue = atomicArray.getAndAccumulate(index, valueToAdd, Double::sum);

          // Assert
          assertBitEquals(initialValue, returnedValue);
          assertBitEquals(initialValue + valueToAdd, atomicArray.get(index));
        }
      }
    }
  }

  /** Tests `getAndAccumulate` with a max operation. */
  public void getAndAccumulate_withMax_updatesToMax_andReturnsOldValue() {
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
    int[] indicesToTest = {0, SIZE - 1};

    for (int index : indicesToTest) {
      for (double initialValue : INTERESTING_DOUBLES) {
        for (double valueToCompare : INTERESTING_DOUBLES) {
          // Arrange
          atomicArray.set(index, initialValue);

          // Act
          double returnedValue = atomicArray.getAndAccumulate(index, valueToCompare, Double::max);

          // Assert
          assertBitEquals(initialValue, returnedValue);
          assertBitEquals(max(initialValue, valueToCompare), atomicArray.get(index));
        }
      }
    }
  }

  /** Tests `accumulateAndGet` with a sum operation. */
  public void accumulateAndGet_withSum_addsValue_andReturnsNewValue() {
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
    int[] indicesToTest = {0, SIZE - 1};

    for (int index : indicesToTest) {
      for (double initialValue : INTERESTING_DOUBLES) {
        for (double valueToAdd : INTERESTING_DOUBLES) {
          // Arrange
          atomicArray.set(index, initialValue);

          // Act
          double result = atomicArray.accumulateAndGet(index, valueToAdd, Double::sum);

          // Assert
          double expectedValue = initialValue + valueToAdd;
          assertBitEquals(expectedValue, result);
          assertBitEquals(expectedValue, atomicArray.get(index));
        }
      }
    }
  }

  /** Tests `accumulateAndGet` with a max operation. */
  public void accumulateAndGet_withMax_updatesToMax_andReturnsNewValue() {
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
    int[] indicesToTest = {0, SIZE - 1};

    for (int index : indicesToTest) {
      for (double initialValue : INTERESTING_DOUBLES) {
        for (double valueToCompare : INTERESTING_DOUBLES) {
          // Arrange
          atomicArray.set(index, initialValue);

          // Act
          double result = atomicArray.accumulateAndGet(index, valueToCompare, Double::max);

          // Assert
          double expectedMax = max(initialValue, valueToCompare);
          assertBitEquals(expectedMax, result);
          assertBitEquals(expectedMax, atomicArray.get(index));
        }
      }
    }
  }

  /** Tests `getAndUpdate` with a sum operation. */
  public void getAndUpdate_withSum_addsValue_andReturnsOldValue() {
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
    int[] indicesToTest = {0, SIZE - 1};

    for (int index : indicesToTest) {
      for (double initialValue : INTERESTING_DOUBLES) {
        for (double valueToAdd : INTERESTING_DOUBLES) {
          // Arrange
          atomicArray.set(index, initialValue);

          // Act
          double returnedValue = atomicArray.getAndUpdate(index, currentValue -> currentValue + valueToAdd);

          // Assert
          assertBitEquals(initialValue, returnedValue);
          assertBitEquals(initialValue + valueToAdd, atomicArray.get(index));
        }
      }
    }
  }

  /** Tests `updateAndGet` with a sum operation. */
  public void updateAndGet_withSum_addsValue_andReturnsNewValue() {
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
    int[] indicesToTest = {0, SIZE - 1};

    for (int index : indicesToTest) {
      for (double initialValue : INTERESTING_DOUBLES) {
        for (double valueToAdd : INTERESTING_DOUBLES) {
          // Arrange
          atomicArray.set(index, initialValue);

          // Act
          double result = atomicArray.updateAndGet(index, currentValue -> currentValue + valueToAdd);

          // Assert
          double expectedValue = initialValue + valueToAdd;
          assertBitEquals(expectedValue, result);
          assertBitEquals(expectedValue, atomicArray.get(index));
        }
      }
    }
  }

  // ==================== Concurrency Tests ====================

  /**
   * Tests a scenario where one thread is waiting for a value to change via `compareAndSet`, and
   * another thread makes that change.
   */
  public void compareAndSet_isSuccessfulInMultithreadedScenario() throws InterruptedException {
    // Arrange
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(1);
    atomicArray.set(0, 1.0);

    // This thread will spin until the value at index 0 is 2.0, then update it to 3.0.
    Thread backgroundThread =
        newStartedThread(
            new CheckedRunnable() {
              @Override
              @SuppressWarnings("ThreadPriorityCheck") // doing our best to test for races
              public void realRun() {
                while (!atomicArray.compareAndSet(0, 2.0, 3.0)) {
                  Thread.yield();
                }
              }
            });

    // Act: Main thread updates the value from 1.0 to 2.0, which should unblock the background thread.
    assertTrue(atomicArray.compareAndSet(0, 1.0, 2.0));

    // Assert: Wait for the background thread to complete and verify the final state.
    awaitTermination(backgroundThread);
    assertBitEquals(3.0, atomicArray.get(0));
  }

  static final long COUNTDOWN = 100_000;

  /**
   * A runnable that decrements values in an AtomicDoubleArray until they all reach zero, counting
   * the number of successful decrements it performs.
   */
  class Counter extends CheckedRunnable {
    final AtomicDoubleArray atomicArray;
    volatile long successfulDecrements;

    Counter(AtomicDoubleArray a) {
      this.atomicArray = a;
    }

    @Override
    public void realRun() {
      while (true) {
        boolean allCountersAreZero = true;
        for (int i = 0; i < atomicArray.length(); i++) {
          double currentValue = atomicArray.get(i);
          assertTrue(currentValue >= 0);
          if (currentValue > 0) {
            allCountersAreZero = false;
            if (atomicArray.compareAndSet(i, currentValue, currentValue - 1.0)) {
              successfulDecrements++;
            }
          }
        }
        if (allCountersAreZero) {
          break;
        }
      }
    }
  }

  /**
   * Tests that multiple threads can concurrently decrement counters in the array without losing
   * updates.
   */
  public void testCountingInMultipleThreads() throws InterruptedException {
    // Arrange
    AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
    for (int i = 0; i < SIZE; i++) {
      atomicArray.set(i, (double) COUNTDOWN);
    }

    Counter counter1 = new Counter(atomicArray);
    Counter counter2 = new Counter(atomicArray);
    Thread thread1 = newStartedThread(counter1);
    Thread thread2 = newStartedThread(counter2);

    // Act: Wait for both threads to finish their work.
    awaitTermination(thread1);
    awaitTermination(thread2);

    // Assert: The total number of decrements should equal the total initial value.
    long totalDecrements = counter1.successfulDecrements + counter2.successfulDecrements;
    assertEquals(SIZE * COUNTDOWN, totalDecrements);
  }

  // ==================== Serialization Tests ====================

  /** Tests that a deserialized array is a distinct object but holds the same values. */
  public void serialization_preservesArrayValues() throws Exception {
    // Arrange
    AtomicDoubleArray originalArray = new AtomicDoubleArray(INTERESTING_DOUBLES);

    // Act
    AtomicDoubleArray deserializedArray = serialClone(originalArray);

    // Assert
    assertTrue(originalArray != deserializedArray);
    assertEquals(originalArray.length(), deserializedArray.length());
    // AtomicDoubleArray does not override equals(), so it uses reference equality.
    assertFalse(originalArray.equals(deserializedArray));

    for (int i = 0; i < INTERESTING_DOUBLES.length; i++) {
      assertBitEquals(originalArray.get(i), deserializedArray.get(i));
    }
  }
}
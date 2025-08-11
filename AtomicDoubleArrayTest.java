/*
 * Written by Doug Lea and Martin Buchholz with assistance from
 * members of JCP JSR-166 Expert Group and released to the public
 * domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

/*
 * Source:
 * http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/test/tck-jsr166e/AtomicDoubleArrayTest.java?revision=1.13
 * (Modified to adapt to guava coding conventions)
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
 * Unit tests for {@link AtomicDoubleArray}.
 *
 * Notes for readers:
 * - There are many loops over VALUES to cover interesting doubles, including NaN and signed zeros.
 * - "Bit equality" is used throughout to match AtomicDoubleArray semantics (Double.doubleToRawLongBits).
 * - To reduce noise, most tests exercise only edge indexes: first (0) and last (SIZE - 1).
 */
@NullUnmarked
public class AtomicDoubleArrayTest extends JSR166TestCase {

  private static final double[] VALUES = {
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

  // Edge indexes that are representative for bounds-related behavior.
  private static final int[] EDGE_INDEXES = {0, SIZE - 1};

  /** The notion of equality used by AtomicDoubleArray. */
  static boolean bitEquals(double x, double y) {
    return Double.doubleToRawLongBits(x) == Double.doubleToRawLongBits(y);
  }

  static void assertBitEquals(double expected, double actual) {
    assertEquals(Double.doubleToRawLongBits(expected), Double.doubleToRawLongBits(actual));
  }

  private static void assertAllZeros(AtomicDoubleArray array) {
    for (int i = 0; i < array.length(); i++) {
      assertBitEquals(0.0, array.get(i));
    }
  }

  private static void assertIndexOutOfBoundsForAllOps(AtomicDoubleArray array, int index) {
    assertThrows(IndexOutOfBoundsException.class, () -> array.get(index));
    assertThrows(IndexOutOfBoundsException.class, () -> array.set(index, 1.0));
    assertThrows(IndexOutOfBoundsException.class, () -> array.lazySet(index, 1.0));
    assertThrows(IndexOutOfBoundsException.class, () -> array.compareAndSet(index, 1.0, 2.0));
    assertThrows(IndexOutOfBoundsException.class, () -> array.weakCompareAndSet(index, 1.0, 2.0));
    assertThrows(IndexOutOfBoundsException.class, () -> array.getAndAdd(index, 1.0));
    assertThrows(IndexOutOfBoundsException.class, () -> array.addAndGet(index, 1.0));
  }

  @J2ktIncompatible
  @GwtIncompatible // NullPointerTester
  public void testNulls() {
    new NullPointerTester().testAllPublicStaticMethods(AtomicDoubleArray.class);
    new NullPointerTester().testAllPublicConstructors(AtomicDoubleArray.class);
    new NullPointerTester().testAllPublicInstanceMethods(new AtomicDoubleArray(1));
  }

  /** Constructor creates array of given size with all elements zero. */
  public void testConstructor() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    assertAllZeros(array);
  }

  /** Constructor with null array throws NPE. */
  public void testConstructor2NPE() {
    double[] a = null;
    assertThrows(NullPointerException.class, () -> new AtomicDoubleArray(a));
  }

  /** Constructor with array is of same size and has all elements copied. */
  public void testConstructor2() {
    AtomicDoubleArray array = new AtomicDoubleArray(VALUES);
    assertEquals(VALUES.length, array.length());
    for (int i = 0; i < VALUES.length; i++) {
      assertBitEquals(VALUES[i], array.get(i));
    }
  }

  /** Constructor with empty array has size 0 and contains no elements. */
  public void testConstructorEmptyArray() {
    AtomicDoubleArray array = new AtomicDoubleArray(new double[0]);
    assertEquals(0, array.length());
    assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
  }

  /** Constructor with length zero has size 0 and contains no elements. */
  public void testConstructorZeroLength() {
    AtomicDoubleArray array = new AtomicDoubleArray(0);
    assertEquals(0, array.length());
    assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
  }

  /** get/set and other operations on out-of-bounds indices throw IndexOutOfBoundsException. */
  public void testIndexing() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int invalidIndex : new int[] {-1, SIZE}) {
      assertIndexOutOfBoundsForAllOps(array, invalidIndex);
    }
  }

  /** get returns the last value set at index. */
  public void testGetSet() {
    AtomicDoubleArray array = new AtomicDoubleArray(VALUES.length);
    for (int index = 0; index < VALUES.length; index++) {
      assertBitEquals(0.0, array.get(index));

      array.set(index, VALUES[index]);
      assertBitEquals(VALUES[index], array.get(index));

      array.set(index, -3.0);
      assertBitEquals(-3.0, array.get(index));
    }
  }

  /** get returns the last value lazySet at index by same thread. */
  public void testGetLazySet() {
    AtomicDoubleArray array = new AtomicDoubleArray(VALUES.length);
    for (int index = 0; index < VALUES.length; index++) {
      assertBitEquals(0.0, array.get(index));

      array.lazySet(index, VALUES[index]);
      assertBitEquals(VALUES[index], array.get(index));

      array.lazySet(index, -3.0);
      assertBitEquals(-3.0, array.get(index));
    }
  }

  /** compareAndSet succeeds in changing value if equal to expected, else fails. */
  public void testCompareAndSet() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int index : EDGE_INDEXES) {
      double previous = 0.0;
      double mismatchedValue = Math.E + Math.PI;

      for (double next : VALUES) {
        assertBitEquals(previous, array.get(index));

        assertFalse(array.compareAndSet(index, mismatchedValue, next));
        assertBitEquals(previous, array.get(index));

        assertTrue(array.compareAndSet(index, previous, next));
        assertBitEquals(next, array.get(index));

        previous = next;
      }
    }
  }

  /** compareAndSet in one thread enables another waiting for value to succeed. */
  public void testCompareAndSetInMultipleThreads() throws InterruptedException {
    AtomicDoubleArray array = new AtomicDoubleArray(1);
    array.set(0, 1.0);

    Thread setterThread =
        newStartedThread(
            new CheckedRunnable() {
              @Override
              @SuppressWarnings("ThreadPriorityCheck") // doing our best to test for races
              public void realRun() {
                // Wait until main thread updates 1.0 -> 2.0, then perform 2.0 -> 3.0
                while (!array.compareAndSet(0, 2.0, 3.0)) {
                  Thread.yield();
                }
              }
            });

    assertTrue(array.compareAndSet(0, 1.0, 2.0));
    awaitTermination(setterThread);
    assertBitEquals(3.0, array.get(0));
  }

  /** Repeated weakCompareAndSet succeeds when current equals expected. */
  public void testWeakCompareAndSet() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int index : EDGE_INDEXES) {
      double previous = 0.0;
      double mismatchedValue = Math.E + Math.PI;

      for (double next : VALUES) {
        assertBitEquals(previous, array.get(index));

        assertFalse(array.weakCompareAndSet(index, mismatchedValue, next));
        assertBitEquals(previous, array.get(index));

        while (!array.weakCompareAndSet(index, previous, next)) {
          // retry
        }
        assertBitEquals(next, array.get(index));

        previous = next;
      }
    }
  }

  /** getAndSet returns previous value and sets to given value at given index. */
  public void testGetAndSet() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int index : EDGE_INDEXES) {
      double previous = 0.0;
      for (double next : VALUES) {
        assertBitEquals(previous, array.getAndSet(index, next));
        previous = next;
      }
    }
  }

  /** getAndAdd returns previous value and adds given value. */
  public void testGetAndAdd() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int index : EDGE_INDEXES) {
      for (double start : VALUES) {
        for (double delta : VALUES) {
          array.set(index, start);
          double returned = array.getAndAdd(index, delta);
          assertBitEquals(start, returned);
          assertBitEquals(start + delta, array.get(index));
        }
      }
    }
  }

  /** addAndGet adds given value to current, and returns current value. */
  public void testAddAndGet() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int index : EDGE_INDEXES) {
      for (double start : VALUES) {
        for (double delta : VALUES) {
          array.set(index, start);
          double updated = array.addAndGet(index, delta);
          assertBitEquals(start + delta, updated);
          assertBitEquals(start + delta, array.get(index));
        }
      }
    }
  }

  /** getAndAccumulate with sum adds given value to current, and returns previous value. */
  public void testGetAndAccumulateWithSum() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int index : EDGE_INDEXES) {
      for (double start : VALUES) {
        for (double x : VALUES) {
          array.set(index, start);
          double previous = array.getAndAccumulate(index, x, Double::sum);
          assertBitEquals(start, previous);
          assertBitEquals(start + x, array.get(index));
        }
      }
    }
  }

  /** getAndAccumulate with max stores max(current, x) and returns previous value. */
  public void testGetAndAccumulateWithMax() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int index : EDGE_INDEXES) {
      for (double start : VALUES) {
        for (double x : VALUES) {
          array.set(index, start);
          double previous = array.getAndAccumulate(index, x, Double::max);
          double expectedMax = max(start, x);
          assertBitEquals(start, previous);
          assertBitEquals(expectedMax, array.get(index));
        }
      }
    }
  }

  /** accumulateAndGet with sum adds given value to current, and returns current value. */
  public void testAccumulateAndGetWithSum() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int index : EDGE_INDEXES) {
      for (double start : VALUES) {
        for (double x : VALUES) {
          array.set(index, start);
          double updated = array.accumulateAndGet(index, x, Double::sum);
          assertBitEquals(start + x, updated);
          assertBitEquals(start + x, array.get(index));
        }
      }
    }
  }

  /** accumulateAndGet with max stores max(current, x) and returns current value. */
  public void testAccumulateAndGetWithMax() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int index : EDGE_INDEXES) {
      for (double start : VALUES) {
        for (double x : VALUES) {
          array.set(index, start);
          double updated = array.accumulateAndGet(index, x, Double::max);
          double expectedMax = max(start, x);
          assertBitEquals(expectedMax, updated);
          assertBitEquals(expectedMax, array.get(index));
        }
      }
    }
  }

  /** getAndUpdate adds given value to current via updater and returns previous value. */
  public void testGetAndUpdateWithSum() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int index : EDGE_INDEXES) {
      for (double start : VALUES) {
        for (double delta : VALUES) {
          array.set(index, start);
          double previous = array.getAndUpdate(index, value -> value + delta);
          assertBitEquals(start, previous);
          assertBitEquals(start + delta, array.get(index));
        }
      }
    }
  }

  /** getAndUpdate subtracts given value via updater and returns previous value. */
  public void testGetAndUpdateWithSubtract() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int index : EDGE_INDEXES) {
      for (double start : VALUES) {
        for (double delta : VALUES) {
          array.set(index, start);
          double previous = array.getAndUpdate(index, value -> value - delta);
          assertBitEquals(start, previous);
          assertBitEquals(start - delta, array.get(index));
        }
      }
    }
  }

  /** updateAndGet adds given value via updater and returns current value. */
  public void testUpdateAndGetWithSum() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int index : EDGE_INDEXES) {
      for (double start : VALUES) {
        for (double delta : VALUES) {
          array.set(index, start);
          double updated = array.updateAndGet(index, value -> value + delta);
          assertBitEquals(start + delta, updated);
          assertBitEquals(start + delta, array.get(index));
        }
      }
    }
  }

  /** updateAndGet subtracts given value via updater and returns current value. */
  public void testUpdateAndGetWithSubtract() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int index : EDGE_INDEXES) {
      for (double start : VALUES) {
        for (double delta : VALUES) {
          array.set(index, start);
          double updated = array.updateAndGet(index, value -> value - delta);
          assertBitEquals(start - delta, updated);
          assertBitEquals(start - delta, array.get(index));
        }
      }
    }
  }

  static final long COUNTDOWN = 100000;

  /**
   * Worker that decrements all elements in the array until they reach zero.
   * It records how many successful decrements it performed.
   */
  class Counter extends CheckedRunnable {
    final AtomicDoubleArray array;
    volatile long successfulDecrements;

    Counter(AtomicDoubleArray array) {
      this.array = array;
    }

    @Override
    public void realRun() {
      while (true) {
        boolean allZero = true;
        for (int i = 0; i < array.length(); i++) {
          double value = array.get(i);
          assertTrue(value >= 0);
          if (value != 0.0) {
            allZero = false;
            if (array.compareAndSet(i, value, value - 1.0)) {
              ++successfulDecrements;
            }
          }
        }
        if (allZero) {
          break;
        }
      }
    }
  }

  /**
   * Multiple threads using same array of counters successfully update a number of times equal to
   * the total initial count.
   */
  public void testCountingInMultipleThreads() throws InterruptedException {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i = 0; i < SIZE; i++) {
      array.set(i, (double) COUNTDOWN);
    }

    Counter c1 = new Counter(array);
    Counter c2 = new Counter(array);
    Thread t1 = newStartedThread(c1);
    Thread t2 = newStartedThread(c2);

    awaitTermination(t1);
    awaitTermination(t2);

    assertEquals(SIZE * COUNTDOWN, c1.successfulDecrements + c2.successfulDecrements);
  }

  /** A deserialized serialized array holds same values (but is a distinct instance). */
  public void testSerialization() throws Exception {
    AtomicDoubleArray x = new AtomicDoubleArray(SIZE);
    for (int i = 0; i < SIZE; i++) {
      x.set(i, (double) -i);
    }
    AtomicDoubleArray y = serialClone(x);
    assertTrue(x != y);
    assertEquals(x.length(), y.length());
    for (int i = 0; i < SIZE; i++) {
      assertBitEquals(x.get(i), y.get(i));
    }

    AtomicDoubleArray a = new AtomicDoubleArray(VALUES);
    AtomicDoubleArray b = serialClone(a);
    assertFalse(a.equals(b));
    assertFalse(b.equals(a));
    assertEquals(a.length(), b.length());
    for (int i = 0; i < VALUES.length; i++) {
      assertBitEquals(a.get(i), b.get(i));
    }
  }

  /** toString returns current value. */
  public void testToString() {
    AtomicDoubleArray array = new AtomicDoubleArray(VALUES);
    assertEquals(Arrays.toString(VALUES), array.toString());
    assertEquals("[]", new AtomicDoubleArray(0).toString());
    assertEquals("[]", new AtomicDoubleArray(new double[0]).toString());
  }

  /** compareAndSet treats +0.0 and -0.0 as distinct values (bitwise distinct). */
  public void testDistinctZeros() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    final double negativeZero = -0.0;
    final double positiveZero = +0.0;

    for (int index : EDGE_INDEXES) {
      // Initially +0.0
      assertFalse(array.compareAndSet(index, negativeZero, 7.0));
      assertFalse(array.weakCompareAndSet(index, negativeZero, 7.0));
      assertBitEquals(positiveZero, array.get(index));

      // Switch to -0.0
      assertTrue(array.compareAndSet(index, positiveZero, negativeZero));
      assertBitEquals(negativeZero, array.get(index));

      // Now at -0.0, so expecting +0.0 should fail
      assertFalse(array.compareAndSet(index, positiveZero, 7.0));
      assertFalse(array.weakCompareAndSet(index, positiveZero, 7.0));
      assertBitEquals(negativeZero, array.get(index));
    }
  }
}
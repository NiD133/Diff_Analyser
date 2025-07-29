package com.google.common.util.concurrent;

import static java.lang.Math.max;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.testing.NullPointerTester;
import java.util.Arrays;
import org.jspecify.annotations.NullUnmarked;

/** Unit test for {@link AtomicDoubleArray}. */
@NullUnmarked
public class AtomicDoubleArrayTest extends JSR166TestCase {

  private static final double[] TEST_VALUES = {
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

  /** Checks if two doubles are bitwise equal. */
  static boolean areBitsEqual(double x, double y) {
    return Double.doubleToRawLongBits(x) == Double.doubleToRawLongBits(y);
  }

  /** Asserts that two doubles are bitwise equal. */
  static void assertBitsEqual(double x, double y) {
    assertEquals(Double.doubleToRawLongBits(x), Double.doubleToRawLongBits(y));
  }

  @J2ktIncompatible
  @GwtIncompatible // NullPointerTester
  public void testNullPointerHandling() {
    new NullPointerTester().testAllPublicStaticMethods(AtomicDoubleArray.class);
    new NullPointerTester().testAllPublicConstructors(AtomicDoubleArray.class);
    new NullPointerTester().testAllPublicInstanceMethods(new AtomicDoubleArray(1));
  }

  /** Test constructor initializes array with zeros. */
  public void testConstructorInitializesWithZeros() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i = 0; i < SIZE; i++) {
      assertBitsEqual(0.0, array.get(i));
    }
  }

  /** Test constructor throws NullPointerException for null array. */
  public void testConstructorThrowsNPEForNullArray() {
    double[] nullArray = null;
    assertThrows(NullPointerException.class, () -> new AtomicDoubleArray(nullArray));
  }

  /** Test constructor copies values from provided array. */
  public void testConstructorCopiesValuesFromArray() {
    AtomicDoubleArray array = new AtomicDoubleArray(TEST_VALUES);
    assertEquals(TEST_VALUES.length, array.length());
    for (int i = 0; i < TEST_VALUES.length; i++) {
      assertBitsEqual(TEST_VALUES[i], array.get(i));
    }
  }

  /** Test constructor with empty array results in zero length. */
  public void testConstructorWithEmptyArray() {
    AtomicDoubleArray array = new AtomicDoubleArray(new double[0]);
    assertEquals(0, array.length());
    assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
  }

  /** Test constructor with zero length results in zero length. */
  public void testConstructorWithZeroLength() {
    AtomicDoubleArray array = new AtomicDoubleArray(0);
    assertEquals(0, array.length());
    assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
  }

  /** Test out-of-bounds access throws IndexOutOfBoundsException. */
  public void testOutOfBoundsAccessThrowsException() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int index : new int[] {-1, SIZE}) {
      assertThrows(IndexOutOfBoundsException.class, () -> array.get(index));
      assertThrows(IndexOutOfBoundsException.class, () -> array.set(index, 1.0));
      assertThrows(IndexOutOfBoundsException.class, () -> array.lazySet(index, 1.0));
      assertThrows(IndexOutOfBoundsException.class, () -> array.compareAndSet(index, 1.0, 2.0));
      assertThrows(IndexOutOfBoundsException.class, () -> array.weakCompareAndSet(index, 1.0, 2.0));
      assertThrows(IndexOutOfBoundsException.class, () -> array.getAndAdd(index, 1.0));
      assertThrows(IndexOutOfBoundsException.class, () -> array.addAndGet(index, 1.0));
    }
  }

  /** Test get and set operations. */
  public void testGetAndSetOperations() {
    AtomicDoubleArray array = new AtomicDoubleArray(TEST_VALUES.length);
    for (int i = 0; i < TEST_VALUES.length; i++) {
      assertBitsEqual(0.0, array.get(i));
      array.set(i, TEST_VALUES[i]);
      assertBitsEqual(TEST_VALUES[i], array.get(i));
      array.set(i, -3.0);
      assertBitsEqual(-3.0, array.get(i));
    }
  }

  /** Test lazySet operation. */
  public void testLazySetOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(TEST_VALUES.length);
    for (int i = 0; i < TEST_VALUES.length; i++) {
      assertBitsEqual(0.0, array.get(i));
      array.lazySet(i, TEST_VALUES[i]);
      assertBitsEqual(TEST_VALUES[i], array.get(i));
      array.lazySet(i, -3.0);
      assertBitsEqual(-3.0, array.get(i));
    }
  }

  /** Test compareAndSet operation. */
  public void testCompareAndSetOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      double previousValue = 0.0;
      double unusedValue = Math.E + Math.PI;
      for (double value : TEST_VALUES) {
        assertBitsEqual(previousValue, array.get(i));
        assertFalse(array.compareAndSet(i, unusedValue, value));
        assertBitsEqual(previousValue, array.get(i));
        assertTrue(array.compareAndSet(i, previousValue, value));
        assertBitsEqual(value, array.get(i));
        previousValue = value;
      }
    }
  }

  /** Test compareAndSet operation in multiple threads. */
  public void testCompareAndSetInMultipleThreads() throws InterruptedException {
    AtomicDoubleArray array = new AtomicDoubleArray(1);
    array.set(0, 1.0);
    Thread thread =
        newStartedThread(
            new CheckedRunnable() {
              @Override
              @SuppressWarnings("ThreadPriorityCheck") // doing our best to test for races
              public void realRun() {
                while (!array.compareAndSet(0, 2.0, 3.0)) {
                  Thread.yield();
                }
              }
            });

    assertTrue(array.compareAndSet(0, 1.0, 2.0));
    awaitTermination(thread);
    assertBitsEqual(3.0, array.get(0));
  }

  /** Test weakCompareAndSet operation. */
  public void testWeakCompareAndSetOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      double previousValue = 0.0;
      double unusedValue = Math.E + Math.PI;
      for (double value : TEST_VALUES) {
        assertBitsEqual(previousValue, array.get(i));
        assertFalse(array.weakCompareAndSet(i, unusedValue, value));
        assertBitsEqual(previousValue, array.get(i));
        while (!array.weakCompareAndSet(i, previousValue, value)) {
          // Retry until successful
        }
        assertBitsEqual(value, array.get(i));
        previousValue = value;
      }
    }
  }

  /** Test getAndSet operation. */
  public void testGetAndSetOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      double previousValue = 0.0;
      for (double value : TEST_VALUES) {
        assertBitsEqual(previousValue, array.getAndSet(i, value));
        previousValue = value;
      }
    }
  }

  /** Test getAndAdd operation. */
  public void testGetAndAddOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double x : TEST_VALUES) {
        for (double y : TEST_VALUES) {
          array.set(i, x);
          double result = array.getAndAdd(i, y);
          assertBitsEqual(x, result);
          assertBitsEqual(x + y, array.get(i));
        }
      }
    }
  }

  /** Test addAndGet operation. */
  public void testAddAndGetOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double x : TEST_VALUES) {
        for (double y : TEST_VALUES) {
          array.set(i, x);
          double result = array.addAndGet(i, y);
          assertBitsEqual(x + y, result);
          assertBitsEqual(x + y, array.get(i));
        }
      }
    }
  }

  /** Test getAndAccumulate with sum operation. */
  public void testGetAndAccumulateWithSumOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double x : TEST_VALUES) {
        for (double y : TEST_VALUES) {
          array.set(i, x);
          double result = array.getAndAccumulate(i, y, Double::sum);
          assertBitsEqual(x, result);
          assertBitsEqual(x + y, array.get(i));
        }
      }
    }
  }

  /** Test getAndAccumulate with max operation. */
  public void testGetAndAccumulateWithMaxOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double x : TEST_VALUES) {
        for (double y : TEST_VALUES) {
          array.set(i, x);
          double result = array.getAndAccumulate(i, y, Double::max);
          double expectedMax = max(x, y);
          assertBitsEqual(x, result);
          assertBitsEqual(expectedMax, array.get(i));
        }
      }
    }
  }

  /** Test accumulateAndGet with sum operation. */
  public void testAccumulateAndGetWithSumOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double x : TEST_VALUES) {
        for (double y : TEST_VALUES) {
          array.set(i, x);
          double result = array.accumulateAndGet(i, y, Double::sum);
          assertBitsEqual(x + y, result);
          assertBitsEqual(x + y, array.get(i));
        }
      }
    }
  }

  /** Test accumulateAndGet with max operation. */
  public void testAccumulateAndGetWithMaxOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double x : TEST_VALUES) {
        for (double y : TEST_VALUES) {
          array.set(i, x);
          double result = array.accumulateAndGet(i, y, Double::max);
          double expectedMax = max(x, y);
          assertBitsEqual(expectedMax, result);
          assertBitsEqual(expectedMax, array.get(i));
        }
      }
    }
  }

  /** Test getAndUpdate with sum operation. */
  public void testGetAndUpdateWithSumOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double x : TEST_VALUES) {
        for (double y : TEST_VALUES) {
          array.set(i, x);
          double result = array.getAndUpdate(i, value -> value + y);
          assertBitsEqual(x, result);
          assertBitsEqual(x + y, array.get(i));
        }
      }
    }
  }

  /** Test getAndUpdate with subtract operation. */
  public void testGetAndUpdateWithSubtractOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double x : TEST_VALUES) {
        for (double y : TEST_VALUES) {
          array.set(i, x);
          double result = array.getAndUpdate(i, value -> value - y);
          assertBitsEqual(x, result);
          assertBitsEqual(x - y, array.get(i));
        }
      }
    }
  }

  /** Test updateAndGet with sum operation. */
  public void testUpdateAndGetWithSumOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double x : TEST_VALUES) {
        for (double y : TEST_VALUES) {
          array.set(i, x);
          double result = array.updateAndGet(i, value -> value + y);
          assertBitsEqual(x + y, result);
          assertBitsEqual(x + y, array.get(i));
        }
      }
    }
  }

  /** Test updateAndGet with subtract operation. */
  public void testUpdateAndGetWithSubtractOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double x : TEST_VALUES) {
        for (double y : TEST_VALUES) {
          array.set(i, x);
          double result = array.updateAndGet(i, value -> value - y);
          assertBitsEqual(x - y, result);
          assertBitsEqual(x - y, array.get(i));
        }
      }
    }
  }

  static final long COUNTDOWN = 100000;

  class Counter extends CheckedRunnable {
    final AtomicDoubleArray array;
    volatile long counts;

    Counter(AtomicDoubleArray array) {
      this.array = array;
    }

    @Override
    public void realRun() {
      while (true) {
        boolean done = true;
        for (int i = 0; i < array.length(); i++) {
          double value = array.get(i);
          assertTrue(value >= 0);
          if (value != 0) {
            done = false;
            if (array.compareAndSet(i, value, value - 1.0)) {
              ++counts;
            }
          }
        }
        if (done) {
          break;
        }
      }
    }
  }

  /** Test multiple threads updating the same array of counters. */
  public void testCountingInMultipleThreads() throws InterruptedException {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i = 0; i < SIZE; i++) {
      array.set(i, (double) COUNTDOWN);
    }
    Counter counter1 = new Counter(array);
    Counter counter2 = new Counter(array);
    Thread thread1 = newStartedThread(counter1);
    Thread thread2 = newStartedThread(counter2);
    awaitTermination(thread1);
    awaitTermination(thread2);
    assertEquals(SIZE * COUNTDOWN, counter1.counts + counter2.counts);
  }

  /** Test serialization and deserialization of the array. */
  public void testSerialization() throws Exception {
    AtomicDoubleArray originalArray = new AtomicDoubleArray(SIZE);
    for (int i = 0; i < SIZE; i++) {
      originalArray.set(i, (double) -i);
    }
    AtomicDoubleArray clonedArray = serialClone(originalArray);
    assertTrue(originalArray != clonedArray);
    assertEquals(originalArray.length(), clonedArray.length());
    for (int i = 0; i < SIZE; i++) {
      assertBitsEqual(originalArray.get(i), clonedArray.get(i));
    }

    AtomicDoubleArray arrayA = new AtomicDoubleArray(TEST_VALUES);
    AtomicDoubleArray arrayB = serialClone(arrayA);
    assertFalse(arrayA.equals(arrayB));
    assertFalse(arrayB.equals(arrayA));
    assertEquals(arrayA.length(), arrayB.length());
    for (int i = 0; i < TEST_VALUES.length; i++) {
      assertBitsEqual(arrayA.get(i), arrayB.get(i));
    }
  }

  /** Test toString method returns current values. */
  public void testToStringMethod() {
    AtomicDoubleArray array = new AtomicDoubleArray(TEST_VALUES);
    assertEquals(Arrays.toString(TEST_VALUES), array.toString());
    assertEquals("[]", new AtomicDoubleArray(0).toString());
    assertEquals("[]", new AtomicDoubleArray(new double[0]).toString());
  }

  /** Test compareAndSet treats +0.0 and -0.0 as distinct values. */
  public void testDistinctZerosHandling() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      assertFalse(array.compareAndSet(i, -0.0, 7.0));
      assertFalse(array.weakCompareAndSet(i, -0.0, 7.0));
      assertBitsEqual(+0.0, array.get(i));
      assertTrue(array.compareAndSet(i, +0.0, -0.0));
      assertBitsEqual(-0.0, array.get(i));
      assertFalse(array.compareAndSet(i, +0.0, 7.0));
      assertFalse(array.weakCompareAndSet(i, +0.0, 7.0));
      assertBitsEqual(-0.0, array.get(i));
    }
  }
}
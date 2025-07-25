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
    Double.NEGATIVE_INFINITY, -Double.MAX_VALUE, (double) Long.MIN_VALUE, (double) Integer.MIN_VALUE,
    -Math.PI, -1.0, -Double.MIN_VALUE, -0.0, +0.0, Double.MIN_VALUE, 1.0, Math.PI,
    (double) Integer.MAX_VALUE, (double) Long.MAX_VALUE, Double.MAX_VALUE, Double.POSITIVE_INFINITY,
    Double.NaN, Float.MAX_VALUE,
  };

  /** Checks if two doubles are bitwise equal. */
  static boolean areDoublesBitwiseEqual(double x, double y) {
    return Double.doubleToRawLongBits(x) == Double.doubleToRawLongBits(y);
  }

  /** Asserts that two doubles are bitwise equal. */
  static void assertDoublesBitwiseEqual(double expected, double actual) {
    assertEquals(Double.doubleToRawLongBits(expected), Double.doubleToRawLongBits(actual));
  }

  @J2ktIncompatible
  @GwtIncompatible // NullPointerTester
  public void testNullPointerHandling() {
    NullPointerTester tester = new NullPointerTester();
    tester.testAllPublicStaticMethods(AtomicDoubleArray.class);
    tester.testAllPublicConstructors(AtomicDoubleArray.class);
    tester.testAllPublicInstanceMethods(new AtomicDoubleArray(1));
  }

  /** Tests that the constructor creates an array of the specified size with all elements initialized to zero. */
  public void testConstructorInitializesToZero() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i = 0; i < SIZE; i++) {
      assertDoublesBitwiseEqual(0.0, array.get(i));
    }
  }

  /** Tests that constructing with a null array throws a NullPointerException. */
  public void testConstructorWithNullArrayThrowsException() {
    assertThrows(NullPointerException.class, () -> new AtomicDoubleArray((double[]) null));
  }

  /** Tests that constructing with an array copies the array correctly. */
  public void testConstructorCopiesArray() {
    AtomicDoubleArray array = new AtomicDoubleArray(TEST_VALUES);
    assertEquals(TEST_VALUES.length, array.length());
    for (int i = 0; i < TEST_VALUES.length; i++) {
      assertDoublesBitwiseEqual(TEST_VALUES[i], array.get(i));
    }
  }

  /** Tests that constructing with an empty array results in an array of size 0. */
  public void testConstructorWithEmptyArray() {
    AtomicDoubleArray array = new AtomicDoubleArray(new double[0]);
    assertEquals(0, array.length());
    assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
  }

  /** Tests that constructing with a length of zero results in an array of size 0. */
  public void testConstructorWithZeroLength() {
    AtomicDoubleArray array = new AtomicDoubleArray(0);
    assertEquals(0, array.length());
    assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
  }

  /** Tests that accessing out-of-bounds indices throws an IndexOutOfBoundsException. */
  public void testOutOfBoundsIndexing() {
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

  /** Tests that get and set operations work correctly. */
  public void testGetAndSetOperations() {
    AtomicDoubleArray array = new AtomicDoubleArray(TEST_VALUES.length);
    for (int i = 0; i < TEST_VALUES.length; i++) {
      assertDoublesBitwiseEqual(0.0, array.get(i));
      array.set(i, TEST_VALUES[i]);
      assertDoublesBitwiseEqual(TEST_VALUES[i], array.get(i));
      array.set(i, -3.0);
      assertDoublesBitwiseEqual(-3.0, array.get(i));
    }
  }

  /** Tests that lazySet works correctly. */
  public void testLazySetOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(TEST_VALUES.length);
    for (int i = 0; i < TEST_VALUES.length; i++) {
      assertDoublesBitwiseEqual(0.0, array.get(i));
      array.lazySet(i, TEST_VALUES[i]);
      assertDoublesBitwiseEqual(TEST_VALUES[i], array.get(i));
      array.lazySet(i, -3.0);
      assertDoublesBitwiseEqual(-3.0, array.get(i));
    }
  }

  /** Tests that compareAndSet works correctly. */
  public void testCompareAndSetOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      double previousValue = 0.0;
      double unusedValue = Math.E + Math.PI;
      for (double newValue : TEST_VALUES) {
        assertDoublesBitwiseEqual(previousValue, array.get(i));
        assertFalse(array.compareAndSet(i, unusedValue, newValue));
        assertDoublesBitwiseEqual(previousValue, array.get(i));
        assertTrue(array.compareAndSet(i, previousValue, newValue));
        assertDoublesBitwiseEqual(newValue, array.get(i));
        previousValue = newValue;
      }
    }
  }

  /** Tests that compareAndSet works correctly across multiple threads. */
  public void testCompareAndSetInMultipleThreads() throws InterruptedException {
    AtomicDoubleArray array = new AtomicDoubleArray(1);
    array.set(0, 1.0);
    Thread thread = newStartedThread(() -> {
      while (!array.compareAndSet(0, 2.0, 3.0)) {
        Thread.yield();
      }
    });

    assertTrue(array.compareAndSet(0, 1.0, 2.0));
    awaitTermination(thread);
    assertDoublesBitwiseEqual(3.0, array.get(0));
  }

  /** Tests that weakCompareAndSet works correctly. */
  public void testWeakCompareAndSetOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      double previousValue = 0.0;
      double unusedValue = Math.E + Math.PI;
      for (double newValue : TEST_VALUES) {
        assertDoublesBitwiseEqual(previousValue, array.get(i));
        assertFalse(array.weakCompareAndSet(i, unusedValue, newValue));
        assertDoublesBitwiseEqual(previousValue, array.get(i));
        while (!array.weakCompareAndSet(i, previousValue, newValue)) {
          // Retry until successful
        }
        assertDoublesBitwiseEqual(newValue, array.get(i));
        previousValue = newValue;
      }
    }
  }

  /** Tests that getAndSet returns the previous value and sets the new value. */
  public void testGetAndSetReturnsPreviousValue() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      double previousValue = 0.0;
      for (double newValue : TEST_VALUES) {
        assertDoublesBitwiseEqual(previousValue, array.getAndSet(i, newValue));
        previousValue = newValue;
      }
    }
  }

  /** Tests that getAndAdd returns the previous value and adds the given value. */
  public void testGetAndAddOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double delta : TEST_VALUES) {
          array.set(i, initialValue);
          double previousValue = array.getAndAdd(i, delta);
          assertDoublesBitwiseEqual(initialValue, previousValue);
          assertDoublesBitwiseEqual(initialValue + delta, array.get(i));
        }
      }
    }
  }

  /** Tests that addAndGet adds the given value and returns the updated value. */
  public void testAddAndGetOperation() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double delta : TEST_VALUES) {
          array.set(i, initialValue);
          double updatedValue = array.addAndGet(i, delta);
          assertDoublesBitwiseEqual(initialValue + delta, updatedValue);
          assertDoublesBitwiseEqual(initialValue + delta, array.get(i));
        }
      }
    }
  }

  /** Tests getAndAccumulate with sum operation. */
  public void testGetAndAccumulateWithSum() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double delta : TEST_VALUES) {
          array.set(i, initialValue);
          double previousValue = array.getAndAccumulate(i, delta, Double::sum);
          assertDoublesBitwiseEqual(initialValue, previousValue);
          assertDoublesBitwiseEqual(initialValue + delta, array.get(i));
        }
      }
    }
  }

  /** Tests getAndAccumulate with max operation. */
  public void testGetAndAccumulateWithMax() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double delta : TEST_VALUES) {
          array.set(i, initialValue);
          double previousValue = array.getAndAccumulate(i, delta, Double::max);
          double expectedMax = max(initialValue, delta);
          assertDoublesBitwiseEqual(initialValue, previousValue);
          assertDoublesBitwiseEqual(expectedMax, array.get(i));
        }
      }
    }
  }

  /** Tests accumulateAndGet with sum operation. */
  public void testAccumulateAndGetWithSum() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double delta : TEST_VALUES) {
          array.set(i, initialValue);
          double updatedValue = array.accumulateAndGet(i, delta, Double::sum);
          assertDoublesBitwiseEqual(initialValue + delta, updatedValue);
          assertDoublesBitwiseEqual(initialValue + delta, array.get(i));
        }
      }
    }
  }

  /** Tests accumulateAndGet with max operation. */
  public void testAccumulateAndGetWithMax() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double delta : TEST_VALUES) {
          array.set(i, initialValue);
          double updatedValue = array.accumulateAndGet(i, delta, Double::max);
          double expectedMax = max(initialValue, delta);
          assertDoublesBitwiseEqual(expectedMax, updatedValue);
          assertDoublesBitwiseEqual(expectedMax, array.get(i));
        }
      }
    }
  }

  /** Tests getAndUpdate with sum operation. */
  public void testGetAndUpdateWithSum() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double delta : TEST_VALUES) {
          array.set(i, initialValue);
          double previousValue = array.getAndUpdate(i, value -> value + delta);
          assertDoublesBitwiseEqual(initialValue, previousValue);
          assertDoublesBitwiseEqual(initialValue + delta, array.get(i));
        }
      }
    }
  }

  /** Tests getAndUpdate with subtract operation. */
  public void testGetAndUpdateWithSubtract() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double delta : TEST_VALUES) {
          array.set(i, initialValue);
          double previousValue = array.getAndUpdate(i, value -> value - delta);
          assertDoublesBitwiseEqual(initialValue, previousValue);
          assertDoublesBitwiseEqual(initialValue - delta, array.get(i));
        }
      }
    }
  }

  /** Tests updateAndGet with sum operation. */
  public void testUpdateAndGetWithSum() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double delta : TEST_VALUES) {
          array.set(i, initialValue);
          double updatedValue = array.updateAndGet(i, value -> value + delta);
          assertDoublesBitwiseEqual(initialValue + delta, updatedValue);
          assertDoublesBitwiseEqual(initialValue + delta, array.get(i));
        }
      }
    }
  }

  /** Tests updateAndGet with subtract operation. */
  public void testUpdateAndGetWithSubtract() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double delta : TEST_VALUES) {
          array.set(i, initialValue);
          double updatedValue = array.updateAndGet(i, value -> value - delta);
          assertDoublesBitwiseEqual(initialValue - delta, updatedValue);
          assertDoublesBitwiseEqual(initialValue - delta, array.get(i));
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

  /** Tests that multiple threads can update the array correctly. */
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

  /** Tests that a deserialized array holds the same values as the original. */
  public void testSerialization() throws Exception {
    AtomicDoubleArray originalArray = new AtomicDoubleArray(SIZE);
    for (int i = 0; i < SIZE; i++) {
      originalArray.set(i, (double) -i);
    }
    AtomicDoubleArray deserializedArray = serialClone(originalArray);
    assertTrue(originalArray != deserializedArray);
    assertEquals(originalArray.length(), deserializedArray.length());
    for (int i = 0; i < SIZE; i++) {
      assertDoublesBitwiseEqual(originalArray.get(i), deserializedArray.get(i));
    }

    AtomicDoubleArray arrayA = new AtomicDoubleArray(TEST_VALUES);
    AtomicDoubleArray arrayB = serialClone(arrayA);
    assertFalse(arrayA.equals(arrayB));
    assertFalse(arrayB.equals(arrayA));
    assertEquals(arrayA.length(), arrayB.length());
    for (int i = 0; i < TEST_VALUES.length; i++) {
      assertDoublesBitwiseEqual(arrayA.get(i), arrayB.get(i));
    }
  }

  /** Tests that toString returns the correct string representation of the array. */
  public void testToString() {
    AtomicDoubleArray array = new AtomicDoubleArray(TEST_VALUES);
    assertEquals(Arrays.toString(TEST_VALUES), array.toString());
    assertEquals("[]", new AtomicDoubleArray(0).toString());
    assertEquals("[]", new AtomicDoubleArray(new double[0]).toString());
  }

  /** Tests that compareAndSet treats +0.0 and -0.0 as distinct values. */
  public void testDistinctZeros() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      assertFalse(array.compareAndSet(i, -0.0, 7.0));
      assertFalse(array.weakCompareAndSet(i, -0.0, 7.0));
      assertDoublesBitwiseEqual(+0.0, array.get(i));
      assertTrue(array.compareAndSet(i, +0.0, -0.0));
      assertDoublesBitwiseEqual(-0.0, array.get(i));
      assertFalse(array.compareAndSet(i, +0.0, 7.0));
      assertFalse(array.weakCompareAndSet(i, +0.0, 7.0));
      assertDoublesBitwiseEqual(-0.0, array.get(i));
    }
  }
}
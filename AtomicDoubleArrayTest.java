package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.testing.NullPointerTester;
import java.util.Arrays;
import org.junit.Test;
import org.jspecify.annotations.NullUnmarked;

/** Unit tests for {@link AtomicDoubleArray}. */
@NullUnmarked
@GwtIncompatible
@J2ktIncompatible
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

  /**
   * Asserts that two doubles are bitwise equal, meaning their raw long bits representation is the
   * same. This is used because AtomicDoubleArray compares doubles this way.
   */
  private static void assertBitwiseEquals(double expected, double actual) {
    assertThat(Double.doubleToRawLongBits(actual)).isEqualTo(Double.doubleToRawLongBits(expected));
  }

  @Test
  public void testNulls() {
    NullPointerTester tester = new NullPointerTester();
    tester.testAllPublicStaticMethods(AtomicDoubleArray.class);
    tester.testAllPublicConstructors(AtomicDoubleArray.class);
    tester.testAllPublicInstanceMethods(new AtomicDoubleArray(1));
  }

  @Test
  public void testConstructor_createsArrayOfGivenSizeWithAllElementsZero() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i = 0; i < SIZE; i++) {
      assertBitwiseEquals(0.0, array.get(i));
    }
  }

  @Test
  public void testConstructor_withNullArray_throwsNPE() {
    assertThrows(NullPointerException.class, () -> new AtomicDoubleArray(null));
  }

  @Test
  public void testConstructor_withArray_createsCopy() {
    AtomicDoubleArray array = new AtomicDoubleArray(TEST_VALUES);
    assertThat(array.length()).isEqualTo(TEST_VALUES.length);
    for (int i = 0; i < TEST_VALUES.length; i++) {
      assertBitwiseEquals(TEST_VALUES[i], array.get(i));
    }
  }

  @Test
  public void testConstructor_withEmptyArray_hasSizeZero() {
    AtomicDoubleArray array = new AtomicDoubleArray(new double[0]);
    assertThat(array.length()).isEqualTo(0);
    assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
  }

  @Test
  public void testConstructor_withZeroLength_hasSizeZero() {
    AtomicDoubleArray array = new AtomicDoubleArray(0);
    assertThat(array.length()).isEqualTo(0);
    assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
  }

  @Test
  public void testIndexing_outOfBounds_throwsIndexOutOfBoundsException() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    int[] outOfBoundsIndices = {-1, SIZE};

    for (int index : outOfBoundsIndices) {
      assertThrows(IndexOutOfBoundsException.class, () -> array.get(index));
      assertThrows(IndexOutOfBoundsException.class, () -> array.set(index, 1.0));
      assertThrows(IndexOutOfBoundsException.class, () -> array.lazySet(index, 1.0));
      assertThrows(
          IndexOutOfBoundsException.class, () -> array.compareAndSet(index, 1.0, 2.0));
      assertThrows(
          IndexOutOfBoundsException.class, () -> array.weakCompareAndSet(index, 1.0, 2.0));
      assertThrows(IndexOutOfBoundsException.class, () -> array.getAndAdd(index, 1.0));
      assertThrows(IndexOutOfBoundsException.class, () -> array.addAndGet(index, 1.0));
    }
  }

  @Test
  public void testGetSet_returnsLastValueSet() {
    AtomicDoubleArray array = new AtomicDoubleArray(TEST_VALUES.length);
    for (int i = 0; i < TEST_VALUES.length; i++) {
      assertBitwiseEquals(0.0, array.get(i));
      array.set(i, TEST_VALUES[i]);
      assertBitwiseEquals(TEST_VALUES[i], array.get(i));
      array.set(i, -3.0);
      assertBitwiseEquals(-3.0, array.get(i));
    }
  }

  @Test
  public void testGetLazySet_returnsLastValueSet() {
    AtomicDoubleArray array = new AtomicDoubleArray(TEST_VALUES.length);
    for (int i = 0; i < TEST_VALUES.length; i++) {
      assertBitwiseEquals(0.0, array.get(i));
      array.lazySet(i, TEST_VALUES[i]);
      assertBitwiseEquals(TEST_VALUES[i], array.get(i));
      array.lazySet(i, -3.0);
      assertBitwiseEquals(-3.0, array.get(i));
    }
  }

  @Test
  public void testCompareAndSet_succeedsOnlyIfEqualToExpectedValue() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      double initialValue = 0.0;
      double newValue = Math.E + Math.PI; // Some arbitrary different value.

      for (double testValue : TEST_VALUES) {
        assertBitwiseEquals(initialValue, array.get(i));
        assertThat(array.compareAndSet(i, newValue, testValue)).isFalse();
        assertBitwiseEquals(initialValue, array.get(i));
        assertThat(array.compareAndSet(i, initialValue, testValue)).isTrue();
        assertBitwiseEquals(testValue, array.get(i));
        initialValue = testValue;
      }
    }
  }

  @Test
  public void testCompareAndSetInMultipleThreads_enablesWaitingThreadToSucceed()
      throws InterruptedException {
    AtomicDoubleArray array = new AtomicDoubleArray(1);
    array.set(0, 1.0);

    Thread thread =
        newStartedThread(
            () -> {
              while (!array.compareAndSet(0, 2.0, 3.0)) {
                Thread.yield();
              }
            });

    assertThat(array.compareAndSet(0, 1.0, 2.0)).isTrue();
    awaitTermination(thread);
    assertBitwiseEquals(3.0, array.get(0));
  }

  @Test
  public void testWeakCompareAndSet_succeedsEventuallyIfEqualToExpectedValue() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      double initialValue = 0.0;
      double newValue = Math.E + Math.PI; // Some arbitrary different value.

      for (double testValue : TEST_VALUES) {
        assertBitwiseEquals(initialValue, array.get(i));
        assertThat(array.weakCompareAndSet(i, newValue, testValue)).isFalse();
        assertBitwiseEquals(initialValue, array.get(i));
        while (!array.weakCompareAndSet(i, initialValue, testValue)) {
          ; // Spin until successful.
        }
        assertBitwiseEquals(testValue, array.get(i));
        initialValue = testValue;
      }
    }
  }

  @Test
  public void testGetAndSet_returnsPreviousValueAndSetsToGivenValue() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      double previousValue = 0.0;
      for (double newValue : TEST_VALUES) {
        assertBitwiseEquals(previousValue, array.getAndSet(i, newValue));
        assertBitwiseEquals(newValue, array.get(i)); // Verify set.
        previousValue = newValue;
      }
    }
  }

  @Test
  public void testGetAndAdd_returnsPreviousValueAndAddsGivenValue() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double delta : TEST_VALUES) {
          array.set(i, initialValue);
          double previousValue = array.getAndAdd(i, delta);
          assertBitwiseEquals(initialValue, previousValue);
          assertBitwiseEquals(initialValue + delta, array.get(i));
        }
      }
    }
  }

  @Test
  public void testAddAndGet_addsGivenValueAndReturnsNewValue() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double delta : TEST_VALUES) {
          array.set(i, initialValue);
          double newValue = array.addAndGet(i, delta);
          assertBitwiseEquals(initialValue + delta, newValue);
          assertBitwiseEquals(initialValue + delta, array.get(i));
        }
      }
    }
  }

  @Test
  public void testGetAndAccumulateWithSum_returnsPreviousValueAndAccumulates() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double accumulatorValue : TEST_VALUES) {
          array.set(i, initialValue);
          double previousValue = array.getAndAccumulate(i, accumulatorValue, Double::sum);
          assertBitwiseEquals(initialValue, previousValue);
          assertBitwiseEquals(initialValue + accumulatorValue, array.get(i));
        }
      }
    }
  }

  @Test
  public void testGetAndAccumulateWithMax_returnsPreviousValueAndAccumulatesMax() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double accumulatorValue : TEST_VALUES) {
          array.set(i, initialValue);
          double previousValue = array.getAndAccumulate(i, accumulatorValue, Math::max);
          double expectedMax = Math.max(initialValue, accumulatorValue);
          assertBitwiseEquals(initialValue, previousValue);
          assertBitwiseEquals(expectedMax, array.get(i));
        }
      }
    }
  }

  @Test
  public void testAccumulateAndGetWithSum_accumulatesAndReturnsNewValue() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double accumulatorValue : TEST_VALUES) {
          array.set(i, initialValue);
          double newValue = array.accumulateAndGet(i, accumulatorValue, Double::sum);
          assertBitwiseEquals(initialValue + accumulatorValue, newValue);
          assertBitwiseEquals(initialValue + accumulatorValue, array.get(i));
        }
      }
    }
  }

  @Test
  public void testAccumulateAndGetWithMax_accumulatesMaxAndReturnsNewValue() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double accumulatorValue : TEST_VALUES) {
          array.set(i, initialValue);
          double newValue = array.accumulateAndGet(i, accumulatorValue, Math::max);
          double expectedMax = Math.max(initialValue, accumulatorValue);
          assertBitwiseEquals(expectedMax, newValue);
          assertBitwiseEquals(expectedMax, array.get(i));
        }
      }
    }
  }

  @Test
  public void testGetAndUpdateWithSum_returnsPreviousValueAndUpdates() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double updateValue : TEST_VALUES) {
          array.set(i, initialValue);
          double previousValue = array.getAndUpdate(i, value -> value + updateValue);
          assertBitwiseEquals(initialValue, previousValue);
          assertBitwiseEquals(initialValue + updateValue, array.get(i));
        }
      }
    }
  }

  @Test
  public void testGetAndUpdateWithSubtract_returnsPreviousValueAndUpdates() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double updateValue : TEST_VALUES) {
          array.set(i, initialValue);
          double previousValue = array.getAndUpdate(i, value -> value - updateValue);
          assertBitwiseEquals(initialValue, previousValue);
          assertBitwiseEquals(initialValue - updateValue, array.get(i));
        }
      }
    }
  }

  @Test
  public void testUpdateAndGetWithSum_updatesAndReturnsNewValue() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double updateValue : TEST_VALUES) {
          array.set(i, initialValue);
          double newValue = array.updateAndGet(i, value -> value + updateValue);
          assertBitwiseEquals(initialValue + updateValue, newValue);
          assertBitwiseEquals(initialValue + updateValue, array.get(i));
        }
      }
    }
  }

  @Test
  public void testUpdateAndGetWithSubtract_updatesAndReturnsNewValue() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      for (double initialValue : TEST_VALUES) {
        for (double updateValue : TEST_VALUES) {
          array.set(i, initialValue);
          double newValue = array.updateAndGet(i, value -> value - updateValue);
          assertBitwiseEquals(initialValue - updateValue, newValue);
          assertBitwiseEquals(initialValue - updateValue, array.get(i));
        }
      }
    }
  }

  private static final long COUNTDOWN = 100000;

  private static class Counter extends CheckedRunnable {
    final AtomicDoubleArray array;
    volatile long counts;

    Counter(AtomicDoubleArray a) {
      array = a;
    }

    @Override
    public void realRun() {
      for (; ; ) {
        boolean done = true;
        for (int i = 0; i < array.length(); i++) {
          double v = array.get(i);
          assertThat(v).isAtLeast(0.0);
          if (v != 0.0) {
            done = false;
            if (array.compareAndSet(i, v, v - 1.0)) {
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

  @Test
  public void testCountingInMultipleThreads_successfullyUpdatesCounters() throws InterruptedException {
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
    assertThat(counter1.counts + counter2.counts).isEqualTo(SIZE * COUNTDOWN);
  }

  @Test
  public void testSerialization_deserializedArrayHoldsSameValues() throws Exception {
    AtomicDoubleArray originalArray = new AtomicDoubleArray(SIZE);
    for (int i = 0; i < SIZE; i++) {
      originalArray.set(i, (double) -i);
    }
    AtomicDoubleArray deserializedArray = serialClone(originalArray);

    assertThat(deserializedArray).isNotSameInstanceAs(originalArray);
    assertThat(deserializedArray.length()).isEqualTo(originalArray.length());
    for (int i = 0; i < SIZE; i++) {
      assertBitwiseEquals(originalArray.get(i), deserializedArray.get(i));
    }

    AtomicDoubleArray originalArray2 = new AtomicDoubleArray(TEST_VALUES);
    AtomicDoubleArray deserializedArray2 = serialClone(originalArray2);

    assertThat(deserializedArray2).isNotEqualTo(originalArray2);
    assertThat(originalArray2).isNotEqualTo(deserializedArray2);
    assertThat(deserializedArray2.length()).isEqualTo(originalArray2.length());
    for (int i = 0; i < TEST_VALUES.length; i++) {
      assertBitwiseEquals(originalArray2.get(i), deserializedArray2.get(i));
    }
  }

  @Test
  public void testToString_returnsCurrentValue() {
    AtomicDoubleArray array = new AtomicDoubleArray(TEST_VALUES);
    assertThat(array.toString()).isEqualTo(Arrays.toString(TEST_VALUES));
    assertThat(new AtomicDoubleArray(0).toString()).isEqualTo("[]");
    assertThat(new AtomicDoubleArray(new double[0]).toString()).isEqualTo("[]");
  }

  @Test
  public void testDistinctZeros_compareAndSetTreatsPositiveAndNegativeZeroAsDistinct() {
    AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
    for (int i : new int[] {0, SIZE - 1}) {
      assertThat(array.compareAndSet(i, -0.0, 7.0)).isFalse();
      assertThat(array.weakCompareAndSet(i, -0.0, 7.0)).isFalse();
      assertBitwiseEquals(+0.0, array.get(i));
      assertThat(array.compareAndSet(i, +0.0, -0.0)).isTrue();
      assertBitwiseEquals(-0.0, array.get(i));
      assertThat(array.compareAndSet(i, +0.0, 7.0)).isFalse();
      assertThat(array.weakCompareAndSet(i, +0.0, 7.0)).isFalse();
      assertBitwiseEquals(-0.0, array.get(i));
    }
  }
}
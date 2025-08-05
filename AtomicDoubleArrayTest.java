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

/** Unit test for {@link AtomicDoubleArray}. */
@NullUnmarked
public class AtomicDoubleArrayTest extends JSR166TestCase {

  // Test constants
  private static final int DEFAULT_ARRAY_SIZE = 10; // Renamed from SIZE for clarity
  private static final long CONCURRENT_UPDATE_COUNT = 100000; // Renamed from COUNTDOWN
  
  // Special double values used for testing edge cases
  private static final double[] SPECIAL_DOUBLE_VALUES = {
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

  // Helper methods
  
  /** 
   * Checks if two doubles are bitwise equal.
   * AtomicDoubleArray uses bitwise comparison, not standard double equality.
   */
  private static boolean areBitwiseEqual(double x, double y) {
    return Double.doubleToRawLongBits(x) == Double.doubleToRawLongBits(y);
  }

  /** Asserts that two doubles are bitwise equal. */
  private static void assertBitwiseEquals(double expected, double actual) {
    assertEquals(
        "Doubles are not bitwise equal", 
        Double.doubleToRawLongBits(expected), 
        Double.doubleToRawLongBits(actual)
    );
  }

  // Constructor tests
  
  @Test
  public void testConstructor_WithSize_CreatesArrayWithZeroValues() {
    int size = 5;
    AtomicDoubleArray array = new AtomicDoubleArray(size);
    
    assertEquals(size, array.length());
    for (int i = 0; i < size; i++) {
      assertBitwiseEquals(0.0, array.get(i));
    }
  }

  @Test
  public void testConstructor_WithDoubleArray_CopiesAllValues() {
    AtomicDoubleArray array = new AtomicDoubleArray(SPECIAL_DOUBLE_VALUES);
    
    assertEquals(SPECIAL_DOUBLE_VALUES.length, array.length());
    for (int i = 0; i < SPECIAL_DOUBLE_VALUES.length; i++) {
      assertBitwiseEquals(SPECIAL_DOUBLE_VALUES[i], array.get(i));
    }
  }

  @Test
  public void testConstructor_WithNullArray_ThrowsNPE() {
    double[] nullArray = null;
    assertThrows(NullPointerException.class, () -> new AtomicDoubleArray(nullArray));
  }

  @Test
  public void testConstructor_WithEmptyArray_CreatesEmptyAtomicArray() {
    AtomicDoubleArray array = new AtomicDoubleArray(new double[0]);
    assertEquals(0, array.length());
    assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
  }

  @Test
  public void testConstructor_WithZeroSize_CreatesEmptyArray() {
    AtomicDoubleArray array = new AtomicDoubleArray(0);
    assertEquals(0, array.length());
    assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
  }

  // Boundary tests
  
  @Test
  public void testArrayBounds_InvalidIndices_ThrowIndexOutOfBoundsException() {
    AtomicDoubleArray array = new AtomicDoubleArray(DEFAULT_ARRAY_SIZE);
    int[] invalidIndices = {-1, DEFAULT_ARRAY_SIZE};
    
    for (int invalidIndex : invalidIndices) {
      assertThrows(IndexOutOfBoundsException.class, () -> array.get(invalidIndex));
      assertThrows(IndexOutOfBoundsException.class, () -> array.set(invalidIndex, 1.0));
      assertThrows(IndexOutOfBoundsException.class, () -> array.lazySet(invalidIndex, 1.0));
      assertThrows(IndexOutOfBoundsException.class, () -> array.compareAndSet(invalidIndex, 1.0, 2.0));
      assertThrows(IndexOutOfBoundsException.class, () -> array.weakCompareAndSet(invalidIndex, 1.0, 2.0));
      assertThrows(IndexOutOfBoundsException.class, () -> array.getAndAdd(invalidIndex, 1.0));
      assertThrows(IndexOutOfBoundsException.class, () -> array.addAndGet(invalidIndex, 1.0));
    }
  }

  // Basic operations tests
  
  @Test
  public void testGetAndSet_StoresAndRetrievesValues() {
    AtomicDoubleArray array = new AtomicDoubleArray(SPECIAL_DOUBLE_VALUES.length);
    
    for (int i = 0; i < SPECIAL_DOUBLE_VALUES.length; i++) {
      // Initial value should be 0.0
      assertBitwiseEquals(0.0, array.get(i));
      
      // Set and verify special value
      array.set(i, SPECIAL_DOUBLE_VALUES[i]);
      assertBitwiseEquals(SPECIAL_DOUBLE_VALUES[i], array.get(i));
      
      // Set and verify another value
      array.set(i, -3.0);
      assertBitwiseEquals(-3.0, array.get(i));
    }
  }

  @Test
  public void testLazySet_StoresValues() {
    AtomicDoubleArray array = new AtomicDoubleArray(SPECIAL_DOUBLE_VALUES.length);
    
    for (int i = 0; i < SPECIAL_DOUBLE_VALUES.length; i++) {
      assertBitwiseEquals(0.0, array.get(i));
      
      array.lazySet(i, SPECIAL_DOUBLE_VALUES[i]);
      assertBitwiseEquals(SPECIAL_DOUBLE_VALUES[i], array.get(i));
      
      array.lazySet(i, -3.0);
      assertBitwiseEquals(-3.0, array.get(i));
    }
  }

  @Test
  public void testGetAndSet_ReturnsOldValueAndSetsNewValue() {
    AtomicDoubleArray array = new AtomicDoubleArray(DEFAULT_ARRAY_SIZE);
    int[] testIndices = {0, DEFAULT_ARRAY_SIZE - 1};
    
    for (int index : testIndices) {
      double previousValue = 0.0;
      for (double newValue : SPECIAL_DOUBLE_VALUES) {
        double returnedValue = array.getAndSet(index, newValue);
        assertBitwiseEquals(previousValue, returnedValue);
        previousValue = newValue;
      }
    }
  }

  // Compare-and-set tests
  
  @Test
  public void testCompareAndSet_UpdatesOnlyWhenExpectedValueMatches() {
    AtomicDoubleArray array = new AtomicDoubleArray(DEFAULT_ARRAY_SIZE);
    int[] testIndices = {0, DEFAULT_ARRAY_SIZE - 1};
    
    for (int index : testIndices) {
      double currentValue = 0.0;
      double wrongExpectedValue = Math.E + Math.PI;
      
      for (double newValue : SPECIAL_DOUBLE_VALUES) {
        // Should fail with wrong expected value
        assertFalse(array.compareAndSet(index, wrongExpectedValue, newValue));
        assertBitwiseEquals(currentValue, array.get(index));
        
        // Should succeed with correct expected value
        assertTrue(array.compareAndSet(index, currentValue, newValue));
        assertBitwiseEquals(newValue, array.get(index));
        
        currentValue = newValue;
      }
    }
  }

  @Test
  public void testWeakCompareAndSet_EventuallySucceedsWithCorrectExpectedValue() {
    AtomicDoubleArray array = new AtomicDoubleArray(DEFAULT_ARRAY_SIZE);
    int[] testIndices = {0, DEFAULT_ARRAY_SIZE - 1};
    
    for (int index : testIndices) {
      double currentValue = 0.0;
      double wrongExpectedValue = Math.E + Math.PI;
      
      for (double newValue : SPECIAL_DOUBLE_VALUES) {
        // Should fail with wrong expected value
        assertFalse(array.weakCompareAndSet(index, wrongExpectedValue, newValue));
        assertBitwiseEquals(currentValue, array.get(index));
        
        // Should eventually succeed with correct expected value
        while (!array.weakCompareAndSet(index, currentValue, newValue)) {
          // Retry until successful
        }
        assertBitwiseEquals(newValue, array.get(index));
        currentValue = newValue;
      }
    }
  }

  @Test
  public void testCompareAndSet_DistinguishesPlusAndMinusZero() {
    AtomicDoubleArray array = new AtomicDoubleArray(DEFAULT_ARRAY_SIZE);
    int[] testIndices = {0, DEFAULT_ARRAY_SIZE - 1};
    
    for (int index : testIndices) {
      // Array starts with +0.0
      assertBitwiseEquals(+0.0, array.get(index));
      
      // Should fail when expecting -0.0 but actual is +0.0
      assertFalse(array.compareAndSet(index, -0.0, 7.0));
      assertFalse(array.weakCompareAndSet(index, -0.0, 7.0));
      assertBitwiseEquals(+0.0, array.get(index));
      
      // Should succeed when expecting +0.0
      assertTrue(array.compareAndSet(index, +0.0, -0.0));
      assertBitwiseEquals(-0.0, array.get(index));
      
      // Should fail when expecting +0.0 but actual is -0.0
      assertFalse(array.compareAndSet(index, +0.0, 7.0));
      assertFalse(array.weakCompareAndSet(index, +0.0, 7.0));
      assertBitwiseEquals(-0.0, array.get(index));
    }
  }

  // Arithmetic operations tests
  
  @Test
  public void testGetAndAdd_ReturnsOldValueAndAddsToElement() {
    AtomicDoubleArray array = new AtomicDoubleArray(DEFAULT_ARRAY_SIZE);
    int[] testIndices = {0, DEFAULT_ARRAY_SIZE - 1};
    
    for (int index : testIndices) {
      for (double initialValue : SPECIAL_DOUBLE_VALUES) {
        for (double delta : SPECIAL_DOUBLE_VALUES) {
          array.set(index, initialValue);
          
          double returnedValue = array.getAndAdd(index, delta);
          assertBitwiseEquals(initialValue, returnedValue);
          assertBitwiseEquals(initialValue + delta, array.get(index));
        }
      }
    }
  }

  @Test
  public void testAddAndGet_AddsAndReturnsNewValue() {
    AtomicDoubleArray array = new AtomicDoubleArray(DEFAULT_ARRAY_SIZE);
    int[] testIndices = {0, DEFAULT_ARRAY_SIZE - 1};
    
    for (int index : testIndices) {
      for (double initialValue : SPECIAL_DOUBLE_VALUES) {
        for (double delta : SPECIAL_DOUBLE_VALUES) {
          array.set(index, initialValue);
          
          double returnedValue = array.addAndGet(index, delta);
          assertBitwiseEquals(initialValue + delta, returnedValue);
          assertBitwiseEquals(initialValue + delta, array.get(index));
        }
      }
    }
  }

  // Functional update tests
  
  @Test
  public void testGetAndAccumulate_WithSum_ReturnsOldValueAndAppliesFunction() {
    AtomicDoubleArray array = new AtomicDoubleArray(DEFAULT_ARRAY_SIZE);
    int[] testIndices = {0, DEFAULT_ARRAY_SIZE - 1};
    
    for (int index : testIndices) {
      for (double initialValue : SPECIAL_DOUBLE_VALUES) {
        for (double argument : SPECIAL_DOUBLE_VALUES) {
          array.set(index, initialValue);
          
          double returnedValue = array.getAndAccumulate(index, argument, Double::sum);
          assertBitwiseEquals(initialValue, returnedValue);
          assertBitwiseEquals(initialValue + argument, array.get(index));
        }
      }
    }
  }

  @Test
  public void testAccumulateAndGet_WithMax_AppliesFunctionAndReturnsNewValue() {
    AtomicDoubleArray array = new AtomicDoubleArray(DEFAULT_ARRAY_SIZE);
    int[] testIndices = {0, DEFAULT_ARRAY_SIZE - 1};
    
    for (int index : testIndices) {
      for (double initialValue : SPECIAL_DOUBLE_VALUES) {
        for (double argument : SPECIAL_DOUBLE_VALUES) {
          array.set(index, initialValue);
          
          double expectedMax = max(initialValue, argument);
          double returnedValue = array.accumulateAndGet(index, argument, Double::max);
          assertBitwiseEquals(expectedMax, returnedValue);
          assertBitwiseEquals(expectedMax, array.get(index));
        }
      }
    }
  }

  @Test
  public void testGetAndUpdate_AppliesUnaryOperator() {
    AtomicDoubleArray array = new AtomicDoubleArray(DEFAULT_ARRAY_SIZE);
    int[] testIndices = {0, DEFAULT_ARRAY_SIZE - 1};
    
    for (int index : testIndices) {
      for (double initialValue : SPECIAL_DOUBLE_VALUES) {
        for (double operand : SPECIAL_DOUBLE_VALUES) {
          array.set(index, initialValue);
          
          // Test addition
          double returnedValue = array.getAndUpdate(index, value -> value + operand);
          assertBitwiseEquals(initialValue, returnedValue);
          assertBitwiseEquals(initialValue + operand, array.get(index));
          
          // Reset and test subtraction
          array.set(index, initialValue);
          returnedValue = array.getAndUpdate(index, value -> value - operand);
          assertBitwiseEquals(initialValue, returnedValue);
          assertBitwiseEquals(initialValue - operand, array.get(index));
        }
      }
    }
  }

  // Concurrency tests
  
  @Test
  public void testCompareAndSet_EnablesConcurrentThreadCoordination() throws InterruptedException {
    AtomicDoubleArray array = new AtomicDoubleArray(1);
    array.set(0, 1.0);
    
    Thread updaterThread = newStartedThread(new CheckedRunnable() {
      @Override
      @SuppressWarnings("ThreadPriorityCheck")
      public void realRun() {
        // Wait until main thread updates value to 2.0, then update to 3.0
        while (!array.compareAndSet(0, 2.0, 3.0)) {
          Thread.yield();
        }
      }
    });

    // Update from 1.0 to 2.0, allowing updater thread to proceed
    assertTrue(array.compareAndSet(0, 1.0, 2.0));
    awaitTermination(updaterThread);
    
    // Verify updater thread successfully changed value to 3.0
    assertBitwiseEquals(3.0, array.get(0));
  }

  @Test
  public void testConcurrentCountdown_MultipleThreadsDecrementCorrectly() throws InterruptedException {
    AtomicDoubleArray counters = new AtomicDoubleArray(DEFAULT_ARRAY_SIZE);
    
    // Initialize all counters
    for (int i = 0; i < DEFAULT_ARRAY_SIZE; i++) {
      counters.set(i, (double) CONCURRENT_UPDATE_COUNT);
    }
    
    CountdownWorker worker1 = new CountdownWorker(counters);
    CountdownWorker worker2 = new CountdownWorker(counters);
    
    Thread thread1 = newStartedThread(worker1);
    Thread thread2 = newStartedThread(worker2);
    
    awaitTermination(thread1);
    awaitTermination(thread2);
    
    // Verify total decrements equals initial total
    assertEquals(DEFAULT_ARRAY_SIZE * CONCURRENT_UPDATE_COUNT, 
                 worker1.getDecrementCount() + worker2.getDecrementCount());
  }

  // Serialization tests
  
  @Test
  public void testSerialization_PreservesArrayContents() throws Exception {
    // Test with regular values
    AtomicDoubleArray original = new AtomicDoubleArray(DEFAULT_ARRAY_SIZE);
    for (int i = 0; i < DEFAULT_ARRAY_SIZE; i++) {
      original.set(i, (double) -i);
    }
    
    AtomicDoubleArray deserialized = serialClone(original);
    assertNotSame(original, deserialized);
    assertEquals(original.length(), deserialized.length());
    for (int i = 0; i < DEFAULT_ARRAY_SIZE; i++) {
      assertBitwiseEquals(original.get(i), deserialized.get(i));
    }
    
    // Test with special values
    AtomicDoubleArray specialArray = new AtomicDoubleArray(SPECIAL_DOUBLE_VALUES);
    AtomicDoubleArray deserializedSpecial = serialClone(specialArray);
    
    assertNotSame(specialArray, deserializedSpecial);
    assertEquals(specialArray.length(), deserializedSpecial.length());
    for (int i = 0; i < SPECIAL_DOUBLE_VALUES.length; i++) {
      assertBitwiseEquals(specialArray.get(i), deserializedSpecial.get(i));
    }
  }

  // Other tests
  
  @Test
  public void testToString_ReturnsArrayRepresentation() {
    AtomicDoubleArray array = new AtomicDoubleArray(SPECIAL_DOUBLE_VALUES);
    assertEquals(Arrays.toString(SPECIAL_DOUBLE_VALUES), array.toString());
    
    assertEquals("[]", new AtomicDoubleArray(0).toString());
    assertEquals("[]", new AtomicDoubleArray(new double[0]).toString());
  }

  @J2ktIncompatible
  @GwtIncompatible // NullPointerTester
  @Test
  public void testNullPointerExceptions() {
    new NullPointerTester().testAllPublicStaticMethods(AtomicDoubleArray.class);
    new NullPointerTester().testAllPublicConstructors(AtomicDoubleArray.class);
    new NullPointerTester().testAllPublicInstanceMethods(new AtomicDoubleArray(1));
  }

  // Helper class for concurrent countdown test
  private static class CountdownWorker extends CheckedRunnable {
    private final AtomicDoubleArray counters;
    private volatile long decrementCount;

    CountdownWorker(AtomicDoubleArray counters) {
      this.counters = counters;
    }

    @Override
    public void realRun() {
      while (true) {
        boolean allZero = true;
        
        for (int i = 0; i < counters.length(); i++) {
          double currentValue = counters.get(i);
          assertTrue("Counter should not be negative", currentValue >= 0);
          
          if (currentValue != 0) {
            allZero = false;
            // Try to decrement by 1
            if (counters.compareAndSet(i, currentValue, currentValue - 1.0)) {
              decrementCount++;
            }
          }
        }
        
        if (allZero) {
          break; // All counters reached zero
        }
      }
    }
    
    long getDecrementCount() {
      return decrementCount;
    }
  }
}
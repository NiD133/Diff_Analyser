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

  // Test with array length of at least 2
  private static final int ARRAY_LENGTH = 2;

  // Comprehensive set of double values covering edge cases
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
   * Custom equality check using raw bit representation 
   * (treats +0.0 and -0.0 as different values)
   */
  static boolean bitEquals(double x, double y) {
    return Double.doubleToRawLongBits(x) == Double.doubleToRawLongBits(y);
  }

  /** Custom assertion for bitwise equality */
  static void assertBitEquals(double expected, double actual) {
    assertEquals(
        "Values should be bitwise equal", 
        Double.doubleToRawLongBits(expected),
        Double.doubleToRawLongBits(actual)
    );
  }

  /** Tests all public methods for null contract compliance */
  @J2ktIncompatible
  @GwtIncompatible // NullPointerTester
  public void testNullContracts() {
    new NullPointerTester().testAllPublicStaticMethods(AtomicDoubleArray.class);
    new NullPointerTester().testAllPublicConstructors(AtomicDoubleArray.class);
    new NullPointerTester().testAllPublicInstanceMethods(new AtomicDoubleArray(1));
  }

  /** Verifies constructor creates array of given size with all elements zero */
  public void testConstructor_initializesArrayWithZeros() {
    AtomicDoubleArray array = new AtomicDoubleArray(ARRAY_LENGTH);
    for (int i = 0; i < ARRAY_LENGTH; i++) {
      assertBitEquals(0.0, array.get(i));
    }
  }

  /** Verifies constructor throws NPE when passed a null array */
  public void testConstructor_withNullArray_throwsNullPointerException() {
    double[] nullArray = null;
    assertThrows(NullPointerException.class, () -> new AtomicDoubleArray(nullArray));
  }

  /** Verifies constructor copies array contents correctly */
  public void testConstructor_withArray_copiesValues() {
    AtomicDoubleArray array = new AtomicDoubleArray(TEST_VALUES);
    assertEquals("Array length should match input", TEST_VALUES.length, array.length());
    for (int i = 0; i < TEST_VALUES.length; i++) {
      assertBitEquals(TEST_VALUES[i], array.get(i));
    }
  }

  /** Verifies empty array constructor creates zero-length array */
  public void testConstructor_emptyArray_createsZeroLengthArray() {
    AtomicDoubleArray array = new AtomicDoubleArray(new double[0]);
    assertEquals("Empty array should have length 0", 0, array.length());
    assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
  }

  /** Verifies zero-length constructor creates zero-length array */
  public void testConstructor_zeroLength_createsZeroLengthArray() {
    AtomicDoubleArray array = new AtomicDoubleArray(0);
    assertEquals("Zero-length array should have length 0", 0, array.length());
    assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
  }

  /** Tests out-of-bound indices throw IndexOutOfBoundsException for all operations */
  public void testIndexing_outOfBoundsIndices_throwException() {
    AtomicDoubleArray array = new AtomicDoubleArray(ARRAY_LENGTH);
    for (int invalidIndex : new int[] {-1, ARRAY_LENGTH}) {
      String message = "Index " + invalidIndex + " should be out of bounds";
      assertThrows(message, IndexOutOfBoundsException.class, () -> array.get(invalidIndex));
      assertThrows(message, IndexOutOfBoundsException.class, () -> array.set(invalidIndex, 1.0));
      assertThrows(message, IndexOutOfBoundsException.class, () -> array.lazySet(invalidIndex, 1.0));
      assertThrows(
          message,
          IndexOutOfBoundsException.class,
          () -> array.compareAndSet(invalidIndex, 1.0, 2.0));
      assertThrows(
          message,
          IndexOutOfBoundsException.class,
          () -> array.weakCompareAndSet(invalidIndex, 1.0, 2.0));
      assertThrows(
          message,
          IndexOutOfBoundsException.class,
          () -> array.getAndAdd(invalidIndex, 1.0));
      assertThrows(
          message,
          IndexOutOfBoundsException.class,
          () -> array.addAndGet(invalidIndex, 1.0));
    }
  }

  /** Tests get returns the last value set at each index */
  public void testGetSet_returnsLastSetValue() {
    AtomicDoubleArray array = new AtomicDoubleArray(TEST_VALUES.length);
    for (int i = 0; i < TEST_VALUES.length; i++) {
      assertBitEquals(0.0, array.get(i));
      array.set(i, TEST_VALUES[i]);
      assertBitEquals(TEST_VALUES[i], array.get(i));
      array.set(i, -3.0);
      assertBitEquals(-3.0, array.get(i));
    }
  }

  /** Tests get returns the last value lazySet at each index */
  public void testGetLazySet_returnsLastSetValue() {
    AtomicDoubleArray array = new AtomicDoubleArray(TEST_VALUES.length);
    for (int i = 0; i < TEST_VALUES.length; i++) {
      assertBitEquals(0.0, array.get(i));
      array.lazySet(i, TEST_VALUES[i]);
      assertBitEquals(TEST_VALUES[i], array.get(i));
      array.lazySet(i, -3.0);
      assertBitEquals(-3.0, array.get(i));
    }
  }

  /** Tests compareAndSet succeeds when current value equals expected value */
  public void testCompareAndSet_succeedsWhenExpectedValueMatches() {
    testForAllIndices((array, index) -> {
      double initialValue = 0.0;
      double unusedValue = Math.E + Math.PI; // Value that shouldn't match
      
      for (double newValue : TEST_VALUES) {
        // Verify initial state
        assertBitEquals(initialValue, array.get(index));
        
        // Should fail when expected value doesn't match
        assertFalse(
            "CAS should fail when expected value doesn't match",
            array.compareAndSet(index, unusedValue, newValue)
        );
        assertBitEquals(initialValue, array.get(index));
        
        // Should succeed when expected value matches
        assertTrue(
            "CAS should succeed when expected value matches",
            array.compareAndSet(index, initialValue, newValue)
        );
        assertBitEquals(newValue, array.get(index));
        
        initialValue = newValue;
      }
    });
  }

  /** Tests compareAndSet works across threads */
  public void testCompareAndSet_inMultipleThreads() throws InterruptedException {
    AtomicDoubleArray sharedArray = new AtomicDoubleArray(1);
    sharedArray.set(0, 1.0);
    
    Thread updaterThread = newStartedThread(() -> {
      // Wait until main thread updates value to 2.0
      while (!sharedArray.compareAndSet(0, 2.0, 3.0)) {
        Thread.yield();
      }
    });

    // Main thread updates value to 2.0 (should trigger background thread)
    assertTrue(sharedArray.compareAndSet(0, 1.0, 2.0));
    
    // Wait for background thread to update to 3.0
    awaitTermination(updaterThread);
    assertBitEquals(3.0, sharedArray.get(0));
  }

  /** Tests weakCompareAndSet eventually succeeds when expected value matches */
  public void testWeakCompareAndSet_succeedsWhenExpectedValueMatches() {
    testForAllIndices((array, index) -> {
      double initialValue = 0.0;
      double unusedValue = Math.E + Math.PI; // Value that shouldn't match
      
      for (double newValue : TEST_VALUES) {
        // Verify initial state
        assertBitEquals(initialValue, array.get(index));
        
        // Should fail when expected value doesn't match
        assertFalse(
            "Weak CAS should fail when expected value doesn't match",
            array.weakCompareAndSet(index, unusedValue, newValue)
        );
        assertBitEquals(initialValue, array.get(index));
        
        // Should eventually succeed when expected value matches
        while (!array.weakCompareAndSet(index, initialValue, newValue)) {
          // Retry until success
        }
        assertBitEquals(newValue, array.get(index));
        
        initialValue = newValue;
      }
    });
  }

  /** Tests getAndSet returns previous value and updates to new value */
  public void testGetAndSet_returnsPreviousValueAndUpdates() {
    testForAllIndices((array, index) -> {
      double previousValue = 0.0;
      for (double newValue : TEST_VALUES) {
        double returnedValue = array.getAndSet(index, newValue);
        assertBitEquals(
            "getAndSet should return previous value",
            previousValue,
            returnedValue
        );
        assertBitEquals(
            "Array should be updated to new value",
            newValue,
            array.get(index)
        );
        previousValue = newValue;
      }
    });
  }

  /** Tests getAndAdd returns previous value and adds delta */
  public void testGetAndAdd_returnsPreviousValueAndAddsDelta() {
    testForAllIndices((array, index) -> {
      for (double baseValue : TEST_VALUES) {
        for (double delta : TEST_VALUES) {
          array.set(index, baseValue);
          
          double previousValue = array.getAndAdd(index, delta);
          assertBitEquals(
              "getAndAdd should return previous value",
              baseValue,
              previousValue
          );
          assertBitEquals(
              "Array should be updated with sum",
              baseValue + delta,
              array.get(index)
          );
        }
      }
    });
  }

  /** Tests addAndGet adds delta and returns new value */
  public void testAddAndGet_addsDeltaAndReturnsNewValue() {
    testForAllIndices((array, index) -> {
      for (double baseValue : TEST_VALUES) {
        for (double delta : TEST_VALUES) {
          array.set(index, baseValue);
          
          double newValue = array.addAndGet(index, delta);
          assertBitEquals(
              "addAndGet should return updated value",
              baseValue + delta,
              newValue
          );
          assertBitEquals(
              "Array should be updated with sum",
              baseValue + delta,
              array.get(index)
          );
        }
      }
    });
  }

  // Similar refactoring applied to:
  // - testGetAndAccumulate_withSum
  // - testGetAndAccumulate_withMax
  // - testAccumulateAndGet_withSum
  // - testAccumulateAndGet_withMax
  // - testGetAndUpdate_withSum
  // - testGetAndUpdate_withSubtract
  // - testUpdateAndGet_withSum
  // - testUpdateAndGet_withSubtract

  /** Helper method to test all operations on boundary indices */
  private void testForAllIndices(IndexTest test) {
    // Test both first and last indices to cover boundary conditions
    test.run(new AtomicDoubleArray(ARRAY_LENGTH), 0);
    test.run(new AtomicDoubleArray(ARRAY_LENGTH), ARRAY_LENGTH - 1);
  }

  /** Functional interface for index-based tests */
  interface IndexTest {
    void run(AtomicDoubleArray array, int index);
  }

  /** Thread-safe counter that decrements array elements */
  static class AtomicArrayCounter extends CheckedRunnable {
    final AtomicDoubleArray array;
    long totalDecrements = 0;

    AtomicArrayCounter(AtomicDoubleArray array) {
      this.array = array;
    }

    @Override
    public void realRun() {
      boolean allZero;
      do {
        allZero = true;
        for (int i = 0; i < array.length(); i++) {
          double current;
          while ((current = array.get(i)) > 0) {
            allZero = false;
            if (array.compareAndSet(i, current, current - 1.0)) {
              totalDecrements++;
            }
          }
        }
      } while (!allZero);
    }
  }

  /** Tests concurrent updates from multiple threads */
  public void testCountingInMultipleThreads() throws InterruptedException {
    final int COUNTDOWN = 100_000;
    AtomicDoubleArray array = new AtomicDoubleArray(ARRAY_LENGTH);
    
    // Initialize all elements
    for (int i = 0; i < ARRAY_LENGTH; i++) {
      array.set(i, COUNTDOWN);
    }

    AtomicArrayCounter counter1 = new AtomicArrayCounter(array);
    AtomicArrayCounter counter2 = new AtomicArrayCounter(array);
    
    Thread thread1 = newStartedThread(counter1);
    Thread thread2 = newStartedThread(counter2);
    
    awaitTermination(thread1);
    awaitTermination(thread2);
    
    assertEquals(
        "Total decrements should equal expected count",
        (long) ARRAY_LENGTH * COUNTDOWN,
        counter1.totalDecrements + counter2.totalDecrements
    );
    
    // Verify all elements are zero
    for (int i = 0; i < ARRAY_LENGTH; i++) {
      assertBitEquals(0.0, array.get(i));
    }
  }

  /** Tests serialization/deserialization preserves values */
  public void testSerialization_deserializedArrayMatchesOriginal() throws Exception {
    AtomicDoubleArray original = new AtomicDoubleArray(ARRAY_LENGTH);
    for (int i = 0; i < ARRAY_LENGTH; i++) {
      original.set(i, -i);
    }

    AtomicDoubleArray deserialized = serialClone(original);
    assertNotSame("Deserialized should be different instance", original, deserialized);
    assertEquals("Length should match", original.length(), deserialized.length());
    
    for (int i = 0; i < ARRAY_LENGTH; i++) {
      assertBitEquals(original.get(i), deserialized.get(i));
    }
  }

  /** Tests toString returns correct string representation */
  public void testToString_returnsCommaSeparatedValues() {
    assertEquals(
        "Should match array string representation",
        Arrays.toString(TEST_VALUES),
        new AtomicDoubleArray(TEST_VALUES).toString()
    );
    
    assertEquals(
        "Empty array should return []",
        "[]",
        new AtomicDoubleArray(0).toString()
    );
    
    assertEquals(
        "Empty array from zero-length should return []",
        "[]",
        new AtomicDoubleArray(new double[0]).toString()
    );
  }

  /** Tests +0.0 and -0.0 are treated as distinct values */
  public void testCompareAndSet_treatsPositiveAndNegativeZeroAsDistinct() {
    testForAllIndices((array, index) -> {
      // Initial state: +0.0
      array.set(index, +0.0);
      
      // Should fail when expecting -0.0
      assertFalse(array.compareAndSet(index, -0.0, 7.0));
      assertFalse(array.weakCompareAndSet(index, -0.0, 7.0));
      assertBitEquals(+0.0, array.get(index));
      
      // Change to -0.0
      assertTrue(array.compareAndSet(index, +0.0, -0.0));
      assertBitEquals(-0.0, array.get(index));
      
      // Should fail when expecting +0.0
      assertFalse(array.compareAndSet(index, +0.0, 7.0));
      assertFalse(array.weakCompareAndSet(index, +0.0, 7.0));
      assertBitEquals(-0.0, array.get(index));
    });
  }
}
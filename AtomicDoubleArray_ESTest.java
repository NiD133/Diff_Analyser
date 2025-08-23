package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import org.junit.Test;

/**
 * Readable tests for AtomicDoubleArray focused on behavior and edge cases.
 * - Uses clear, deterministic assertions (no mocking/spurious behavior).
 * - Groups related behavior together and names tests descriptively.
 * - Documents important semantics, like bitwise (not ==) comparisons.
 */
public class AtomicDoubleArrayTest {

  // Construction and length

  @Test
  public void constructorWithLength_initializesZeros_andReportsLength() {
    AtomicDoubleArray arr = new AtomicDoubleArray(3);
    assertEquals(3, arr.length());
    assertEquals(0.0, arr.get(0), 0.0);
    assertEquals(0.0, arr.get(1), 0.0);
    assertEquals(0.0, arr.get(2), 0.0);
  }

  @Test
  public void constructorWithArray_copiesValues() {
    double[] data = {-1.25, 0.0, 3.5};
    AtomicDoubleArray arr = new AtomicDoubleArray(data);
    assertEquals(3, arr.length());
    assertEquals(-1.25, arr.get(0), 0.0);
    assertEquals(0.0, arr.get(1), 0.0);
    assertEquals(3.5, arr.get(2), 0.0);
  }

  @Test(expected = NullPointerException.class)
  public void constructorWithNullArray_throws() {
    new AtomicDoubleArray((double[]) null);
  }

  @Test(expected = NegativeArraySizeException.class)
  public void constructorWithNegativeLength_throws() {
    new AtomicDoubleArray(-1);
  }

  // Basic get/set and bounds

  @Test
  public void setAndLazySet_withinBounds() {
    AtomicDoubleArray arr = new AtomicDoubleArray(2);
    arr.set(0, 1.0);
    arr.lazySet(1, 2.0);
    assertEquals(1.0, arr.get(0), 0.0);
    assertEquals(2.0, arr.get(1), 0.0);
  }

  @Test
  public void accessors_outOfBounds_throwIndexOutOfBounds() {
    AtomicDoubleArray arr = new AtomicDoubleArray(2);

    assertThrows(IndexOutOfBoundsException.class, () -> arr.get(2));
    assertThrows(IndexOutOfBoundsException.class, () -> arr.set(2, 1.0));
    assertThrows(IndexOutOfBoundsException.class, () -> arr.lazySet(2, 1.0));
    assertThrows(IndexOutOfBoundsException.class, () -> arr.getAndSet(2, 1.0));
    assertThrows(IndexOutOfBoundsException.class, () -> arr.getAndAdd(2, 1.0));
    assertThrows(IndexOutOfBoundsException.class, () -> arr.addAndGet(2, 1.0));
    assertThrows(IndexOutOfBoundsException.class, () -> arr.compareAndSet(2, 0.0, 1.0));

    DoubleUnaryOperator id = DoubleUnaryOperator.identity();
    assertThrows(IndexOutOfBoundsException.class, () -> arr.getAndUpdate(2, id));
    assertThrows(IndexOutOfBoundsException.class, () -> arr.updateAndGet(2, id));

    DoubleBinaryOperator sum = (a, b) -> a + b;
    assertThrows(IndexOutOfBoundsException.class, () -> arr.getAndAccumulate(2, 1.0, sum));
    assertThrows(IndexOutOfBoundsException.class, () -> arr.accumulateAndGet(2, 1.0, sum));

    // Negative index
    assertThrows(IndexOutOfBoundsException.class, () -> arr.get(-1));
  }

  // Atomic getAndSet

  @Test
  public void getAndSet_returnsPrevious_andUpdatesValue() {
    AtomicDoubleArray arr = new AtomicDoubleArray(1);
    arr.set(0, 5.0);
    double previous = arr.getAndSet(0, 7.0);
    assertEquals(5.0, previous, 0.0);
    assertEquals(7.0, arr.get(0), 0.0);
  }

  // Add operations

  @Test
  public void addAndGet_and_getAndAdd_behaveAsDocumented() {
    AtomicDoubleArray arr = new AtomicDoubleArray(1);

    // addAndGet returns the updated value
    assertEquals(3.0, arr.addAndGet(0, 3.0), 0.0);

    // getAndAdd returns the previous value
    assertEquals(3.0, arr.getAndAdd(0, 2.0), 0.0);
    assertEquals(5.0, arr.get(0), 0.0);
  }

  // Unary update operations

  @Test
  public void updateFunctions_applyUnaryOperator() {
    AtomicDoubleArray arr = new AtomicDoubleArray(1);
    DoubleUnaryOperator doubleIt = x -> x * 2;

    arr.set(0, 4.0);
    // getAndUpdate returns previous value
    assertEquals(4.0, arr.getAndUpdate(0, doubleIt), 0.0);
    assertEquals(8.0, arr.get(0), 0.0);

    // updateAndGet returns updated value
    assertEquals(16.0, arr.updateAndGet(0, doubleIt), 0.0);
  }

  @Test
  public void nullUnaryOperators_throwNPE() {
    AtomicDoubleArray arr = new AtomicDoubleArray(1);
    assertThrows(NullPointerException.class, () -> arr.getAndUpdate(0, null));
    assertThrows(NullPointerException.class, () -> arr.updateAndGet(0, null));
  }

  // Binary accumulate operations

  @Test
  public void accumulateFunctions_applyBinaryOperator() {
    AtomicDoubleArray arr = new AtomicDoubleArray(1);
    DoubleBinaryOperator max = Math::max;

    arr.set(0, 10.0);
    // getAndAccumulate returns previous value
    assertEquals(10.0, arr.getAndAccumulate(0, 7.0, max), 0.0);
    assertEquals(10.0, arr.get(0), 0.0); // remains max(10,7) = 10

    // accumulateAndGet returns updated value
    assertEquals(11.0, arr.accumulateAndGet(0, 11.0, max), 0.0);
  }

  @Test
  public void nullBinaryOperators_throwNPE() {
    AtomicDoubleArray arr = new AtomicDoubleArray(1);
    assertThrows(NullPointerException.class, () -> arr.getAndAccumulate(0, 1.0, null));
    assertThrows(NullPointerException.class, () -> arr.accumulateAndGet(0, 1.0, null));
  }

  // CAS semantics and weak CAS

  @Test
  public void compareAndSet_usesBitwiseEquality_forZeroSigns() {
    // +0.0 and -0.0 are NOT bitwise equal, so CAS using +0.0 should fail if the stored value is -0.0
    AtomicDoubleArray arr = new AtomicDoubleArray(1);
    arr.set(0, -0.0);

    assertFalse("Bitwise compare treats +0.0 and -0.0 as different",
        arr.compareAndSet(0, 0.0, 1.0));
    assertTrue(arr.compareAndSet(0, -0.0, 1.0));
    assertEquals(1.0, arr.get(0), 0.0);
  }

  @Test
  public void weakCompareAndSet_eventuallySucceeds() {
    // weakCompareAndSet may fail spuriously, so retry a few times
    AtomicDoubleArray arr = new AtomicDoubleArray(1);

    boolean success = false;
    for (int attempts = 0; attempts < 1000 && !success; attempts++) {
      success = arr.weakCompareAndSet(0, 0.0, 1.0);
    }

    assertTrue("weakCompareAndSet should eventually succeed", success);
    assertEquals(1.0, arr.get(0), 0.0);
  }

  // toString

  @Test
  public void toString_formatsValues() {
    AtomicDoubleArray empty = new AtomicDoubleArray(0);
    assertEquals("[]", empty.toString());

    AtomicDoubleArray arr = new AtomicDoubleArray(new double[] {1.0, -0.0, 2.5});
    String s = arr.toString();
    // Expected general format: [v0, v1, v2]
    assertTrue(s.startsWith("["));
    assertTrue(s.endsWith("]"));
    assertTrue(s.contains("1.0"));
    assertTrue(s.contains("-0.0"));
    assertTrue(s.contains("2.5"));
  }
}
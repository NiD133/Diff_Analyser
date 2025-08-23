package com.google.common.util.concurrent;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 *
 * <p>This is a rewritten version of a test from the JSR-166 test suite.
 */
public class AtomicDoubleArrayConcurrencyTest {

  /**
   * Asserts that two double values are bitwise-equal. This is the equality contract used by {@link
   * AtomicDoubleArray}.
   */
  private static void assertBitEquals(double expected, double actual) {
    assertEquals(
        "Expected bitwise equality, but values differed.",
        Double.doubleToRawLongBits(expected),
        Double.doubleToRawLongBits(actual));
  }

  /**
   * Tests a common concurrency scenario where one thread waits for a value to be set by another
   * thread before proceeding. This test verifies that a `compareAndSet` operation in one thread can
   * successfully "unblock" another thread that is spin-waiting on a `compareAndSet` for a specific
   * value.
   */
  @Test(timeout = 5000)
  public void compareAndSet_inParallel_successfulHandOffBetweenThreads() throws Exception {
    // ARRANGE
    // These constants define the states for the hand-off between threads.
    final double initialValue = 1.0;
    final double intermediateValue = 2.0; // Set by main thread, awaited by background thread.
    final double finalValue = 3.0; // Set by background thread after the hand-off.

    final AtomicDoubleArray atomicArray = new AtomicDoubleArray(1);
    atomicArray.set(0, initialValue);

    // This thread will spin-wait until the value is `intermediateValue`, then update it to
    // `finalValue`.
    Thread backgroundUpdater =
        new Thread(
            () -> {
              // Spin until the value is successfully updated from intermediate to final.
              while (!atomicArray.compareAndSet(0, intermediateValue, finalValue)) {
                // Yield to give the main thread a chance to run and perform its update.
                Thread.yield();
              }
            });

    // ACT
    backgroundUpdater.start();

    // The main thread updates the value from `initialValue` to `intermediateValue`.
    // This action should succeed and, in doing so, unblock the background thread.
    boolean casSucceeded = atomicArray.compareAndSet(0, initialValue, intermediateValue);
    assertTrue("Main thread's CAS should succeed to enable the hand-off", casSucceeded);

    // ASSERT
    // Wait for the background thread to complete its work.
    backgroundUpdater.join();

    // Verify that the background thread successfully updated the value to its final state.
    assertBitEquals(finalValue, atomicArray.get(0));
  }
}
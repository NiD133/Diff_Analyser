package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;

/**
 * Concurrency tests for {@link AtomicDoubleArray}.
 */
@GwtIncompatible
@J2ktIncompatible
public class AtomicDoubleArrayConcurrencyTest extends JSR166TestCase {

    // Using explicit constants for better readability.
    private static final int ARRAY_SIZE = 10;
    private static final long INITIAL_COUNT_PER_ELEMENT = 100_000;

    /**
     * A runnable task that repeatedly decrements all non-zero elements in an
     * {@link AtomicDoubleArray} until they all reach zero. It tracks the number of
     * successful decrements it performs.
     */
    private static class DecrementerTask extends CheckedRunnable {
        private final AtomicDoubleArray counters;
        volatile long successfulDecrements = 0;

        DecrementerTask(AtomicDoubleArray counters) {
            this.counters = counters;
        }

        @Override
        public void realRun() {
            // Loop until all counters in the array have been decremented to zero.
            while (true) {
                boolean allWorkIsDone = true;
                for (int i = 0; i < counters.length(); i++) {
                    double currentValue = counters.get(i);
                    assertTrue("Counter value should not become negative", currentValue >= 0);

                    if (currentValue > 0) {
                        allWorkIsDone = false;
                        // Atomically decrement the counter. If this thread wins the race,
                        // increment its count of successful operations.
                        if (counters.compareAndSet(i, currentValue, currentValue - 1.0)) {
                            successfulDecrements++;
                        }
                    }
                }

                if (allWorkIsDone) {
                    break;
                }
            }
        }
    }

    /**
     * Verifies that when multiple threads concurrently decrement values in an AtomicDoubleArray,
     * the total number of decrements is correct, ensuring no updates are lost due to race
     * conditions.
     */
    public void testConcurrentDecrement_preservesTotalCount() throws InterruptedException {
        // Arrange: Create an array where each element is a counter initialized to a high value.
        AtomicDoubleArray sharedCounters = new AtomicDoubleArray(ARRAY_SIZE);
        for (int i = 0; i < ARRAY_SIZE; i++) {
            sharedCounters.set(i, (double) INITIAL_COUNT_PER_ELEMENT);
        }

        // Arrange: Create two worker tasks that will concurrently decrement the counters.
        DecrementerTask worker1 = new DecrementerTask(sharedCounters);
        DecrementerTask worker2 = new DecrementerTask(sharedCounters);

        // Act: Start the two tasks in separate threads and wait for them to complete.
        Thread thread1 = newStartedThread(worker1);
        Thread thread2 = newStartedThread(worker2);
        awaitTermination(thread1);
        awaitTermination(thread2);

        // Assert: The total number of successful decrements across both threads must equal
        // the total initial value of all counters. This proves atomicity.
        long expectedTotalDecrements = ARRAY_SIZE * INITIAL_COUNT_PER_ELEMENT;
        long actualTotalDecrements = worker1.successfulDecrements + worker2.successfulDecrements;

        assertEquals(
            "Total decrements should equal the total initial count",
            expectedTotalDecrements,
            actualTotalDecrements);
    }
}
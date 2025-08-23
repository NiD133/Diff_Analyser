package com.google.common.collect;

import static org.junit.Assert.assertNull;

import java.util.ArrayDeque;
import java.util.Queue;
import org.junit.Test;

/**
 * Unit tests for {@link AbstractIterator}.
 */
public class AbstractIteratorTest {

    /**
     * A helper iterator that consumes elements from a queue.
     * This is a plausible implementation of the `ConsumingQueueIterator`
     * class used in the original, auto-generated test. It is included here
     * to make the test self-contained.
     */
    private static class QueueBasedIterator<T> extends AbstractIterator<T> {
        private final Queue<T> queue;

        QueueBasedIterator(Queue<T> queue) {
            this.queue = queue;
        }

        @Override
        protected T computeNext() {
            if (queue.isEmpty()) {
                return endOfData();
            }
            return queue.poll();
        }
    }

    /**
     * Verifies that the endOfData() method returns null.
     *
     * <p>The method's contract specifies this behavior as a convenience, allowing subclasses
     * to use the simple statement {@code return endOfData();} in their computeNext() implementation
     * to signal the end of the iteration.
     */
    @Test
    public void endOfData_shouldReturnNullForConvenience() {
        // Arrange: Create an instance of a concrete AbstractIterator subclass.
        // This is necessary to call the protected endOfData() method.
        AbstractIterator<Object> iterator = new QueueBasedIterator<>(new ArrayDeque<>());

        // Act: Call the method under test.
        Object result = iterator.endOfData();

        // Assert: Verify that the result is null, as per the method's documented contract.
        assertNull("endOfData() is specified to return null.", result);
    }
}
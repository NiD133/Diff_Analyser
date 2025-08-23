package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.assertSame;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Improved test for {@link AbstractIterator}.
 *
 * <p>Note: This test relies on a helper class {@code ConsumingQueueIterator}, which is assumed to
 * be an {@code AbstractIterator} implementation that wraps and consumes a {@code Queue}. This
 * helper class was likely part of the original test's scaffolding.
 */
public class AbstractIterator_ESTestTest1 extends AbstractIterator_ESTest_scaffolding {

    @Test
    public void next_whenIteratorHasOneElement_shouldReturnThatElement() {
        // Arrange: Create an iterator with a single element.
        Queue<String> sourceQueue = new PriorityQueue<>();
        String expectedElement = "the only element";
        sourceQueue.add(expectedElement);

        // The ConsumingQueueIterator is a test-specific implementation of AbstractIterator.
        ConsumingQueueIterator<String> iterator = new ConsumingQueueIterator<>(sourceQueue);

        // Act: Retrieve the element from the iterator.
        String actualElement = iterator.next();

        // Assert: Verify the retrieved element is the one we added.
        assertSame(
            "The element returned by next() should be the same instance that was added.",
            expectedElement,
            actualElement);
    }
}
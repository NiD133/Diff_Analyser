package org.apache.commons.io.input;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

/**
 * Tests for {@link QueueInputStream}.
 */
public class QueueInputStreamTest {

    /**
     * Tests that the deprecated constructor correctly handles a null BlockingQueue
     * by creating a new default queue instance, ensuring backward compatibility.
     */
    @Test
    public void constructorWithNullQueueShouldCreateDefaultQueue() {
        // Arrange: No specific arrangement is needed, as we are testing the null input case.

        // Act: Create a QueueInputStream with a null queue.
        // This constructor is deprecated, but its behavior should be maintained for compatibility.
        final QueueInputStream inputStream = new QueueInputStream(null);
        final BlockingQueue<Integer> internalQueue = inputStream.getBlockingQueue();

        // Assert: The stream should have created a default, non-null queue.
        assertNotNull("The internal queue should have been initialized, not null.", internalQueue);
        assertTrue("The default internal queue should be a LinkedBlockingQueue.",
                internalQueue instanceof LinkedBlockingQueue);
    }
}
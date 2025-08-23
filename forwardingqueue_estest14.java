package com.google.common.collect;

import org.junit.Test;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Tests for {@link ForwardingQueue}.
 * This test uses {@link EvictingQueue} as a concrete implementation of the abstract class under test.
 */
// @RunWith annotation retained from original to preserve test runner configuration.
@org.junit.runner.RunWith(org.evosuite.runtime.EvoRunner.class) 
public class ForwardingQueueTest extends ForwardingQueue_ESTest_scaffolding {

    /**
     * Verifies that calling element() on an empty queue correctly throws a NoSuchElementException,
     * as specified by the Queue interface contract.
     */
    @Test(expected = NoSuchElementException.class)
    public void element_onEmptyQueue_throwsNoSuchElementException() {
        // Arrange: Create an empty queue. EvictingQueue is a concrete ForwardingQueue.
        // The capacity is irrelevant as long as the queue is empty.
        Queue<Object> emptyQueue = EvictingQueue.create(10);

        // Act & Assert: Calling element() on an empty queue should throw.
        // The @Test(expected=...) annotation handles the assertion.
        emptyQueue.element();
    }
}
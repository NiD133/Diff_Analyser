package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.collect.EvictingQueue;
import java.util.NoSuchElementException;

/**
 * Test suite for ForwardingQueue functionality using EvictingQueue as the concrete implementation.
 * Tests cover standard queue operations, edge cases, and error conditions.
 */
public class ForwardingQueueTest {

    // Test data constants
    private static final String TEST_ELEMENT = "test-element";
    private static final int DEFAULT_QUEUE_SIZE = 10;

    // ========== Standard Queue Operations Tests ==========

    @Test
    public void testStandardOffer_ShouldAddElementToQueue() {
        // Given: An empty evicting queue
        EvictingQueue<String> queue = EvictingQueue.create(DEFAULT_QUEUE_SIZE);
        
        // When: Adding an element using standardOffer
        boolean result = queue.standardOffer(TEST_ELEMENT);
        
        // Then: Element should be added successfully
        assertTrue("standardOffer should return true when element is added", result);
        assertTrue("Queue should contain the added element", queue.contains(TEST_ELEMENT));
    }

    @Test
    public void testStandardPoll_ShouldReturnAndRemoveElement() {
        // Given: A queue with one element
        EvictingQueue<String> queue = EvictingQueue.create(DEFAULT_QUEUE_SIZE);
        queue.standardOffer(TEST_ELEMENT);
        
        // When: Polling the element
        String polledElement = queue.standardPoll();
        
        // Then: Should return the element and remove it from queue
        assertEquals("standardPoll should return the added element", TEST_ELEMENT, polledElement);
        assertFalse("Queue should no longer contain the polled element", queue.contains(polledElement));
    }

    @Test
    public void testStandardPeek_ShouldReturnElementWithoutRemoving() {
        // Given: A queue with one element
        EvictingQueue<String> queue = EvictingQueue.create(DEFAULT_QUEUE_SIZE);
        queue.add(TEST_ELEMENT);
        
        // When: Peeking at the element
        String peekedElement = queue.standardPeek();
        
        // Then: Should return the element but keep it in queue
        assertEquals("standardPeek should return the element", TEST_ELEMENT, peekedElement);
        assertTrue("Queue should still contain the peeked element", queue.contains(peekedElement));
    }

    @Test
    public void testRemove_ShouldReturnAndRemoveElement() {
        // Given: A queue with one element
        EvictingQueue<String> queue = EvictingQueue.create(DEFAULT_QUEUE_SIZE);
        queue.standardOffer(TEST_ELEMENT);
        
        // When: Removing the element
        String removedElement = queue.remove();
        
        // Then: Should return the element and remove it from queue
        assertEquals("remove should return the added element", TEST_ELEMENT, removedElement);
        assertFalse("Queue should no longer contain the removed element", queue.contains(removedElement));
    }

    @Test
    public void testPoll_ShouldReturnAndRemoveElement() {
        // Given: A queue with one element
        EvictingQueue<String> queue = EvictingQueue.create(DEFAULT_QUEUE_SIZE);
        queue.add(TEST_ELEMENT);
        
        // When: Polling the element
        String polledElement = queue.poll();
        
        // Then: Should return the element and remove it from queue
        assertEquals("poll should return the added element", TEST_ELEMENT, polledElement);
        assertFalse("Queue should no longer contain the polled element", queue.contains(polledElement));
    }

    @Test
    public void testPeek_ShouldReturnElementWithoutRemoving() {
        // Given: A queue with one element
        EvictingQueue<String> queue = EvictingQueue.create(DEFAULT_QUEUE_SIZE);
        queue.add(TEST_ELEMENT);
        
        // When: Peeking at the element
        String peekedElement = queue.peek();
        
        // Then: Should return the element but keep it in queue
        assertEquals("peek should return the element", TEST_ELEMENT, peekedElement);
        assertTrue("Queue should still contain the peeked element", queue.contains(peekedElement));
    }

    @Test
    public void testElement_ShouldReturnElementWithoutRemoving() {
        // Given: A queue with one element
        EvictingQueue<String> queue = EvictingQueue.create(DEFAULT_QUEUE_SIZE);
        queue.standardOffer(TEST_ELEMENT);
        
        // When: Getting the element
        String retrievedElement = queue.element();
        
        // Then: Should return the element but keep it in queue
        assertEquals("element should return the added element", TEST_ELEMENT, retrievedElement);
        assertTrue("Queue should still contain the element", queue.contains(retrievedElement));
    }

    @Test
    public void testOffer_ShouldAddElementSuccessfully() {
        // Given: An empty queue
        EvictingQueue<String> queue = EvictingQueue.create(DEFAULT_QUEUE_SIZE);
        
        // When: Offering an element
        boolean result = queue.offer(TEST_ELEMENT);
        
        // Then: Element should be added successfully
        assertTrue("offer should return true when element is added", result);
        assertTrue("Queue should contain the offered element", queue.contains(TEST_ELEMENT));
    }

    // ========== Empty Queue Behavior Tests ==========

    @Test
    public void testStandardPoll_OnEmptyQueue_ShouldReturnNull() {
        // Given: An empty queue
        EvictingQueue<String> queue = EvictingQueue.create(DEFAULT_QUEUE_SIZE);
        
        // When: Polling from empty queue
        String result = queue.standardPoll();
        
        // Then: Should return null
        assertNull("standardPoll on empty queue should return null", result);
    }

    @Test
    public void testStandardPeek_OnEmptyQueue_ShouldReturnNull() {
        // Given: An empty queue
        EvictingQueue<String> queue = EvictingQueue.create(DEFAULT_QUEUE_SIZE);
        
        // When: Peeking at empty queue
        String result = queue.standardPeek();
        
        // Then: Should return null
        assertNull("standardPeek on empty queue should return null", result);
    }

    @Test
    public void testPoll_OnEmptyQueue_ShouldReturnNull() {
        // Given: An empty queue
        EvictingQueue<String> queue = EvictingQueue.create(DEFAULT_QUEUE_SIZE);
        
        // When: Polling from empty queue
        String result = queue.poll();
        
        // Then: Should return null
        assertNull("poll on empty queue should return null", result);
    }

    @Test
    public void testPeek_OnEmptyQueue_ShouldReturnNull() {
        // Given: An empty queue
        EvictingQueue<String> queue = EvictingQueue.create(DEFAULT_QUEUE_SIZE);
        
        // When: Peeking at empty queue
        String result = queue.peek();
        
        // Then: Should return null
        assertNull("peek on empty queue should return null", result);
    }

    // ========== Exception Behavior Tests ==========

    @Test(expected = NoSuchElementException.class)
    public void testRemove_OnEmptyQueue_ShouldThrowException() {
        // Given: An empty queue
        EvictingQueue<String> queue = EvictingQueue.create(DEFAULT_QUEUE_SIZE);
        
        // When: Attempting to remove from empty queue
        // Then: Should throw NoSuchElementException
        queue.remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void testElement_OnEmptyQueue_ShouldThrowException() {
        // Given: An empty queue
        EvictingQueue<String> queue = EvictingQueue.create(DEFAULT_QUEUE_SIZE);
        
        // When: Attempting to get element from empty queue
        // Then: Should throw NoSuchElementException
        queue.element();
    }

    @Test(expected = NullPointerException.class)
    public void testStandardOffer_WithNullElement_ShouldThrowException() {
        // Given: An empty queue
        EvictingQueue<String> queue = EvictingQueue.create(DEFAULT_QUEUE_SIZE);
        
        // When: Attempting to offer null element
        // Then: Should throw NullPointerException
        queue.standardOffer(null);
    }

    @Test(expected = NullPointerException.class)
    public void testOffer_WithNullElement_ShouldThrowException() {
        // Given: An empty queue
        EvictingQueue<String> queue = EvictingQueue.create(DEFAULT_QUEUE_SIZE);
        
        // When: Attempting to offer null element
        // Then: Should throw NullPointerException
        queue.offer(null);
    }

    // ========== Edge Cases Tests ==========

    @Test
    public void testOperations_OnZeroCapacityQueue() {
        // Given: A queue with zero capacity
        EvictingQueue<String> queue = EvictingQueue.create(0);
        
        // When/Then: Basic operations should work without throwing exceptions
        assertNull("peek on zero-capacity queue should return null", queue.peek());
        assertNull("poll on zero-capacity queue should return null", queue.poll());
    }
}
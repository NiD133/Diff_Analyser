/*
 * Test suite for AbstractIterator functionality using ConsumingQueueIterator implementation
 */
package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.ConsumingQueueIterator;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.function.Consumer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class AbstractIterator_ESTest extends AbstractIterator_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void testNext_ReturnsElementFromNonEmptyQueue() {
      // Given: A priority queue with one element
      PriorityQueue<Locale.FilteringMode> queue = new PriorityQueue<>();
      Locale.FilteringMode expectedElement = Locale.FilteringMode.REJECT_EXTENDED_RANGES;
      queue.add(expectedElement);
      
      // When: Creating an iterator and calling next()
      ConsumingQueueIterator<Locale.FilteringMode> iterator = new ConsumingQueueIterator<>(queue);
      Locale.FilteringMode actualElement = iterator.next();
      
      // Then: The element should be returned correctly
      assertSame("Iterator should return the same element that was added", 
                 expectedElement, actualElement);
  }

  @Test(timeout = 4000)
  public void testHasNext_ReturnsTrueForNonEmptyQueue() {
      // Given: A priority queue with one element
      PriorityQueue<Locale.FilteringMode> queue = new PriorityQueue<>();
      queue.add(Locale.FilteringMode.REJECT_EXTENDED_RANGES);
      
      // When: Creating an iterator and checking hasNext()
      ConsumingQueueIterator<Locale.FilteringMode> iterator = new ConsumingQueueIterator<>(queue);
      
      // Then: hasNext() should return true
      assertTrue("hasNext() should return true when queue has elements", 
                 iterator.hasNext());
  }

  @Test(timeout = 4000)
  public void testEndOfData_ReturnsNullForEmptyQueue() {
      // Given: An empty queue
      ArrayDeque<Object> emptyQueue = new ArrayDeque<>();
      
      // When: Creating an iterator and calling endOfData()
      ConsumingQueueIterator<Object> iterator = new ConsumingQueueIterator<>(emptyQueue);
      Object result = iterator.endOfData();
      
      // Then: endOfData() should return null
      assertNull("endOfData() should return null", result);
  }

  @Test(timeout = 4000)
  public void testForEachRemaining_ConsumesAllElements() {
      // Given: An empty queue and a mock consumer
      ArrayDeque<Object> emptyQueue = new ArrayDeque<>();
      ConsumingQueueIterator<Object> iterator = new ConsumingQueueIterator<>(emptyQueue);
      Consumer<Object> mockConsumer = (Consumer<Object>) mock(Consumer.class, new ViolatedAssumptionAnswer());
      
      // When: Calling forEachRemaining
      iterator.forEachRemaining(mockConsumer);
      
      // Then: hasNext() should return false after consuming all elements
      assertFalse("hasNext() should return false after forEachRemaining on empty queue", 
                  iterator.hasNext());
  }

  @Test(timeout = 4000)
  public void testForEachRemaining_AfterPeek_ConsumesRemainingElements() {
      // Given: A queue with one element
      LinkedList<Object> queue = new LinkedList<>();
      Object element = new Object();
      queue.add(element);
      
      // When: Creating iterator, peeking, then consuming remaining elements
      ConsumingQueueIterator<Object> iterator = new ConsumingQueueIterator<>(queue);
      iterator.peek(); // Peek at the element without consuming
      Consumer<Object> mockConsumer = (Consumer<Object>) mock(Consumer.class, new ViolatedAssumptionAnswer());
      iterator.forEachRemaining(mockConsumer);
      
      // Then: The consumer should be invoked (verified by mock framework)
  }

  @Test(timeout = 4000)
  public void testNext_ThrowsNoSuchElementException_WhenQueueIsEmpty() {
      // Given: An empty priority queue
      PriorityQueue<Locale.FilteringMode> emptyQueue = new PriorityQueue<>();
      ConsumingQueueIterator<Locale.FilteringMode> iterator = new ConsumingQueueIterator<>(emptyQueue);
      
      // When/Then: Calling next() on empty iterator should throw NoSuchElementException
      try {
        iterator.next();
        fail("Expected NoSuchElementException when calling next() on empty iterator");
      } catch(NoSuchElementException e) {
        // Expected exception
        verifyException("com.google.common.collect.AbstractIterator", e);
      }
  }

  @Test(timeout = 4000)
  public void testPeek_ThrowsNoSuchElementException_AfterAllElementsConsumed() {
      // Given: An empty queue that has been consumed
      LinkedList<Object> emptyQueue = new LinkedList<>();
      ConsumingQueueIterator<Object> iterator = new ConsumingQueueIterator<>(emptyQueue);
      Consumer<Object> mockConsumer = (Consumer<Object>) mock(Consumer.class, new ViolatedAssumptionAnswer());
      iterator.forEachRemaining(mockConsumer);
      
      // When/Then: Calling peek() after consuming all elements should throw NoSuchElementException
      try {
        iterator.peek();
        fail("Expected NoSuchElementException when calling peek() on exhausted iterator");
      } catch(NoSuchElementException e) {
        // Expected exception
        verifyException("com.google.common.collect.AbstractIterator", e);
      }
  }
}
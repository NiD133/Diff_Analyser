package com.google.common.collect;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.junit.Test;

public class ConsumingQueueIteratorTest {

  @Test
  public void nextReturnsElementAndRemovesItFromQueue() {
    PriorityQueue<String> queue = new PriorityQueue<>();
    queue.add("only");

    ConsumingQueueIterator<String> it = new ConsumingQueueIterator<>(queue);

    assertSame("next should return the element from the queue", "only", it.next());
    assertTrue("queue should be consumed by the iterator", queue.isEmpty());
  }

  @Test
  public void hasNextReturnsTrueWhenQueueNotEmpty() {
    Queue<Integer> queue = new ArrayDeque<>();
    queue.add(42);

    ConsumingQueueIterator<Integer> it = new ConsumingQueueIterator<>(queue);

    assertTrue(it.hasNext());
  }

  @Test
  public void hasNextReturnsFalseWhenQueueEmpty() {
    Queue<Object> queue = new ArrayDeque<>();

    ConsumingQueueIterator<Object> it = new ConsumingQueueIterator<>(queue);

    assertFalse(it.hasNext());
  }

  @Test(expected = NoSuchElementException.class)
  public void nextThrowsWhenQueueEmpty() {
    Queue<Object> queue = new ArrayDeque<>();

    ConsumingQueueIterator<Object> it = new ConsumingQueueIterator<>(queue);

    it.next(); // should throw
  }

  @Test
  public void forEachRemainingConsumesAllElementsAndExhaustsIterator() {
    LinkedList<Integer> queue = new LinkedList<>();
    queue.add(1);
    queue.add(2);
    queue.add(3);

    ConsumingQueueIterator<Integer> it = new ConsumingQueueIterator<>(queue);

    StringBuilder seen = new StringBuilder();
    it.forEachRemaining(n -> {
      if (seen.length() > 0) seen.append(",");
      seen.append(n);
    });

    assertEquals("1,2,3", seen.toString());
    assertFalse("iterator should be exhausted after forEachRemaining", it.hasNext());
    assertTrue("underlying queue should be empty after consumption", queue.isEmpty());
  }

  @Test
  public void peekReturnsNextWithoutAdvancing() {
    LinkedList<Object> queue = new LinkedList<>();
    Object element = new Object();
    queue.add(element);

    ConsumingQueueIterator<Object> it = new ConsumingQueueIterator<>(queue);

    assertSame("peek should return the next element", element, it.peek());
    assertSame("next should return the same element after peek", element, it.next());
    assertFalse("iterator should be exhausted after consuming the only element", it.hasNext());
  }

  @Test(expected = NoSuchElementException.class)
  public void peekThrowsWhenQueueEmpty() {
    Queue<Object> queue = new ArrayDeque<>();

    ConsumingQueueIterator<Object> it = new ConsumingQueueIterator<>(queue);

    it.peek(); // should throw
  }

  @Test
  public void repeatedHasNextCallsDoNotAdvanceIterator() {
    Queue<String> queue = new ArrayDeque<>();
    queue.add("a");

    ConsumingQueueIterator<String> it = new ConsumingQueueIterator<>(queue);

    assertTrue(it.hasNext());
    assertTrue("calling hasNext again should not consume the element", it.hasNext());
    assertEquals("a", it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void forEachRemainingOnEmptyDoesNothing() {
    Queue<Integer> queue = new ArrayDeque<>();
    ConsumingQueueIterator<Integer> it = new ConsumingQueueIterator<>(queue);

    AtomicInteger calls = new AtomicInteger();
    Consumer<Integer> counter = x -> calls.incrementAndGet();

    it.forEachRemaining(counter);

    assertEquals("consumer should not be called for an empty iterator", 0, calls.get());
    assertFalse(it.hasNext());
  }
}
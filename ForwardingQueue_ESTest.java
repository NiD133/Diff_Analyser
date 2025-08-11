package com.google.common.collect;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Queue;
import org.junit.Test;

/**
 * Readable tests for ForwardingQueue's standard* helper methods.
 *
 * These tests use a simple concrete ForwardingQueue that:
 * - Implements element() and remove() by delegating to an ArrayDeque.
 * - Implements offer(), peek(), and poll() using the standard* helpers.
 *
 * This lets us verify the behaviors promised by standardOffer, standardPeek, and standardPoll:
 * - standardOffer delegates to add.
 * - standardPeek delegates to element and returns null on empty.
 * - standardPoll delegates to remove and returns null on empty.
 *
 * We also verify the expected exceptions for element()/remove() on an empty queue and that
 * offering null fails (as ArrayDeque does not permit null).
 */
public class ForwardingQueueTest {

  /**
   * Minimal concrete ForwardingQueue to exercise the standard* helpers.
   */
  private static class StandardForwardingQueue<E> extends ForwardingQueue<E> {
    private final Queue<E> delegate = new ArrayDeque<>();

    @Override
    protected Queue<E> delegate() {
      return delegate;
    }

    // Base implementations used by the standard* helpers

    @Override
    public E element() {
      return delegate.element();
    }

    @Override
    public E remove() {
      return delegate.remove();
    }

    // Use the standard* helpers we want to test

    @Override
    public boolean offer(E e) {
      return standardOffer(e);
    }

    @Override
    public E peek() {
      return standardPeek();
    }

    @Override
    public E poll() {
      return standardPoll();
    }
  }

  @Test
  public void standardOffer_addsElement_andReturnsTrue() {
    StandardForwardingQueue<String> q = new StandardForwardingQueue<>();

    boolean offered = q.offer("a");

    assertTrue(offered);
    assertEquals(1, q.size());
    assertTrue(q.contains("a"));
  }

  @Test(expected = NullPointerException.class)
  public void standardOffer_throwsOnNull() {
    StandardForwardingQueue<String> q = new StandardForwardingQueue<>();

    // ArrayDeque (our delegate) rejects null via add(), which standardOffer delegates to.
    q.offer(null);
  }

  @Test
  public void standardPeek_returnsHeadWithoutRemoving() {
    StandardForwardingQueue<String> q = new StandardForwardingQueue<>();
    q.offer("first");
    q.offer("second");

    String head = q.peek();

    assertEquals("first", head);
    // Peek should not remove
    assertEquals(2, q.size());
    assertTrue(q.contains("first"));
    assertTrue(q.contains("second"));
  }

  @Test
  public void standardPeek_onEmpty_returnsNull() {
    StandardForwardingQueue<String> q = new StandardForwardingQueue<>();

    assertNull(q.peek());
  }

  @Test
  public void standardPoll_removesAndReturnsHead() {
    StandardForwardingQueue<String> q = new StandardForwardingQueue<>();
    q.offer("first");
    q.offer("second");

    String head = q.poll();

    assertEquals("first", head);
    assertEquals(1, q.size());
    assertFalse(q.contains("first"));
    assertTrue(q.contains("second"));
  }

  @Test
  public void standardPoll_onEmpty_returnsNull() {
    StandardForwardingQueue<String> q = new StandardForwardingQueue<>();

    assertNull(q.poll());
  }

  @Test(expected = NoSuchElementException.class)
  public void remove_onEmpty_throwsNoSuchElementException() {
    StandardForwardingQueue<String> q = new StandardForwardingQueue<>();

    q.remove();
  }

  @Test(expected = NoSuchElementException.class)
  public void element_onEmpty_throwsNoSuchElementException() {
    StandardForwardingQueue<String> q = new StandardForwardingQueue<>();

    q.element();
  }
}
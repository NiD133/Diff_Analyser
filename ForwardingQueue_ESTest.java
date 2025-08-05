/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Queue;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link ForwardingQueue}.
 *
 * <p>This test class is in the {@code com.google.common.collect} package to access the protected
 * {@code standard*} helper methods of {@link ForwardingQueue}.
 */
public class ForwardingQueueTest {

  /**
   * A minimal {@link ForwardingQueue} implementation backed by an {@link ArrayDeque}.
   *
   * <p>This class is used to test the behavior of {@code ForwardingQueue}'s delegation and
   * standard helper methods.
   */
  private static class TestForwardingQueue<E> extends ForwardingQueue<E> {
    private final Queue<E> delegate;

    TestForwardingQueue(Queue<E> delegate) {
      this.delegate = delegate;
    }

    @Override
    protected Queue<E> delegate() {
      return delegate;
    }
  }

  private TestForwardingQueue<String> queue;

  @Before
  public void setUp() {
    // ArrayDeque is a standard Queue that does not permit nulls,
    // which allows testing of null-handling behavior.
    queue = new TestForwardingQueue<>(new ArrayDeque<>());
  }

  // --- Tests for standard* helper methods ---

  @Test
  public void standardOffer_addsElementToQueue() {
    // Act
    boolean result = queue.standardOffer("A");

    // Assert
    assertTrue(result);
    assertFalse(queue.isEmpty());
    assertEquals("A", queue.peek());
  }

  @Test(expected = NullPointerException.class)
  public void standardOffer_withNull_throwsExceptionWhenDelegateDisallowsNulls() {
    // Act
    queue.standardOffer(null);
  }

  @Test
  public void standardPoll_onNonEmptyQueue_removesAndReturnsHead() {
    // Arrange
    queue.add("A");

    // Act
    String polledElement = queue.standardPoll();

    // Assert
    assertEquals("A", polledElement);
    assertTrue(queue.isEmpty());
  }

  @Test
  public void standardPoll_onEmptyQueue_returnsNull() {
    // Arrange
    assertTrue(queue.isEmpty());

    // Act
    String polledElement = queue.standardPoll();

    // Assert
    assertNull(polledElement);
  }

  @Test
  public void standardPeek_onNonEmptyQueue_returnsHeadWithoutRemoving() {
    // Arrange
    queue.add("A");
    queue.add("B");

    // Act
    String peekedElement = queue.standardPeek();

    // Assert
    assertEquals("A", peekedElement);
    assertEquals(2, queue.size());
  }

  @Test
  public void standardPeek_onEmptyQueue_returnsNull() {
    // Arrange
    assertTrue(queue.isEmpty());

    // Act
    String peekedElement = queue.standardPeek();

    // Assert
    assertNull(peekedElement);
  }

  // --- Tests for delegated methods ---

  @Test
  public void offer_addsElementAndReturnsTrue() {
    // Act
    boolean result = queue.offer("A");

    // Assert
    assertTrue(result);
    assertEquals("A", queue.peek());
  }

  @Test(expected = NullPointerException.class)
  public void offer_withNull_throwsExceptionWhenDelegateDisallowsNulls() {
    // Act
    queue.offer(null);
  }

  @Test
  public void poll_onNonEmptyQueue_removesAndReturnsHead() {
    // Arrange
    queue.add("A");

    // Act
    String polledElement = queue.poll();

    // Assert
    assertEquals("A", polledElement);
    assertTrue(queue.isEmpty());
  }

  @Test
  public void poll_onEmptyQueue_returnsNull() {
    // Arrange
    assertTrue(queue.isEmpty());

    // Act
    String polledElement = queue.poll();

    // Assert
    assertNull(polledElement);
  }

  @Test
  public void remove_onNonEmptyQueue_removesAndReturnsHead() {
    // Arrange
    queue.add("A");

    // Act
    String removedElement = queue.remove();

    // Assert
    assertEquals("A", removedElement);
    assertTrue(queue.isEmpty());
  }

  @Test(expected = NoSuchElementException.class)
  public void remove_onEmptyQueue_throwsNoSuchElementException() {
    // Arrange
    assertTrue(queue.isEmpty());

    // Act
    queue.remove();
  }

  @Test
  public void peek_onNonEmptyQueue_returnsHeadWithoutRemoving() {
    // Arrange
    queue.add("A");
    queue.add("B");

    // Act
    String peekedElement = queue.peek();

    // Assert
    assertEquals("A", peekedElement);
    assertEquals(2, queue.size());
  }

  @Test
  public void peek_onEmptyQueue_returnsNull() {
    // Arrange
    assertTrue(queue.isEmpty());

    // Act
    String peekedElement = queue.peek();

    // Assert
    assertNull(peekedElement);
  }

  @Test
  public void element_onNonEmptyQueue_returnsHeadWithoutRemoving() {
    // Arrange
    queue.add("A");
    queue.add("B");

    // Act
    String element = queue.element();

    // Assert
    assertEquals("A", element);
    assertEquals(2, queue.size());
  }

  @Test(expected = NoSuchElementException.class)
  public void element_onEmptyQueue_throwsNoSuchElementException() {
    // Arrange
    assertTrue(queue.isEmpty());

    // Act
    queue.element();
  }
}
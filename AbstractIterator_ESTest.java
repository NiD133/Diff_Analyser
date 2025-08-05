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
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link AbstractIterator}.
 *
 * <p>This test suite uses {@link ConsumingQueueIterator}, a simple concrete implementation of
 * {@link AbstractIterator}, to verify the public contract of the abstract class.
 */
@RunWith(JUnit4.class)
public class AbstractIteratorTest {

  /**
   * A simple, concrete implementation of {@link AbstractIterator} for testing purposes. It wraps a
   * {@link Queue} and consumes it.
   */
  private static class ConsumingQueueIterator<T> extends AbstractIterator<T> {
    private final Queue<T> queue;

    ConsumingQueueIterator(Queue<T> queue) {
      this.queue = queue;
    }

    @Override
    protected T computeNext() {
      if (queue.isEmpty()) {
        return endOfData();
      }
      return queue.remove();
    }
  }

  /**
   * Creates a {@link ConsumingQueueIterator} for the given elements.
   */
  private <T> AbstractIterator<T> createIterator(T... elements) {
    return new ConsumingQueueIterator<>(new ArrayDeque<>(Arrays.asList(elements)));
  }

  @Test
  public void hasNext_whenIteratorHasElements_returnsTrue() {
    // Arrange
    AbstractIterator<String> iterator = createIterator("A");

    // Act & Assert
    assertTrue("Iterator should have a next element", iterator.hasNext());
  }

  @Test
  public void hasNext_whenIteratorIsEmpty_returnsFalse() {
    // Arrange
    AbstractIterator<String> iterator = createIterator();

    // Act & Assert
    assertFalse("Empty iterator should not have a next element", iterator.hasNext());
  }

  @Test
  public void next_whenIteratorHasElements_returnsElementsInOrder() {
    // Arrange
    AbstractIterator<String> iterator = createIterator("A", "B");

    // Act & Assert
    assertEquals("A", iterator.next());
    assertEquals("B", iterator.next());
    assertFalse("Iterator should be exhausted after all elements are consumed", iterator.hasNext());
  }

  @Test
  public void next_whenIteratorIsEmpty_throwsNoSuchElementException() {
    // Arrange
    AbstractIterator<Object> emptyIterator = createIterator();

    // Act & Assert
    assertThrows(NoSuchElementException.class, emptyIterator::next);
  }

  @Test
  public void peek_whenIteratorHasElements_returnsNextElementWithoutAdvancing() {
    // Arrange
    AbstractIterator<String> iterator = createIterator("A", "B");

    // Act & Assert
    assertEquals("peek() should return the next element", "A", iterator.peek());
    assertEquals("peek() should not advance the iterator", "A", iterator.peek());
    assertTrue("hasNext() should still be true after peeking", iterator.hasNext());
    assertEquals("next() should return the peeked element", "A", iterator.next());
  }

  @Test
  public void peek_whenIteratorIsEmpty_throwsNoSuchElementException() {
    // Arrange
    AbstractIterator<Object> emptyIterator = createIterator();

    // Act & Assert
    assertThrows(NoSuchElementException.class, emptyIterator::peek);
  }

  @Test
  public void forEachRemaining_consumesAllElementsFromNonEmptyIterator() {
    // Arrange
    AbstractIterator<String> iterator = createIterator("A", "B", "C");
    List<String> consumedElements = new ArrayList<>();

    // Act
    iterator.forEachRemaining(consumedElements::add);

    // Assert
    assertEquals(Arrays.asList("A", "B", "C"), consumedElements);
    assertFalse("Iterator should be exhausted after forEachRemaining", iterator.hasNext());
  }

  @Test
  public void forEachRemaining_onEmptyIterator_doesNothing() {
    // Arrange
    AbstractIterator<Object> emptyIterator = createIterator();
    List<Object> consumedElements = new ArrayList<>();

    // Act
    emptyIterator.forEachRemaining(consumedElements::add);

    // Assert
    assertTrue("List should be empty as the iterator was empty", consumedElements.isEmpty());
    assertFalse("Iterator should remain exhausted", emptyIterator.hasNext());
  }
}
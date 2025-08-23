package com.google.common.collect;

import static com.google.common.collect.ReflectionFreeAssertThrows.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;
import junit.framework.TestCase;
import org.jspecify.annotations.Nullable;

/**
 * Tests for the {@link AbstractIterator#peek()} method and its interaction with {@code next()} and
 * {@code hasNext()}.
 */
public class AbstractIteratorPeekTest extends TestCase {

  /**
   * Creates a sample AbstractIterator that produces 0, then 1, and then signals it has reached the
   * end of the data.
   *
   * <p>The implementation includes an assertion to fail if {@code computeNext} is called more
   * times than expected, which helps verify the internal state machine of {@code
   * AbstractIterator}.
   */
  private AbstractIterator<Integer> createIteratorProducingZeroAndOne() {
    return new AbstractIterator<Integer>() {
      private int callCount;

      @Override
      @Nullable
      public Integer computeNext() {
        switch (callCount++) {
          case 0:
            return 0;
          case 1:
            return 1;
          case 2:
            return endOfData();
          default:
            throw new AssertionError("computeNext() should not have been called again");
        }
      }
    };
  }

  public void testPeek_isIdempotentAndDoesNotAdvanceIterator() {
    // Arrange
    AbstractIterator<Integer> iterator = createIteratorProducingZeroAndOne();

    // Act & Assert
    assertEquals("First peek should return the first element.", Integer.valueOf(0), iterator.peek());
    assertEquals(
        "Second peek should return the same element.", Integer.valueOf(0), iterator.peek());
    assertTrue("hasNext() should still be true after peeking.", iterator.hasNext());
  }

  public void testNext_afterPeek_returnsPeekedElement() {
    // Arrange
    AbstractIterator<Integer> iterator = createIteratorProducingZeroAndOne();
    Integer peekedElement = iterator.peek();

    // Act
    Integer nextElement = iterator.next();

    // Assert
    assertEquals("The peeked element should be the first element.", Integer.valueOf(0), peekedElement);
    assertEquals("next() should return the same element as peek().", peekedElement, nextElement);
  }

  public void testPeek_afterNext_returnsNewNextElement() {
    // Arrange
    AbstractIterator<Integer> iterator = createIteratorProducingZeroAndOne();
    iterator.next(); // Consume the first element (0)

    // Act & Assert
    assertEquals(
        "peek() should now return the second element.", Integer.valueOf(1), iterator.peek());
    assertTrue("hasNext() should still be true.", iterator.hasNext());
    assertEquals("next() should return the second element.", Integer.valueOf(1), iterator.next());
  }

  public void testPeek_atEndOfData_throwsNoSuchElementException() {
    // Arrange
    AbstractIterator<Integer> iterator = createIteratorProducingZeroAndOne();
    iterator.next(); // Consume 0
    iterator.next(); // Consume 1

    // Act & Assert
    assertFalse("Iterator should be exhausted.", iterator.hasNext());
    assertThrows(NoSuchElementException.class, iterator::peek);
  }

  public void testIterator_remainsExhaustedAfterFailedCalls() {
    // Arrange
    AbstractIterator<Integer> iterator = createIteratorProducingZeroAndOne();
    iterator.next(); // Consume 0
    iterator.next(); // Consume 1

    // Act & Assert
    // Verify that once the iterator is exhausted, subsequent calls to peek() and next()
    // consistently throw NoSuchElementException, regardless of order.
    assertThrows("First peek on exhausted iterator should throw.", NoSuchElementException.class, iterator::peek);
    assertThrows("Second peek on exhausted iterator should throw.", NoSuchElementException.class, iterator::peek);
    assertThrows("A call to next() after a failed peek() should also throw.", NoSuchElementException.class, iterator::next);
    assertThrows("A final peek() should still throw.", NoSuchElementException.class, iterator::peek);
  }
}
package com.google.common.collect;

import static com.google.common.collect.ReflectionFreeAssertThrows.assertThrows;

import com.google.common.collect.TestExceptions.SomeUncheckedException;
import java.util.Iterator;
import junit.framework.TestCase;

/**
 * Tests for {@link AbstractIterator}.
 */
public class AbstractIteratorTest extends TestCase {

  /**
   * This test verifies the behavior of the iterator when the {@code computeNext()} implementation
   * first signals the end of data and then immediately throws an exception. The expected behavior
   * is that the exception is propagated, and the iterator is left in a failed state.
   */
  public void testHasNext_whenComputeNextThrowsAfterSignalingEndOfData_propagatesException() {
    // Arrange: Create an iterator whose computeNext() calls endOfData() and then throws.
    Iterator<Integer> iterator =
        new AbstractIterator<Integer>() {
          @Override
          protected Integer computeNext() {
            endOfData();
            throw new SomeUncheckedException();
          }
        };

    // Act & Assert: The first call to hasNext() should propagate the exception from computeNext().
    assertThrows(SomeUncheckedException.class, iterator::hasNext);

    // Assert: The iterator is now in a failed state. Subsequent calls to hasNext() or next()
    // should throw an IllegalStateException, as documented by AbstractIterator.
    assertThrows(IllegalStateException.class, iterator::hasNext);
    assertThrows(IllegalStateException.class, iterator::next);
  }
}
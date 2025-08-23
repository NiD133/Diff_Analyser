package com.google.common.collect;

import static com.google.common.collect.ReflectionFreeAssertThrows.assertThrows;

import java.util.NoSuchElementException;
import junit.framework.TestCase;

public class AbstractIteratorTestTest4 extends TestCase {

  /**
   * Tests that calling peek() on an iterator that is immediately exhausted throws a {@link
   * NoSuchElementException}.
   */
  public void testPeek_onEmptyIterator_throwsNoSuchElementException() {
    // Arrange: Create an iterator that is immediately exhausted. Its computeNext() will be called
    // once (the first time hasNext() or peek() is called) and will signal the end of data.
    AbstractIterator<Integer> emptyIterator =
        new AbstractIterator<Integer>() {
          @Override
          protected Integer computeNext() {
            return endOfData();
          }
        };

    // Act & Assert:
    // The first call to peek() should trigger computeNext(), find the end of data,
    // and throw NoSuchElementException.
    assertThrows(NoSuchElementException.class, emptyIterator::peek);

    // A subsequent call to peek() should also throw. This ensures that the iterator
    // remains in a terminal state and consistently reports that it's empty.
    assertThrows(NoSuchElementException.class, emptyIterator::peek);
  }
}
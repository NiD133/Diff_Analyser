package com.google.common.collect;

import static com.google.common.collect.ReflectionFreeAssertThrows.assertThrows;

import java.util.Iterator;
import junit.framework.TestCase;

public class AbstractIteratorTestTest9 extends TestCase {

  /**
   * Verifies that calling {@code hasNext()} from within {@code computeNext()} throws an {@code
   * IllegalStateException}. This is a "reentrant" call that is explicitly disallowed by the {@code
   * AbstractIterator} contract to prevent infinite loops and inconsistent state.
   */
  public void testHasNext_whenCalledFromComputeNext_throwsIllegalStateException() {
    // ARRANGE: Create an iterator that makes a reentrant call to hasNext()
    // inside its computeNext() implementation.
    Iterator<Integer> iterator =
        new AbstractIterator<Integer>() {
          @Override
          protected Integer computeNext() {
            // This reentrant call is disallowed by the AbstractIterator contract.
            hasNext();

            // This line should be unreachable. If it's reached, the test will fail
            // because the IllegalStateException was not thrown as expected.
            return 42;
          }
        };

    // ACT & ASSERT: The initial call to hasNext() on the iterator triggers computeNext().
    // This in turn makes the illegal reentrant call, which should throw an exception.
    assertThrows(IllegalStateException.class, iterator::hasNext);
  }
}
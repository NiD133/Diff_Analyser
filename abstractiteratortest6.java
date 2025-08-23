package com.google.common.collect;

import static com.google.common.collect.ReflectionFreeAssertThrows.assertThrows;

import com.google.common.collect.TestExceptions.SomeUncheckedException;
import java.util.Iterator;
import junit.framework.TestCase;

/**
 * Tests for {@link AbstractIterator}.
 */
public class AbstractIteratorTestTest6 extends TestCase {

  /**
   * Tests that an unchecked exception thrown from {@link AbstractIterator#computeNext()} is
   * propagated without modification when {@link Iterator#hasNext()} is called.
   */
  public void testHasNext_propagatesUncheckedExceptionFromComputeNext() {
    // ARRANGE: Create an exception and an iterator that is designed to throw it.
    SomeUncheckedException expectedException = new SomeUncheckedException();
    Iterator<Integer> iterator =
        new AbstractIterator<Integer>() {
          @Override
          protected Integer computeNext() {
            throw expectedException;
          }
        };

    // ACT: Call hasNext(), which should trigger computeNext() and throw the exception.
    SomeUncheckedException thrownException =
        assertThrows(SomeUncheckedException.class, iterator::hasNext);

    // ASSERT: Verify that the caught exception is the exact same instance we arranged to be thrown,
    // confirming it was propagated without being wrapped or altered.
    assertSame(
        "The propagated exception should be the same instance thrown from computeNext()",
        expectedException,
        thrownException);
  }
}
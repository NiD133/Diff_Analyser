package com.google.common.collect;

import static com.google.common.collect.ReflectionFreeAssertThrows.assertThrows;
import static com.google.common.collect.SneakyThrows.sneakyThrow;

import com.google.common.annotations.AndroidIncompatible;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.collect.TestExceptions.SomeCheckedException;
import com.google.common.collect.TestExceptions.SomeUncheckedException;
import com.google.common.testing.GcFinalization;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.NoSuchElementException;
import junit.framework.TestCase;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Tests for AbstractIterator.
 *
 * The test suite focuses on:
 * - Contract of hasNext/next and idempotence of hasNext
 * - Behavior of peek
 * - Exception propagation and iterator failure state
 * - Removal support
 * - Reentrancy protection
 * - Ensuring references are not retained after consumption
 */
@SuppressWarnings("serial") // No serialization used in tests
@GwtCompatible(emulated = true)
@NullMarked
public class AbstractIteratorTest extends TestCase {

  /**
   * Iterator that returns 0, then 1, then signals end of data.
   */
  private static final class TwoElementIterator extends AbstractIterator<Integer> {
    private int index = 0; // 0 -> return 0, 1 -> return 1, 2 -> end

    @Override
    protected @Nullable Integer computeNext() {
      switch (index++) {
        case 0:
          return 0;
        case 1:
          return 1;
        case 2:
          return endOfData();
        default:
          throw new AssertionError("computeNext() should not be called after endOfData()");
      }
    }
  }

  /**
   * Empty iterator that calls endOfData() exactly once; subsequent calls are a test failure.
   */
  private static final class EmptyIterator extends AbstractIterator<Integer> {
    private boolean ended;

    @Override
    protected @Nullable Integer computeNext() {
      if (ended) {
        fail("computeNext() must not be called again after endOfData()");
      }
      ended = true;
      return endOfData();
    }
  }

  /**
   * Iterator whose computeNext() always returns a new Object instance.
   */
  private static final class NewObjectEachTimeIterator extends AbstractIterator<Object> {
    @Override
    protected Object computeNext() {
      return new Object();
    }
  }

  public void testNextAndHasNext_defaultBehavior() {
    // Arrange
    Iterator<Integer> iter = new TwoElementIterator();

    // Act + Assert: first element
    assertTrue(iter.hasNext());
    assertEquals(0, (int) iter.next());

    // hasNext() is idempotent
    assertTrue(iter.hasNext());
    assertTrue(iter.hasNext());
    assertTrue(iter.hasNext());

    // Act + Assert: second element
    assertEquals(1, (int) iter.next());

    // End of iteration
    assertFalse(iter.hasNext());

    // computeNext() must not run again after end
    assertFalse(iter.hasNext());

    // Next past end throws
    assertThrows(NoSuchElementException.class, iter::next);
  }

  public void testPeek_defaultBehavior() {
    // Arrange
    AbstractIterator<Integer> iter = new TwoElementIterator();

    // Peek does not advance
    assertEquals(0, (int) iter.peek());
    assertEquals(0, (int) iter.peek());

    // hasNext() remains true and does not advance
    assertTrue(iter.hasNext());
    assertEquals(0, (int) iter.peek());

    // First next() returns the peeked value
    assertEquals(0, (int) iter.next());

    // Next element can also be peeked before consuming
    assertEquals(1, (int) iter.peek());
    assertEquals(1, (int) iter.next());

    // After exhaustion, peek/next must consistently throw
    assertThrows(NoSuchElementException.class, iter::peek);
    assertThrows(NoSuchElementException.class, iter::peek);
    assertThrows(NoSuchElementException.class, iter::next);
    assertThrows(NoSuchElementException.class, iter::peek);
  }

  @J2ktIncompatible // weak references, details of GC
  @GwtIncompatible // weak references
  @AndroidIncompatible // depends on details of GC
  public void testFreesNextReference() {
    Iterator<Object> iter = new NewObjectEachTimeIterator();

    // Hold only a weak reference to the element and ensure it can be GC'd after iteration advances.
    WeakReference<Object> ref = new WeakReference<>(iter.next());
    GcFinalization.awaitClear(ref);
  }

  public void testPeek_onEmptyIterator_throwsNoSuchElementException() {
    AbstractIterator<Integer> empty = new EmptyIterator();

    // Multiple calls to peek still throw the correct exception
    assertThrows(NoSuchElementException.class, empty::peek);
    assertThrows(NoSuchElementException.class, empty::peek);
  }

  public void testCheckedExceptionIsSneakilyPropagated_thenIteratorIsFailed() throws Exception {
    // Iterator whose computeNext throws a checked exception using sneakyThrow
    Iterator<Integer> iter =
        new AbstractIterator<Integer>() {
          @Override
          public Integer computeNext() {
            throw sneakyThrow(new SomeCheckedException());
          }
        };

    // First call propagates the checked exception
    assertThrows(SomeCheckedException.class, iter::hasNext);

    // After an exception, the iterator is in FAILED state and further usage throws ISE
    assertThrows(IllegalStateException.class, iter::hasNext);
  }

  public void testUncheckedExceptionPassesThrough() {
    SomeUncheckedException exception = new SomeUncheckedException();

    Iterator<Integer> iter =
        new AbstractIterator<Integer>() {
          @Override
          public Integer computeNext() {
            throw exception;
          }
        };

    // Unchecked exceptions propagate as-is
    SomeUncheckedException e = assertThrows(SomeUncheckedException.class, iter::hasNext);
    assertSame(exception, e);
  }

  public void testExceptionAfterEndOfData_isStillSurfaced() {
    Iterator<Integer> iter =
        new AbstractIterator<Integer>() {
          @Override
          public Integer computeNext() {
            endOfData();
            throw new SomeUncheckedException();
          }
        };

    // Even after calling endOfData(), an exception thrown by computeNext surfaces to the caller
    assertThrows(SomeUncheckedException.class, iter::hasNext);
  }

  @SuppressWarnings("DoNotCall")
  public void testRemove_isUnsupported() {
    Iterator<Integer> iter =
        new AbstractIterator<Integer>() {
          boolean returnedOnce;

          @Override
          public Integer computeNext() {
            if (returnedOnce) {
              return endOfData();
            }
            returnedOnce = true;
            return 0;
          }
        };

    assertEquals(0, (int) iter.next());
    assertThrows(UnsupportedOperationException.class, iter::remove);
  }

  public void testReentrantHasNext_throwsIllegalState() {
    Iterator<Integer> iter =
        new AbstractIterator<Integer>() {
          @Override
          protected Integer computeNext() {
            // Calling hasNext() (or next()/peek()) from computeNext() is illegal
            boolean unused = hasNext();
            throw new AssertionError("computeNext() should not proceed after reentrant call");
          }
        };

    assertThrows(IllegalStateException.class, iter::hasNext);
  }
}
package com.google.common.collect;

import static com.google.common.collect.ReflectionFreeAssertThrows.assertThrows;
import static com.google.common.collect.SneakyThrows.sneakyThrow;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.collect.TestExceptions.SomeCheckedException;
import com.google.common.collect.TestExceptions.SomeUncheckedException;
import com.google.common.testing.GcFinalization;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Unit tests for {@link AbstractIterator}.  These tests focus on verifying the core contract
 * of AbstractIterator, especially around `computeNext()` and `endOfData()`.
 */
@GwtCompatible(emulated = true)
@NullMarked
@RunWith(JUnit4.class)
public class AbstractIteratorTest {

  @Test
  public void testNextAndHasNext_defaultBehavior_returnsSequenceAndThenEmpty() {
    // Arrange: Define an AbstractIterator that returns 0, then 1, then signals endOfData.
    Iterator<Integer> iterator =
        new AbstractIterator<Integer>() {
          private int callCount;

          @Override
          protected @Nullable Integer computeNext() {
            switch (callCount++) {
              case 0:
                return 0;
              case 1:
                return 1;
              case 2:
                return endOfData();
              default:
                throw new AssertionError("computeNext() called more than expected");
            }
          }
        };

    // Act & Assert: Verify the sequence of hasNext() and next() calls.
    assertTrue(iterator.hasNext());
    assertEquals(Integer.valueOf(0), iterator.next());

    // hasNext() should be idempotent (multiple calls should return the same value).
    assertTrue(iterator.hasNext());
    assertTrue(iterator.hasNext());
    assertEquals(Integer.valueOf(1), iterator.next());

    assertFalse(iterator.hasNext());

    // After endOfData() is called, hasNext() should consistently return false.
    assertFalse(iterator.hasNext());

    // Calling next() after endOfData() should throw NoSuchElementException.
    assertThrows(NoSuchElementException.class, iterator::next);
  }

  @Test
  public void testPeek_defaultBehavior_returnsNextElementWithoutAdvancing() {
    // Arrange: Define an AbstractIterator that returns 0, then 1, then signals endOfData.
    AbstractIterator<Integer> iterator =
        new AbstractIterator<Integer>() {
          private int callCount;

          @Override
          protected @Nullable Integer computeNext() {
            switch (callCount++) {
              case 0:
                return 0;
              case 1:
                return 1;
              case 2:
                return endOfData();
              default:
                throw new AssertionError("computeNext() called more than expected");
            }
          }
        };

    // Act & Assert: Verify peek() returns the next element without advancing the iterator.
    assertEquals(Integer.valueOf(0), iterator.peek());
    assertEquals(Integer.valueOf(0), iterator.peek()); // peek() can be called multiple times.
    assertTrue(iterator.hasNext()); // Calling peek() should not affect hasNext().
    assertEquals(Integer.valueOf(0), iterator.peek());
    assertEquals(Integer.valueOf(0), iterator.next()); // next() advances the iterator.

    assertEquals(Integer.valueOf(1), iterator.peek());
    assertEquals(Integer.valueOf(1), iterator.next());

    // peek() and next() should throw NoSuchElementException after endOfData().
    assertThrows(NoSuchElementException.class, iterator::peek);
    assertThrows(NoSuchElementException.class, iterator::peek);
    assertThrows(NoSuchElementException.class, iterator::next);
    assertThrows(NoSuchElementException.class, iterator::peek);
  }

  @J2ktIncompatible // weak references, details of GC
  @GwtIncompatible // weak references
  @AndroidIncompatible // depends on details of GC
  @Test
  public void testFreesNextReference_allowsGarbageCollection() {
    // Arrange: Create an AbstractIterator that always returns a new Object.
    Iterator<Object> iterator =
        new AbstractIterator<Object>() {
          @Override
          protected Object computeNext() {
            return new Object();
          }
        };

    // Act: Get the next object and create a weak reference to it.
    WeakReference<Object> ref = new WeakReference<>(iterator.next());

    // Assert:  Ensure that the object is garbage collected.  This relies on GC finalization, so
    // it might be flaky if the GC doesn't run.
    GcFinalization.awaitClear(ref);
  }

  @Test
  public void testPeek_emptyIteration_throwsNoSuchElementException() {
    // Arrange: Define an AbstractIterator that immediately calls endOfData.
    AbstractIterator<Integer> emptyIterator =
        new AbstractIterator<Integer>() {
          private boolean endOfDataCalled;

          @Override
          protected @Nullable Integer computeNext() {
            if (endOfDataCalled) {
              throw new AssertionError("computeNext() called more than expected");
            }
            endOfDataCalled = true;
            return endOfData();
          }
        };

    // Act & Assert: peek() should throw NoSuchElementException on an empty iterator.
    assertThrows(NoSuchElementException.class, emptyIterator::peek);
    assertThrows(NoSuchElementException.class, emptyIterator::peek); // Multiple calls should still throw.
  }

  @Test
  public void testSneakyThrow_checkedException_propagatesCorrectlyThenThrowsISE() {
    // Arrange: Define an AbstractIterator that throws a checked exception using sneakyThrow.
    Iterator<Integer> iterator =
        new AbstractIterator<Integer>() {
          private boolean computeNextCalled;

          @Override
          protected Integer computeNext() {
            if (computeNextCalled) {
              throw new AssertionError("computeNext() called more than expected");
            }
            computeNextCalled = true;
            throw sneakyThrow(new SomeCheckedException());
          }
        };

    // Act & Assert: The first call to hasNext() should propagate the sneaky-thrown exception.
    assertThrows(SomeCheckedException.class, iterator::hasNext);

    // Subsequent calls to hasNext() should throw IllegalStateException because the iterator is now in a failed state.
    assertThrows(IllegalStateException.class, iterator::hasNext);
  }

  @Test
  public void testException_uncheckedException_propagatesUnchanged() {
    // Arrange: Define an AbstractIterator that throws an unchecked exception.
    SomeUncheckedException exception = new SomeUncheckedException();
    Iterator<Integer> iterator =
        new AbstractIterator<Integer>() {
          @Override
          protected Integer computeNext() {
            throw exception;
          }
        };

    // Act: Call hasNext() and capture the exception.
    SomeUncheckedException caughtException = assertThrows(SomeUncheckedException.class, iterator::hasNext);

    // Assert: The exception thrown by hasNext() should be the same instance as the one thrown by computeNext().
    assertSame(exception, caughtException);
  }

  @Test
  public void testExceptionAfterEndOfData_uncheckedException_propagatesUnchanged() {
    // Arrange: Define an AbstractIterator that calls endOfData() then throws an unchecked exception.
    Iterator<Integer> iterator =
        new AbstractIterator<Integer>() {
          @Override
          protected Integer computeNext() {
            endOfData();
            throw new SomeUncheckedException();
          }
        };

    // Act & Assert:  The exception thrown by hasNext() should be the SomeUncheckedException
    assertThrows(SomeUncheckedException.class, iterator::hasNext);
  }

  @Test
  public void testRemove_unsupportedOperation() {
    // Arrange: Define an AbstractIterator.
    Iterator<Integer> iterator =
        new AbstractIterator<Integer>() {
          private boolean computeNextCalled;

          @Override
          protected Integer computeNext() {
            if (computeNextCalled) {
              return endOfData();
            }
            computeNextCalled = true;
            return 0;
          }
        };

    // Act: Call next() to advance the iterator.
    assertEquals(Integer.valueOf(0), iterator.next());

    // Assert: remove() should throw UnsupportedOperationException because AbstractIterator doesn't implement it.
    assertThrows(UnsupportedOperationException.class, iterator::remove);
  }

  @Test
  public void testReentrantHasNext_throwsIllegalStateException() {
    // Arrange: Define an AbstractIterator where computeNext() calls hasNext().
    Iterator<Integer> iterator =
        new AbstractIterator<Integer>() {
          @Override
          protected Integer computeNext() {
            boolean unused = hasNext(); // Reentrant call to hasNext().
            throw new AssertionError("Should not reach this point");
          }
        };

    // Act & Assert: hasNext() should throw IllegalStateException due to the reentrant call.
    assertThrows(IllegalStateException.class, iterator::hasNext);
  }
}
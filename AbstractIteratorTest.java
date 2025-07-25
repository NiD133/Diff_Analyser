package com.google.common.collect;

import static com.google.common.collect.ReflectionFreeAssertThrows.assertThrows;
import static com.google.common.collect.SneakyThrows.sneakyThrow;

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
 * Unit test for {@code AbstractIterator}.
 */
@GwtCompatible(emulated = true)
@NullMarked
public class AbstractIteratorTest extends TestCase {

  /**
   * Tests the default behavior of next() and hasNext() methods.
   */
  public void testNextAndHasNextBehavior() {
    Iterator<Integer> iterator = new AbstractIterator<Integer>() {
      private int state = 0;

      @Override
      public @Nullable Integer computeNext() {
        switch (state++) {
          case 0: return 0;
          case 1: return 1;
          default: return endOfData();
        }
      }
    };

    assertTrue(iterator.hasNext());
    assertEquals(0, (int) iterator.next());

    // Verify idempotence of hasNext()
    assertTrue(iterator.hasNext());
    assertTrue(iterator.hasNext());
    assertEquals(1, (int) iterator.next());

    assertFalse(iterator.hasNext());
    assertThrows(NoSuchElementException.class, iterator::next);
  }

  /**
   * Tests the default behavior of the peek() method.
   */
  public void testPeekBehavior() {
    AbstractIterator<Integer> iterator = new AbstractIterator<Integer>() {
      private int state = 0;

      @Override
      public @Nullable Integer computeNext() {
        switch (state++) {
          case 0: return 0;
          case 1: return 1;
          default: return endOfData();
        }
      }
    };

    assertEquals(0, (int) iterator.peek());
    assertEquals(0, (int) iterator.peek());
    assertTrue(iterator.hasNext());
    assertEquals(0, (int) iterator.next());

    assertEquals(1, (int) iterator.peek());
    assertEquals(1, (int) iterator.next());

    // Ensure peek() throws NoSuchElementException after end of data
    assertThrows(NoSuchElementException.class, iterator::peek);
    assertThrows(NoSuchElementException.class, iterator::next);
  }

  /**
   * Tests that the iterator frees the reference to the next element after it is returned.
   */
  @J2ktIncompatible // weak references, details of GC
  @GwtIncompatible // weak references
  @AndroidIncompatible // depends on details of GC
  public void testFreesNextReference() {
    Iterator<Object> iterator = new AbstractIterator<Object>() {
      @Override
      public Object computeNext() {
        return new Object();
      }
    };
    WeakReference<Object> ref = new WeakReference<>(iterator.next());
    GcFinalization.awaitClear(ref);
  }

  /**
   * Tests the behavior of peek() when the iteration is empty.
   */
  public void testPeekOnEmptyIteration() {
    AbstractIterator<Integer> emptyIterator = new AbstractIterator<Integer>() {
      private boolean endOfDataCalled = false;

      @Override
      public @Nullable Integer computeNext() {
        if (endOfDataCalled) {
          fail("computeNext() should not be called again after endOfData()");
        }
        endOfDataCalled = true;
        return endOfData();
      }
    };

    assertThrows(NoSuchElementException.class, emptyIterator::peek);
    assertThrows(NoSuchElementException.class, emptyIterator::peek);
  }

  /**
   * Tests the sneakyThrow() method with a checked exception.
   */
  public void testSneakyThrow() throws Exception {
    Iterator<Integer> iterator = new AbstractIterator<Integer>() {
      private boolean called = false;

      @Override
      public Integer computeNext() {
        if (called) {
          throw new AssertionError("computeNext() should not be called again");
        }
        called = true;
        throw sneakyThrow(new SomeCheckedException());
      }
    };

    assertThrows(SomeCheckedException.class, iterator::hasNext);
    assertThrows(IllegalStateException.class, iterator::hasNext);
  }

  /**
   * Tests that unchecked exceptions are propagated correctly.
   */
  public void testUncheckedExceptionPropagation() {
    SomeUncheckedException exception = new SomeUncheckedException();
    Iterator<Integer> iterator = new AbstractIterator<Integer>() {
      @Override
      public Integer computeNext() {
        throw exception;
      }
    };

    SomeUncheckedException thrownException = assertThrows(SomeUncheckedException.class, iterator::hasNext);
    assertSame(exception, thrownException);
  }

  /**
   * Tests that exceptions thrown after endOfData() are handled correctly.
   */
  public void testExceptionAfterEndOfData() {
    Iterator<Integer> iterator = new AbstractIterator<Integer>() {
      @Override
      public Integer computeNext() {
        endOfData();
        throw new SomeUncheckedException();
      }
    };
    assertThrows(SomeUncheckedException.class, iterator::hasNext);
  }

  /**
   * Tests that remove() is not supported.
   */
  @SuppressWarnings("DoNotCall")
  public void testRemoveNotSupported() {
    Iterator<Integer> iterator = new AbstractIterator<Integer>() {
      private boolean called = false;

      @Override
      public Integer computeNext() {
        if (called) {
          endOfData();
        }
        called = true;
        return 0;
      }
    };

    assertEquals(0, (int) iterator.next());
    assertThrows(UnsupportedOperationException.class, iterator::remove);
  }

  /**
   * Tests that reentrant calls to hasNext() throw an exception.
   */
  public void testReentrantHasNext() {
    Iterator<Integer> iterator = new AbstractIterator<Integer>() {
      @Override
      protected Integer computeNext() {
        hasNext(); // Reentrant call
        throw new AssertionError("Reentrant call to hasNext() should not be allowed");
      }
    };
    assertThrows(IllegalStateException.class, iterator::hasNext);
  }
}
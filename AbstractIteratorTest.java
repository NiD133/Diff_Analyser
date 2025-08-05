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
  public void testDefaultBehaviorOfNextAndHasNext() {
    Iterator<Integer> iterator = createSampleIterator();

    // Test initial state
    assertTrue(iterator.hasNext());
    assertEquals(0, (int) iterator.next());

    // Test idempotence of hasNext()
    assertTrue(iterator.hasNext());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.hasNext());
    assertEquals(1, (int) iterator.next());

    // Test end of data
    assertFalse(iterator.hasNext());
    assertFalse(iterator.hasNext());

    // Test exception on next() after end of data
    assertThrows(NoSuchElementException.class, iterator::next);
  }

  /**
   * Tests the default behavior of peek() method.
   */
  public void testDefaultBehaviorOfPeek() {
    AbstractIterator<Integer> iterator = createSampleIterator();

    // Test peek() without advancing
    assertEquals(0, (int) iterator.peek());
    assertEquals(0, (int) iterator.peek());
    assertTrue(iterator.hasNext());
    assertEquals(0, (int) iterator.peek());
    assertEquals(0, (int) iterator.next());

    // Test peek() after advancing
    assertEquals(1, (int) iterator.peek());
    assertEquals(1, (int) iterator.next());

    // Test exception on peek() after end of data
    assertThrows(NoSuchElementException.class, iterator::peek);
    assertThrows(NoSuchElementException.class, iterator::peek);
    assertThrows(NoSuchElementException.class, iterator::next);
    assertThrows(NoSuchElementException.class, iterator::peek);
  }

  /**
   * Tests that the iterator frees the reference to the next element.
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
   * Tests the default behavior of peek() for an empty iteration.
   */
  public void testDefaultBehaviorOfPeekForEmptyIteration() {
    AbstractIterator<Integer> emptyIterator = new AbstractIterator<Integer>() {
      private boolean alreadyCalledEndOfData;

      @Override
      public @Nullable Integer computeNext() {
        if (alreadyCalledEndOfData) {
          fail("Should not have been invoked again");
        }
        alreadyCalledEndOfData = true;
        return endOfData();
      }
    };

    // Test exception on peek() for empty iteration
    assertThrows(NoSuchElementException.class, emptyIterator::peek);
    assertThrows(NoSuchElementException.class, emptyIterator::peek);
  }

  /**
   * Tests sneakyThrow() method.
   */
  public void testSneakyThrow() throws Exception {
    Iterator<Integer> iterator = new AbstractIterator<Integer>() {
      boolean haveBeenCalled;

      @Override
      public Integer computeNext() {
        if (haveBeenCalled) {
          throw new AssertionError("Should not have been called again");
        } else {
          haveBeenCalled = true;
          throw sneakyThrow(new SomeCheckedException());
        }
      }
    };

    // Test sneaky exception
    assertThrows(SomeCheckedException.class, iterator::hasNext);
    // Test IllegalStateException after sneaky exception
    assertThrows(IllegalStateException.class, iterator::hasNext);
  }

  /**
   * Tests exception propagation.
   */
  public void testException() {
    SomeUncheckedException exception = new SomeUncheckedException();
    Iterator<Integer> iterator = new AbstractIterator<Integer>() {
      @Override
      public Integer computeNext() {
        throw exception;
      }
    };

    // Test exception propagation
    SomeUncheckedException e = assertThrows(SomeUncheckedException.class, iterator::hasNext);
    assertSame(exception, e);
  }

  /**
   * Tests exception after endOfData() is called.
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
   * Tests that remove() method is not supported.
   */
  @SuppressWarnings("DoNotCall")
  public void testCantRemove() {
    Iterator<Integer> iterator = new AbstractIterator<Integer>() {
      boolean haveBeenCalled;

      @Override
      public Integer computeNext() {
        if (haveBeenCalled) {
          endOfData();
        }
        haveBeenCalled = true;
        return 0;
      }
    };

    assertEquals(0, (int) iterator.next());

    // Test UnsupportedOperationException on remove()
    assertThrows(UnsupportedOperationException.class, iterator::remove);
  }

  /**
   * Tests reentrant hasNext() call.
   */
  public void testReentrantHasNext() {
    Iterator<Integer> iterator = new AbstractIterator<Integer>() {
      @Override
      protected Integer computeNext() {
        boolean unused = hasNext();
        throw new AssertionError();
      }
    };
    assertThrows(IllegalStateException.class, iterator::hasNext);
  }

  /**
   * Creates a sample AbstractIterator for testing.
   */
  private AbstractIterator<Integer> createSampleIterator() {
    return new AbstractIterator<Integer>() {
      private int state;

      @Override
      public @Nullable Integer computeNext() {
        switch (state++) {
          case 0:
            return 0;
          case 1:
            return 1;
          case 2:
            return endOfData();
          default:
            throw new AssertionError("Should not have been invoked again");
        }
      }
    };
  }
}
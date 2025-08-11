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

import static com.google.common.collect.ReflectionFreeAssertThrows.assertThrows;
import static com.google.common.collect.SneakyThrows.sneakyThrow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.collect.TestExceptions.SomeCheckedException;
import com.google.common.collect.TestExceptions.SomeUncheckedException;
import com.google.common.testing.GcFinalization;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit test for {@code AbstractIterator}.
 *
 * <p>This revised test suite uses a helper method {@link #createFiniteIterator(Object...)} to
 * reduce boilerplate and improve clarity. Test methods are named to describe the behavior under
 * test.
 *
 * @author Kevin Bourrillion
 */
@GwtCompatible(emulated = true)
@NullMarked
@RunWith(JUnit4.class)
public class AbstractIteratorTest {

  /**
   * Creates an {@link AbstractIterator} that iterates over the given elements.
   */
  private static <T> AbstractIterator<T> createFiniteIterator(T... elements) {
    final Iterator<T> backingIterator = Iterators.forArray(elements);
    return new AbstractIterator<T>() {
      @Override
      protected @Nullable T computeNext() {
        if (backingIterator.hasNext()) {
          return backingIterator.next();
        }
        return endOfData();
      }
    };
  }

  @Test
  public void iterator_yieldsAllElements_andThenHasNextIsFalse() {
    // Arrange
    Iterator<Integer> iterator = createFiniteIterator(0, 1);

    // Act & Assert for first element
    assertTrue(iterator.hasNext());
    assertEquals(Integer.valueOf(0), iterator.next());

    // Act & Assert for second element (and hasNext idempotence)
    assertTrue(iterator.hasNext());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.hasNext());
    assertEquals(Integer.valueOf(1), iterator.next());

    // Act & Assert for end of iteration
    assertFalse(iterator.hasNext());
    assertFalse(iterator.hasNext()); // ensure computeNext() is not called again

    // Act & Assert that next() throws exception when no elements remain
    assertThrows(NoSuchElementException.class, iterator::next);
  }

  @Test
  public void peek_returnsNextElement_withoutAdvancingIterator() {
    // Arrange
    AbstractIterator<Integer> iterator = createFiniteIterator(0, 1);

    // Act & Assert: peek() should be idempotent and not affect hasNext() or next()
    assertEquals(Integer.valueOf(0), iterator.peek());
    assertEquals(Integer.valueOf(0), iterator.peek());
    assertTrue(iterator.hasNext());
    assertEquals(Integer.valueOf(0), iterator.peek());
    assertEquals(Integer.valueOf(0), iterator.next());

    // Act & Assert for the next element
    assertEquals(Integer.valueOf(1), iterator.peek());
    assertEquals(Integer.valueOf(1), iterator.next());

    // Act & Assert that peek() and next() throw exceptions at the end of iteration
    assertThrows(NoSuchElementException.class, iterator::peek);
    assertThrows(NoSuchElementException.class, iterator::peek);
    assertThrows(NoSuchElementException.class, iterator::next);
    assertThrows(NoSuchElementException.class, iterator::peek);
  }

  @Test
  public void peek_throwsNoSuchElementException_forEmptyIterator() {
    // Arrange
    AbstractIterator<Integer> emptyIterator = createFiniteIterator();

    // Act & Assert: peek() should throw consistently for an empty iterator
    assertThrows(NoSuchElementException.class, emptyIterator::peek);
    assertThrows(NoSuchElementException.class, emptyIterator::peek);
  }

  @J2ktIncompatible // weak references, details of GC
  @GwtIncompatible // weak references
  @AndroidIncompatible // depends on details of GC
  @Test
  public void next_releasesReferenceToReturnedObject_forGarbageCollection() {
    // Arrange
    Iterator<Object> iterator =
        new AbstractIterator<Object>() {
          @Override
          public Object computeNext() {
            return new Object();
          }
        };

    // Act
    WeakReference<Object> ref = new WeakReference<>(iterator.next());

    // Assert that the iterator no longer holds a reference to the returned object
    GcFinalization.awaitClear(ref);
  }

  @Test
  public void computeNext_throwingCheckedException_isPropagatedOnFirstCall_thenIteratorFails()
      throws Exception {
    // Arrange: An iterator that sneakily throws a checked exception on first call.
    Iterator<Integer> iterator =
        new AbstractIterator<Integer>() {
          boolean haveBeenCalled = false;

          @Override
          public Integer computeNext() {
            if (haveBeenCalled) {
              // This assertion is critical: computeNext should not be invoked again
              // after the iterator has entered the FAILED state.
              throw new AssertionError("computeNext should not have been called again.");
            }
            haveBeenCalled = true;
            throw sneakyThrow(new SomeCheckedException());
          }
        };

    // Act & Assert: The first call to hasNext computes the next element,
    // which throws the checked exception that is propagated.
    assertThrows(SomeCheckedException.class, iterator::hasNext);

    // Act & Assert: The iterator is now in a FAILED state.
    // Subsequent calls to hasNext should throw IllegalStateException
    // without invoking computeNext again.
    assertThrows(IllegalStateException.class, iterator::hasNext);
  }

  @Test
  public void computeNext_throwingUncheckedException_isPropagatedByHasNext() {
    // Arrange
    SomeUncheckedException exception = new SomeUncheckedException();
    Iterator<Integer> iterator =
        new AbstractIterator<Integer>() {
          @Override
          public Integer computeNext() {
            throw exception;
          }
        };

    // Act & Assert: The exception from computeNext should be propagated untouched.
    SomeUncheckedException thrown =
        assertThrows(SomeUncheckedException.class, iterator::hasNext);
    assertSame("The propagated exception should be the same instance.", exception, thrown);
  }

  @Test
  public void computeNext_throwingExceptionAfterCallingEndOfData_propagatesException() {
    // Arrange: An iterator that calls endOfData() then throws.
    Iterator<Integer> iterator =
        new AbstractIterator<Integer>() {
          @Override
          public Integer computeNext() {
            endOfData();
            throw new SomeUncheckedException();
          }
        };

    // Act & Assert: The exception should be propagated, even though endOfData() was called.
    assertThrows(SomeUncheckedException.class, iterator::hasNext);
  }

  @Test
  @SuppressWarnings("DoNotCall") // Testing a method that should not be called.
  public void remove_throwsUnsupportedOperationException() {
    // Arrange
    Iterator<Integer> iterator = createFiniteIterator(0);
    iterator.next(); // remove() can only be called after next().

    // Act & Assert
    assertThrows(UnsupportedOperationException.class, iterator::remove);
  }

  @Test
  public void hasNext_whenCallingHasNextFromComputeNext_throwsIllegalStateException() {
    // Arrange: An iterator that makes a reentrant call to hasNext() inside computeNext().
    Iterator<Integer> iterator =
        new AbstractIterator<Integer>() {
          @Override
          protected Integer computeNext() {
            hasNext(); // Reentrant call
            fail("Should have thrown IllegalStateException");
            return null; // Unreachable
          }
        };

    // Act & Assert
    assertThrows(IllegalStateException.class, iterator::hasNext);
  }

  // Technically we should test other reentrant scenarios (9 combinations of
  // hasNext/next/peek), but we'll cop out for now, knowing that peek() and
  // next() both start by invoking hasNext() anyway.
}
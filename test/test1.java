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
}
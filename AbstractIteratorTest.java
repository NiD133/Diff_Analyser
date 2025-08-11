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
 *
 * @author Kevin Bourrillion
 */
@SuppressWarnings("serial") // No serialization is used in this test
@GwtCompatible(emulated = true)
@NullMarked
public class AbstractIteratorTest extends TestCase {

  // Reusable test iterator implementations
  private static class CountingIterator extends AbstractIterator<Integer> {
    private int count;
    private final int maxCount;

    CountingIterator(int maxCount) {
      this.maxCount = maxCount;
    }

    @Override
    public @Nullable Integer computeNext() {
      if (count < maxCount) {
        return count++;
      }
      return endOfData();
    }
  }

  private static class EmptyIterator extends AbstractIterator<Object> {
    @Override
    public @Nullable Object computeNext() {
      return endOfData();
    }
  }

  private abstract static class ExceptionThrowingIterator<T> extends AbstractIterator<T> {
    protected abstract void throwException() throws Exception;
    
    @Override
    public @Nullable T computeNext() {
      throwException();
      return endOfData();
    }
  }

  // Tests for core iteration behavior
  public void testNextAndHasNext_StandardIteration() {
    Iterator<Integer> iterator = new CountingIterator(2);
    
    // Initial state
    assertTrue("Iterator should have first element", iterator.hasNext());
    assertEquals("First element should be 0", 0, (int) iterator.next());

    // Verify hasNext() idempotence before second element
    assertTrue(iterator.hasNext());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.hasNext());
    assertEquals("Second element should be 1", 1, (int) iterator.next());

    // End of iteration
    assertFalse("Iterator should be exhausted", iterator.hasNext());
    assertFalse("hasNext() should remain false after exhaustion", iterator.hasNext());
    assertThrows(NoSuchElementException.class, iterator::next);
  }

  public void testPeek_AllStates() {
    AbstractIterator<Integer> iterator = new CountingIterator(2);
    
    // First element
    assertEquals("Peek should show first element", 0, (int) iterator.peek());
    assertEquals("Peek should remain consistent", 0, (int) iterator.peek());
    assertTrue(iterator.hasNext());
    assertEquals(0, (int) iterator.next());

    // Second element
    assertEquals("Peek should show second element", 1, (int) iterator.peek());
    assertEquals(1, (int) iterator.next());

    // Post-exhaustion
    assertThrows(NoSuchElementException.class, iterator::peek);
    assertThrows(NoSuchElementException.class, iterator::peek);
    assertThrows(NoSuchElementException.class, iterator::next);
    assertThrows(NoSuchElementException.class, iterator::peek);
  }

  public void testPeek_EmptyIterator() {
    AbstractIterator<Integer> emptyIterator = new EmptyIterator();
    
    assertThrows(NoSuchElementException.class, emptyIterator::peek);
    assertThrows(NoSuchElementException.class, emptyIterator::peek);
  }

  // Tests for exception handling
  public void testComputeNext_ThrowsCheckedException() {
    Iterator<Integer> iterator = new ExceptionThrowingIterator<Integer>() {
      @Override
      protected void throwException() {
        throw sneakyThrow(new SomeCheckedException());
      }
    };

    // First attempt should propagate checked exception
    assertThrows(SomeCheckedException.class, iterator::hasNext);
    
    // Subsequent attempts should show illegal state
    assertThrows(IllegalStateException.class, iterator::hasNext);
  }

  public void testComputeNext_ThrowsUncheckedException() {
    SomeUncheckedException expectedException = new SomeUncheckedException();
    Iterator<Integer> iterator = new ExceptionThrowingIterator<Integer>() {
      @Override
      protected void throwException() {
        throw expectedException;
      }
    };

    // Should propagate directly
    SomeUncheckedException actualException = 
        assertThrows(SomeUncheckedException.class, iterator::hasNext);
    assertSame("Exception should be propagated directly", 
        expectedException, actualException);
  }

  public void testComputeNext_ThrowsAfterEndOfData() {
    Iterator<Integer> iterator = new AbstractIterator<Integer>() {
      @Override
      public Integer computeNext() {
        endOfData();
        throw new SomeUncheckedException();
      }
    };
    
    assertThrows(SomeUncheckedException.class, iterator::hasNext);
  }

  // Tests for resource management and constraints
  @J2ktIncompatible // weak references, details of GC
  @GwtIncompatible // weak references
  @AndroidIncompatible // depends on details of GC
  public void testNextReference_GCReclaimable() {
    Iterator<Object> iterator = new AbstractIterator<Object>() {
      @Override
      public Object computeNext() {
        return new Object();
      }
    };
    
    WeakReference<Object> ref = new WeakReference<>(iterator.next());
    GcFinalization.awaitClear(ref);
  }

  public void testRemove_AlwaysUnsupported() {
    Iterator<Integer> iterator = new CountingIterator(1);
    iterator.next(); // Advance to element
    
    assertThrows(UnsupportedOperationException.class, iterator::remove);
  }

  public void testHasNext_ReentrantCallProhibited() {
    Iterator<Integer> iterator = new AbstractIterator<Integer>() {
      @Override
      protected Integer computeNext() {
        // Illegal reentrant call to hasNext()
        boolean unused = hasNext();
        throw new AssertionError("Should not get here");
      }
    };
    
    assertThrows(IllegalStateException.class, iterator::hasNext);
  }
}
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

  // Test basic iteration behavior
  
  public void testIteratorReturnsElementsInOrderAndStopsAtEnd() {
    Iterator<Integer> iterator = createIteratorThatReturns(0, 1);

    // First element
    assertTrue("Iterator should have first element", iterator.hasNext());
    assertEquals("First element should be 0", 0, (int) iterator.next());

    // Second element
    assertTrue("Iterator should have second element", iterator.hasNext());
    assertEquals("Second element should be 1", 1, (int) iterator.next());

    // No more elements
    assertFalse("Iterator should be exhausted", iterator.hasNext());
    assertThrows("next() should throw when exhausted", 
        NoSuchElementException.class, iterator::next);
  }

  public void testHasNextIsIdempotent() {
    Iterator<Integer> iterator = createIteratorThatReturns(0, 1);

    // Multiple calls to hasNext() should not advance the iterator
    assertTrue(iterator.hasNext());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.hasNext());
    
    assertEquals("next() should still return first element", 0, (int) iterator.next());
  }

  public void testComputeNextNotCalledAfterEndOfData() {
    Iterator<Integer> iterator = createIteratorWithInvocationTracking();

    // Consume all elements
    assertTrue(iterator.hasNext());
    iterator.next();
    assertTrue(iterator.hasNext());
    iterator.next();
    assertFalse(iterator.hasNext());

    // Additional calls should not invoke computeNext()
    assertFalse("hasNext() should not invoke computeNext() after end", iterator.hasNext());
    assertThrows(NoSuchElementException.class, iterator::next);
  }

  // Test peek() behavior
  
  public void testPeekReturnsNextElementWithoutAdvancing() {
    AbstractIterator<Integer> iterator = createAbstractIteratorThatReturns(0, 1);

    // Peek at first element multiple times
    assertEquals("peek() should return first element", 0, (int) iterator.peek());
    assertEquals("peek() should still return first element", 0, (int) iterator.peek());
    assertTrue("hasNext() should still be true", iterator.hasNext());
    assertEquals("peek() should still return first element", 0, (int) iterator.peek());
    
    // Advance to first element
    assertEquals("next() should return first element", 0, (int) iterator.next());

    // Peek at second element
    assertEquals("peek() should return second element", 1, (int) iterator.peek());
    assertEquals("next() should return second element", 1, (int) iterator.next());
  }

  public void testPeekThrowsWhenIteratorExhausted() {
    AbstractIterator<Integer> iterator = createAbstractIteratorThatReturns(0, 1);

    // Consume all elements
    iterator.next();
    iterator.next();

    // peek() should throw consistently when exhausted
    assertThrows("peek() should throw when exhausted", 
        NoSuchElementException.class, iterator::peek);
    assertThrows("peek() should continue throwing when exhausted", 
        NoSuchElementException.class, iterator::peek);
    assertThrows("next() should also throw when exhausted", 
        NoSuchElementException.class, iterator::next);
    assertThrows("peek() should still throw after next() threw", 
        NoSuchElementException.class, iterator::peek);
  }

  public void testPeekThrowsForEmptyIterator() {
    AbstractIterator<Integer> emptyIterator = createEmptyIterator();

    // Multiple calls should all throw
    assertThrows("peek() should throw for empty iterator", 
        NoSuchElementException.class, emptyIterator::peek);
    assertThrows("peek() should continue throwing for empty iterator", 
        NoSuchElementException.class, emptyIterator::peek);
  }

  // Test memory management
  
  @J2ktIncompatible // weak references, details of GC
  @GwtIncompatible // weak references
  @AndroidIncompatible // depends on details of GC
  public void testIteratorDoesNotRetainReferencesToReturnedElements() {
    Iterator<Object> iterator = createInfiniteObjectIterator();
    
    Object firstElement = iterator.next();
    WeakReference<Object> weakRef = new WeakReference<>(firstElement);
    firstElement = null; // Clear strong reference
    
    GcFinalization.awaitClear(weakRef);
  }

  // Test exception handling
  
  public void testCheckedExceptionPropagatesThroughSneakyThrow() throws Exception {
    Iterator<Integer> iterator = createIteratorThatThrowsCheckedException();

    // First call propagates the checked exception
    assertThrows("Checked exception should propagate", 
        SomeCheckedException.class, iterator::hasNext);
    
    // Subsequent calls throw IllegalStateException
    assertThrows("Should throw IllegalStateException after exception", 
        IllegalStateException.class, iterator::hasNext);
  }

  public void testUncheckedExceptionPropagatesUnchanged() {
    SomeUncheckedException expectedException = new SomeUncheckedException();
    Iterator<Integer> iterator = createIteratorThatThrows(expectedException);

    // Exception should pass through untouched
    SomeUncheckedException actualException = assertThrows(
        "Unchecked exception should propagate", 
        SomeUncheckedException.class, iterator::hasNext);
    assertSame("Should be the same exception instance", expectedException, actualException);
  }

  public void testExceptionThrownAfterEndOfDataCall() {
    Iterator<Integer> iterator = new AbstractIterator<Integer>() {
      @Override
      public Integer computeNext() {
        endOfData();
        throw new SomeUncheckedException();
      }
    };
    
    assertThrows("Exception after endOfData() should propagate", 
        SomeUncheckedException.class, iterator::hasNext);
  }

  // Test unsupported operations
  
  @SuppressWarnings("DoNotCall")
  public void testRemoveIsNotSupported() {
    Iterator<Integer> iterator = createIteratorThatReturns(0);
    
    iterator.next();
    
    assertThrows("remove() should not be supported", 
        UnsupportedOperationException.class, iterator::remove);
  }

  // Test reentrancy protection
  
  public void testReentrantCallToHasNextThrowsIllegalStateException() {
    Iterator<Integer> reentrantIterator = new AbstractIterator<Integer>() {
      @Override
      protected Integer computeNext() {
        // Attempting to call hasNext() from within computeNext()
        boolean unused = hasNext();
        throw new AssertionError("Should not reach here");
      }
    };
    
    assertThrows("Reentrant call should throw IllegalStateException", 
        IllegalStateException.class, reentrantIterator::hasNext);
  }

  // Helper methods for creating test iterators
  
  private static Iterator<Integer> createIteratorThatReturns(Integer... values) {
    return new AbstractIterator<Integer>() {
      private int index = 0;

      @Override
      public @Nullable Integer computeNext() {
        if (index < values.length) {
          return values[index++];
        }
        return endOfData();
      }
    };
  }

  private static AbstractIterator<Integer> createAbstractIteratorThatReturns(Integer... values) {
    return new AbstractIterator<Integer>() {
      private int index = 0;

      @Override
      public @Nullable Integer computeNext() {
        if (index < values.length) {
          return values[index++];
        }
        return endOfData();
      }
    };
  }

  private static Iterator<Integer> createIteratorWithInvocationTracking() {
    return new AbstractIterator<Integer>() {
      private int invocationCount = 0;

      @Override
      public @Nullable Integer computeNext() {
        switch (invocationCount++) {
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
    };
  }

  private static AbstractIterator<Integer> createEmptyIterator() {
    return new AbstractIterator<Integer>() {
      private boolean hasBeenCalled = false;

      @Override
      public @Nullable Integer computeNext() {
        if (hasBeenCalled) {
          fail("computeNext() should only be called once for empty iterator");
        }
        hasBeenCalled = true;
        return endOfData();
      }
    };
  }

  private static Iterator<Object> createInfiniteObjectIterator() {
    return new AbstractIterator<Object>() {
      @Override
      public Object computeNext() {
        return new Object();
      }
    };
  }

  private static Iterator<Integer> createIteratorThatThrowsCheckedException() {
    return new AbstractIterator<Integer>() {
      private boolean hasThrown = false;

      @Override
      public Integer computeNext() {
        if (hasThrown) {
          throw new AssertionError("Should not be called after exception");
        }
        hasThrown = true;
        throw sneakyThrow(new SomeCheckedException());
      }
    };
  }

  private static Iterator<Integer> createIteratorThatThrows(RuntimeException exception) {
    return new AbstractIterator<Integer>() {
      @Override
      public Integer computeNext() {
        throw exception;
      }
    };
  }
}
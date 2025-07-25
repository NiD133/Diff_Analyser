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
import com.google.common.collect.AbstractIteratorTest.SampleAbstractIterator;
import com.google.common.testing.GcFinalization;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.NoSuchElementException;
import junit.framework.TestCase;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Unit tests for {@link AbstractIterator}.
 *
 * <p>This test class covers various scenarios to ensure the correct behavior of {@link AbstractIterator}.
 *
 * @author Kevin Bourrillion
 */
@SuppressWarnings("serial") // No serialization is used in this test
@GwtCompatible(emulated = true)
@NullMarked
public class AbstractIteratorTest extends TestCase {

  /**
   * Tests the default behavior of {@link AbstractIterator#hasNext()} and {@link AbstractIterator#next()}.
   */
  public void testDefaultBehavior() {
    // Create a sample AbstractIterator that returns 0 on the first call, 1 on the second, and then signals the end of data
    SampleAbstractIterator iter = new SampleAbstractIterator(0, 1);

    // Verify that hasNext() returns true initially
    assertTrue(iter.hasNext());

    // Verify that next() returns the expected values
    assertEquals(0, iter.next().intValue());
    assertEquals(1, iter.next().intValue());

    // Verify that hasNext() returns false after the end of data
    assertFalse(iter.hasNext());

    // Verify that next() throws NoSuchElementException after the end of data
    assertThrows(NoSuchElementException.class, iter::next);
  }

  /**
   * Tests the behavior of {@link AbstractIterator#peek()}.
   */
  public void testPeek() {
    // Create a sample AbstractIterator that returns 0 on the first call, 1 on the second, and then signals the end of data
    SampleAbstractIterator iter = new SampleAbstractIterator(0, 1);

    // Verify that peek() returns the expected values without advancing the iteration
    assertEquals(0, iter.peek().intValue());
    assertEquals(0, iter.peek().intValue());

    // Verify that hasNext() returns true after peek()
    assertTrue(iter.hasNext());

    // Verify that next() returns the expected values after peek()
    assertEquals(0, iter.next().intValue());
    assertEquals(1, iter.next().intValue());

    // Verify that peek() throws NoSuchElementException after the end of data
    assertThrows(NoSuchElementException.class, iter::peek);
  }

  /**
   * Tests that the next reference is freed after the end of data.
   *
   * @throws Exception if an error occurs during the test
   */
  @J2ktIncompatible // weak references, details of GC
  @GwtIncompatible // weak references
  @AndroidIncompatible // depends on details of GC
  public void testFreesNextReference() throws Exception {
    // Create an AbstractIterator that returns a new object on each call
    SampleAbstractIterator itr = new SampleAbstractIterator();

    // Create a weak reference to the next object
    WeakReference<Object> ref = new WeakReference<>(itr.next());

    // Wait for the weak reference to be cleared
    GcFinalization.awaitClear(ref);
  }

  /**
   * Tests the behavior of {@link AbstractIterator#peek()} for an empty iteration.
   */
  public void testEmptyPeek() {
    // Create an empty AbstractIterator
    SampleAbstractIterator empty = new SampleAbstractIterator();

    // Verify that peek() throws NoSuchElementException
    assertThrows(NoSuchElementException.class, empty::peek);
  }

  /**
   * Tests the behavior of {@link SneakyThrows#sneakyThrow(Exception)}.
   *
   * @throws Exception if an error occurs during the test
   */
  public void testSneakyThrow() throws Exception {
    // Create an AbstractIterator that throws a checked exception using sneakyThrow
    SampleAbstractIterator iter = new SampleAbstractIterator() {
      @Override
      protected Integer computeNext() {
        throw sneakyThrow(new SomeCheckedException());
      }
    };

    // Verify that hasNext() throws the checked exception
    assertThrows(SomeCheckedException.class, iter::hasNext);

    // Verify that hasNext() throws IllegalStateException on subsequent calls
    assertThrows(IllegalStateException.class, iter::hasNext);
  }

  /**
   * Tests the behavior of {@link AbstractIterator} when an exception is thrown.
   */
  public void testException() {
    // Create an AbstractIterator that throws an unchecked exception
    SampleAbstractIterator iter = new SampleAbstractIterator() {
      @Override
      protected Integer computeNext() {
        throw new SomeUncheckedException();
      }
    };

    // Verify that hasNext() throws the unchecked exception
    assertThrows(SomeUncheckedException.class, iter::hasNext);
  }

  /**
   * Tests the behavior of {@link AbstractIterator} when an exception is thrown after the end of data.
   */
  public void testExceptionAfterEndOfData() {
    // Create an AbstractIterator that throws an exception after signaling the end of data
    SampleAbstractIterator iter = new SampleAbstractIterator() {
      @Override
      protected Integer computeNext() {
        endOfData();
        throw new SomeUncheckedException();
      }
    };

    // Verify that hasNext() throws the unchecked exception
    assertThrows(SomeUncheckedException.class, iter::hasNext);
  }

  /**
   * Tests that {@link AbstractIterator#remove()} is not supported.
   */
  @SuppressWarnings("DoNotCall")
  public void testRemove() {
    // Create a sample AbstractIterator
    SampleAbstractIterator iter = new SampleAbstractIterator(0);

    // Verify that remove() throws UnsupportedOperationException
    assertThrows(UnsupportedOperationException.class, iter::remove);
  }

  /**
   * Tests the behavior of {@link AbstractIterator} when {@link AbstractIterator#hasNext()} is called reentrantly.
   */
  public void testReentrantHasNext() {
    // Create an AbstractIterator that calls hasNext() reentrantly
    SampleAbstractIterator iter = new SampleAbstractIterator() {
      @Override
      protected Integer computeNext() {
        hasNext();
        throw new AssertionError();
      }
    };

    // Verify that hasNext() throws IllegalStateException
    assertThrows(IllegalStateException.class, iter::hasNext);
  }

  // Helper class to create sample AbstractIterators
  private static class SampleAbstractIterator extends AbstractIterator<Integer> {
    private int value;
    private int count;

    public SampleAbstractIterator(int... values) {
      this.value = 0;
      this.count = values.length;
      if (count > 0) {
        this.value = values[0];
      }
    }

    @Override
    protected Integer computeNext() {
      if (count > 0) {
        int result = value;
        if (count > 1) {
          value = 1;
        } else {
          endOfData();
        }
        count--;
        return result;
      }
      return endOfData();
    }
  }
}
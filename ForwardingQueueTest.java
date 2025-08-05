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

import static java.util.Arrays.asList;

import com.google.common.collect.testing.QueueTestSuiteBuilder;
import com.google.common.collect.testing.TestStringQueueGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.testing.ForwardingWrapperTester;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;

/**
 * Tests for {@link ForwardingQueue}.
 *
 * <p>This test class employs two primary strategies:
 *
 * <ol>
 *   <li><b>Forwarding Verification</b>: The {@link #testForwarding()} method uses {@link
 *       ForwardingWrapperTester} to ensure that a simple {@code ForwardingQueue} instance correctly
 *       delegates all of its methods to the backing queue.
 *   <li><b>Standard Method Implementation Testing</b>: The {@link #suite()} method uses {@code
 *       guava-testlib} to run a comprehensive suite of tests against {@link
 *       StandardImplForwardingQueue}. This special implementation overrides every method to call
 *       the corresponding {@code standard*} implementation (e.g., {@link #standardAddAll(Collection)}),
 *       thereby testing the correctness of these provided helper methods.
 * </ol>
 *
 * @author Robert Konigsberg
 * @author Louis Wasserman
 */
@NullUnmarked
public class ForwardingQueueTest extends TestCase {

  /**
   * A custom {@link ForwardingQueue} that overrides all public methods to delegate to the
   * corresponding {@code standard*} implementations. This class is used to verify the correctness
   * of the standard implementations provided by {@link ForwardingQueue}.
   */
  static final class StandardImplForwardingQueue<T> extends ForwardingQueue<T> {
    private final Queue<T> backingQueue;

    StandardImplForwardingQueue(Queue<T> backingQueue) {
      this.backingQueue = backingQueue;
    }

    @Override
    protected Queue<T> delegate() {
      return backingQueue;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
      return standardAddAll(collection);
    }

    @Override
    public void clear() {
      standardClear();
    }

    @Override
    public boolean contains(Object object) {
      return standardContains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
      return standardContainsAll(collection);
    }

    @Override
    public boolean remove(Object object) {
      return standardRemove(object);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
      return standardRemoveAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
      return standardRetainAll(collection);
    }



    @Override
    public Object[] toArray() {
      return standardToArray();
    }

    @Override
    public <T> T[] toArray(T[] array) {
      return standardToArray(array);
    }

    @Override
    public String toString() {
      return standardToString();
    }

    @Override
    public boolean offer(T o) {
      return standardOffer(o);
    }

    @Override
    public @Nullable T peek() {
      return standardPeek();
    }

    @Override
    public @Nullable T poll() {
      return standardPoll();
    }
  }

  @AndroidIncompatible // test-suite builders
  public static Test suite() {
    TestSuite suite = new TestSuite();

    // 1. Test that a simple ForwardingQueue delegates all methods.
    suite.addTestSuite(ForwardingQueueTest.class);

    // 2. Test the standard* implementations using guava-testlib.
    suite.addTest(
        QueueTestSuiteBuilder.using(createStandardQueueGenerator())
            .named("ForwardingQueue[LinkedList] with standard implementations")
            .withFeatures(
                CollectionSize.ANY,
                CollectionFeature.ALLOWS_NULL_VALUES,
                CollectionFeature.GENERAL_PURPOSE)
            .createTestSuite());

    return suite;
  }

  /**
   * Creates a generator for {@link StandardImplForwardingQueue} instances, used by the
   * guava-testlib suite.
   */
  private static TestStringQueueGenerator createStandardQueueGenerator() {
    return new TestStringQueueGenerator() {
      @Override
      protected Queue<String> create(String[] elements) {
        return new StandardImplForwardingQueue<>(new LinkedList<>(asList(elements)));
      }
    };
  }

  /**
   * Tests that a basic {@link ForwardingQueue} wrapper correctly forwards all interface methods to
   * the delegate, using {@link ForwardingWrapperTester}.
   */
  public void testForwarding() {
    // ForwardingWrapperTester requires raw types for its arguments.
    @SuppressWarnings({"rawtypes", "unchecked"})
    Function<Queue, Queue> wrapperFunction = ForwardingQueueTest::wrap;

    new ForwardingWrapperTester().testForwarding(Queue.class, wrapperFunction);
  }

  /** Wraps a queue in a basic {@link ForwardingQueue} that strictly delegates all calls. */
  private static <T> Queue<T> wrap(Queue<T> delegate) {
    return new ForwardingQueue<T>() {
      @Override
      protected Queue<T> delegate() {
        return delegate;
      }
    };
  }
}
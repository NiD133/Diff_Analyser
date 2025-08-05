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

import com.google.common.base.Function;
import com.google.common.collect.testing.QueueTestSuiteBuilder;
import com.google.common.collect.testing.TestStringQueueGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.testing.ForwardingWrapperTester;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;

/**
 * Tests for {@code ForwardingQueue}.
 *
 * <p>This test suite verifies that ForwardingQueue properly delegates method calls
 * and that the standard implementations work correctly.
 *
 * @author Robert Konigsberg
 * @author Louis Wasserman
 */
@NullUnmarked
public class ForwardingQueueTest extends TestCase {

  /**
   * A test implementation of ForwardingQueue that uses all the standard method implementations.
   * This helps verify that the standard implementations provided by ForwardingQueue work correctly.
   */
  static final class QueueWithStandardImplementations<T> extends ForwardingQueue<T> {
    private final Queue<T> backingQueue;

    QueueWithStandardImplementations(Queue<T> backingQueue) {
      this.backingQueue = backingQueue;
    }

    @Override
    protected Queue<T> delegate() {
      return backingQueue;
    }

    // Collection methods using standard implementations
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

    // Queue methods using standard implementations
    @Override
    public boolean offer(T element) {
      return standardOffer(element);
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

    // Add unit tests from this class
    suite.addTestSuite(ForwardingQueueTest.class);
    
    // Add comprehensive Queue contract tests using a ForwardingQueue with standard implementations
    suite.addTest(createStandardImplementationTests());

    return suite;
  }

  /**
   * Creates a comprehensive test suite that verifies ForwardingQueue with standard implementations
   * behaves correctly as a Queue by testing it against the full Queue contract.
   */
  private static Test createStandardImplementationTests() {
    return QueueTestSuiteBuilder.using(new StringQueueGeneratorWithStandardImplementations())
        .named("ForwardingQueue[LinkedList] with standard implementations")
        .withFeatures(
            CollectionSize.ANY,
            CollectionFeature.ALLOWS_NULL_VALUES,
            CollectionFeature.GENERAL_PURPOSE)
        .createTestSuite();
  }

  /**
   * Generator that creates ForwardingQueue instances with standard implementations
   * backed by LinkedList for testing purposes.
   */
  private static class StringQueueGeneratorWithStandardImplementations extends TestStringQueueGenerator {
    @Override
    protected Queue<String> create(String[] elements) {
      Queue<String> backingQueue = new LinkedList<>(asList(elements));
      return new QueueWithStandardImplementations<>(backingQueue);
    }
  }

  /**
   * Tests that ForwardingQueue properly forwards all method calls to its delegate.
   * This uses ForwardingWrapperTester to verify that every Queue method is correctly forwarded.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void testMethodForwardingBehavior() {
    new ForwardingWrapperTester()
        .testForwarding(Queue.class, new QueueWrapperFunction());
  }

  /**
   * Function that wraps a Queue in a basic ForwardingQueue for forwarding tests.
   */
  private static class QueueWrapperFunction implements Function<Queue, Queue> {
    @Override
    public Queue apply(Queue delegate) {
      return createBasicForwardingQueue(delegate);
    }
  }

  /**
   * Creates a basic ForwardingQueue that simply forwards all calls to the delegate
   * without using any standard implementations.
   */
  private static <T> Queue<T> createBasicForwardingQueue(Queue<T> delegate) {
    return new ForwardingQueue<T>() {
      @Override
      protected Queue<T> delegate() {
        return delegate;
      }
    };
  }
}
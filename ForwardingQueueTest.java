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
 * Unit tests for the {@code ForwardingQueue} class.
 * These tests ensure that the forwarding behavior works as expected.
 * 
 * Authors: Robert Konigsberg, Louis Wasserman
 */
@NullUnmarked
public class ForwardingQueueTest extends TestCase {

  /**
   * A concrete implementation of ForwardingQueue that uses a standard Queue as its delegate.
   * This class is used to test the standard forwarding methods.
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

    // Override methods to use standard implementations
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

  /**
   * Builds and returns a test suite that includes all tests for ForwardingQueue.
   * 
   * @return a TestSuite containing all ForwardingQueue tests.
   */
  @AndroidIncompatible // test-suite builders
  public static Test suite() {
    TestSuite suite = new TestSuite();

    // Add the ForwardingQueueTest class to the suite
    suite.addTestSuite(ForwardingQueueTest.class);

    // Add a test suite for ForwardingQueue using QueueTestSuiteBuilder
    suite.addTest(
        QueueTestSuiteBuilder.using(
                new TestStringQueueGenerator() {
                  @Override
                  protected Queue<String> create(String[] elements) {
                    return new StandardImplForwardingQueue<>(new LinkedList<>(asList(elements)));
                  }
                })
            .named("ForwardingQueue[LinkedList] with standard implementations")
            .withFeatures(
                CollectionSize.ANY,
                CollectionFeature.ALLOWS_NULL_VALUES,
                CollectionFeature.GENERAL_PURPOSE)
            .createTestSuite());

    return suite;
  }

  /**
   * Tests the forwarding functionality of the ForwardingQueue.
   * Uses ForwardingWrapperTester to ensure that method calls are correctly forwarded.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void testForwarding() {
    new ForwardingWrapperTester()
        .testForwarding(
            Queue.class,
            new Function<Queue, Queue>() {
              @Override
              public Queue apply(Queue delegate) {
                return wrap(delegate);
              }
            });
  }

  /**
   * Wraps a given Queue with a ForwardingQueue.
   * This helper method is used in forwarding tests.
   * 
   * @param delegate the Queue to be wrapped
   * @return a ForwardingQueue that forwards to the given Queue
   */
  private static <T> Queue<T> wrap(Queue<T> delegate) {
    return new ForwardingQueue<T>() {
      @Override
      protected Queue<T> delegate() {
        return delegate;
      }
    };
  }
}
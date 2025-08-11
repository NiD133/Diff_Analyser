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
 * Tests for ForwardingQueue.
 *
 * This file focuses on:
 * - Verifying that a simple ForwardingQueue truly forwards all calls to its delegate.
 * - Verifying the behavior of the provided "standard*" convenience methods via a concrete subclass.
 */
@NullUnmarked
public class ForwardingQueueTest extends TestCase {

  /**
   * A simple ForwardingQueue that explicitly opts into the "standard*" implementations.
   *
   * This class is used to ensure the standard* helpers behave as documented when a subclass
   * chooses to adopt them.
   */
  static final class StandardForwardingQueue<E> extends ForwardingQueue<E> {
    private final Queue<E> delegate;

    StandardForwardingQueue(Queue<E> delegate) {
      this.delegate = delegate;
    }

    @Override
    protected Queue<E> delegate() {
      return delegate;
    }

    // Collection "standard*" implementations

    @Override
    public boolean addAll(Collection<? extends E> collection) {
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

    // Note: The type parameter <T> here intentionally shadows the class's <E>.
    // This mirrors the signature in Collection and is conventional for toArray.
    @Override
    public <T> T[] toArray(T[] array) {
      return standardToArray(array);
    }

    @Override
    public String toString() {
      return standardToString();
    }

    // Queue-specific "standard*" implementations

    @Override
    public boolean offer(E e) {
      return standardOffer(e);
    }

    @Override
    public @Nullable E peek() {
      return standardPeek();
    }

    @Override
    public @Nullable E poll() {
      return standardPoll();
    }
  }

  /**
   * Builds a comprehensive suite that includes:
   * - This file's direct tests, and
   * - Generated tests that exercise the standard* implementations via a backing LinkedList.
   */
  @AndroidIncompatible // test-suite builders
  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ForwardingQueueTest.class);
    suite.addTest(buildStandardImplSuite());
    return suite;
  }

  private static Test buildStandardImplSuite() {
    return QueueTestSuiteBuilder.using(new LinkedListStringQueueGenerator())
        .named("ForwardingQueue[LinkedList] with standard implementations")
        .withFeatures(
            CollectionSize.ANY,
            CollectionFeature.ALLOWS_NULL_VALUES,
            CollectionFeature.GENERAL_PURPOSE)
        .createTestSuite();
  }

  /**
   * Generator that produces a ForwardingQueue wrapping a LinkedList, opting into the
   * standard* implementations for queue and collection operations.
   */
  private static final class LinkedListStringQueueGenerator extends TestStringQueueGenerator {
    @Override
    protected Queue<String> create(String[] elements) {
      return new StandardForwardingQueue<>(new LinkedList<>(asList(elements)));
    }
  }

  /**
   * Verifies that a minimal ForwardingQueue truly forwards every method to its delegate.
   */
  public void testForwarding_delegatesAllMethods() {
    new ForwardingWrapperTester()
        .testForwarding(
            Queue.class,
            (Function<Queue<Object>, Queue<Object>>) ForwardingQueueTest::wrap);
  }

  /**
   * Returns a ForwardingQueue that delegates directly to the provided delegate.
   * This is the minimal forwarding wrapper (no behavior changes).
   */
  private static <E> Queue<E> wrap(Queue<E> delegate) {
    return new ForwardingQueue<E>() {
      @Override
      protected Queue<E> delegate() {
        return delegate;
      }
    };
  }
}
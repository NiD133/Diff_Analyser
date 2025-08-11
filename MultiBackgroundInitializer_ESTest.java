package org.apache.commons.lang3.concurrent;

import org.junit.Test;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for MultiBackgroundInitializer.
 *
 * These tests favor:
 * - clear Arrange / Act / Assert structure
 * - descriptive test names
 * - minimal use of randomness or unrelated API calls
 * - typical public usage patterns (start() + get()) over calling protected methods directly
 */
public class MultiBackgroundInitializerTest {

    @Test
    public void noChildren_resultsAreSuccessfulAndEmpty() throws Exception {
        // Arrange
        MultiBackgroundInitializer mbi = new MultiBackgroundInitializer();

        // Act
        mbi.start();
        MultiBackgroundInitializer.MultiBackgroundInitializerResults results = mbi.get();

        // Assert
        assertTrue(results.isSuccessful());
        assertTrue(results.initializerNames().isEmpty());
    }

    @Test
    public void addInitializer_afterStart_throwsIllegalStateException() throws Exception {
        // Arrange
        MultiBackgroundInitializer mbi = new MultiBackgroundInitializer();
        ReturningInitializer<String> child = new ReturningInitializer<>("ok");

        // Act
        mbi.start();

        // Assert
        try {
            mbi.addInitializer("child", child);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // addInitializer() must not be called after start()
        }
    }

    @Test
    public void addInitializer_nullArguments_throwNullPointerException() {
        // Arrange
        MultiBackgroundInitializer mbi = new MultiBackgroundInitializer();
        ReturningInitializer<String> child = new ReturningInitializer<>("ok");

        // Assert
        try {
            mbi.addInitializer(null, child);
            fail("Expected NullPointerException for null name");
        } catch (NullPointerException expected) {
            // name must not be null
        }

        try {
            mbi.addInitializer("child", null);
            fail("Expected NullPointerException for null initializer");
        } catch (NullPointerException expected) {
            // initializer must not be null
        }
    }

    @Test
    public void successfulChild_resultAndMetadataAreAccessible() throws Exception {
        // Arrange
        ReturningInitializer<String> child = new ReturningInitializer<>("value");
        MultiBackgroundInitializer mbi = new MultiBackgroundInitializer();
        mbi.addInitializer("child", child);

        // Act
        mbi.start();
        MultiBackgroundInitializer.MultiBackgroundInitializerResults results = mbi.get();

        // Assert - Result object and metadata
        assertTrue(results.isSuccessful());
        assertEquals(Collections.singleton("child"), results.initializerNames());
        assertSame(child, results.getInitializer("child"));
        assertEquals("value", results.getResultObject("child"));
        assertNull(results.getException("child"));
        assertFalse(results.isException("child"));

        // Assert - Parent reports initialized once done
        assertTrue(mbi.isInitialized());
    }

    @Test
    public void unknownOrNullNames_throwNoSuchElementException() throws Exception {
        // Arrange
        MultiBackgroundInitializer mbi = new MultiBackgroundInitializer();

        // Act
        mbi.start();
        MultiBackgroundInitializer.MultiBackgroundInitializerResults results = mbi.get();

        // Assert
        assertThrowsNoSuchElement(() -> results.getInitializer("missing"));
        assertThrowsNoSuchElement(() -> results.getException("missing"));
        assertThrowsNoSuchElement(() -> results.getResultObject("missing"));
        assertThrowsNoSuchElement(() -> results.isException("missing"));

        assertThrowsNoSuchElement(() -> results.getInitializer(null));
        assertThrowsNoSuchElement(() -> results.getException(null));
        assertThrowsNoSuchElement(() -> results.getResultObject(null));
        assertThrowsNoSuchElement(() -> results.isException(null));
    }

    @Test
    public void childThatThrowsCheckedException_isCapturedInResults() throws Exception {
        // Arrange
        Exception boom = new Exception("boom");
        ThrowingInitializer child = new ThrowingInitializer(boom);
        MultiBackgroundInitializer mbi = new MultiBackgroundInitializer();
        mbi.addInitializer("child", child);

        // Act
        mbi.start();
        MultiBackgroundInitializer.MultiBackgroundInitializerResults results = mbi.get();

        // Assert
        assertFalse(results.isSuccessful());
        assertTrue(results.isException("child"));
        assertNotNull(results.getException("child"));
        assertNull(results.getResultObject("child"));
    }

    @Test
    public void getTaskCount_includesChildrenAndNestedMultiBackgroundInitializer() {
        // Arrange
        MultiBackgroundInitializer parent = new MultiBackgroundInitializer();
        assertEquals("Only control task expected", 1, parent.getTaskCount());

        // Add a simple child (1 task)
        parent.addInitializer("a", new ReturningInitializer<>("a"));
        assertEquals("Control + 1 child", 2, parent.getTaskCount());

        // Add a nested MBI with 1 child (nested: 1 control + 1 child = 2 tasks)
        MultiBackgroundInitializer nested = new MultiBackgroundInitializer();
        nested.addInitializer("nestedChild", new ReturningInitializer<>("nc"));
        parent.addInitializer("nested", nested);

        // Parent: 1 (control) + 1 (child a) + 2 (nested) = 4
        assertEquals(4, parent.getTaskCount());
    }

    @Test
    public void externalExecutor_isNotShutDownByInitializer() throws Exception {
        // Arrange
        ScheduledThreadPoolExecutor external = new ScheduledThreadPoolExecutor(1);
        try {
            MultiBackgroundInitializer mbi = new MultiBackgroundInitializer(external);
            mbi.addInitializer("child", new ReturningInitializer<>("ok"));

            // Act
            mbi.start();
            MultiBackgroundInitializer.MultiBackgroundInitializerResults results = mbi.get();

            // Assert
            assertTrue(results.isSuccessful());
            assertFalse("External executor should not be shutdown by MBI", external.isShutdown());
        } finally {
            external.shutdownNow();
        }
    }

    @Test
    public void close_closesChildInitializers() throws Exception {
        // Arrange
        ReturningInitializer<String> closableChild = new ReturningInitializer<>("ok");
        MultiBackgroundInitializer mbi = new MultiBackgroundInitializer();
        mbi.addInitializer("child", closableChild);

        mbi.start();
        mbi.get(); // ensure it has run

        // Act
        mbi.close();

        // Assert
        assertTrue("Child initializer should have been closed", closableChild.isClosed());
    }

    @Test
    public void close_propagatesChildCloseExceptions() throws Exception {
        // Arrange
        ReturningInitializer<String> failingOnClose = new ReturningInitializer<>("ok")
                .withCloseException(new ConcurrentException(new IllegalStateException("close failed")));
        MultiBackgroundInitializer mbi = new MultiBackgroundInitializer();
        mbi.addInitializer("child", failingOnClose);

        mbi.start();
        mbi.get();

        // Act / Assert
        try {
            mbi.close();
            fail("Expected ConcurrentException from child close");
        } catch (ConcurrentException expected) {
            // expected
        }
    }

    @Test
    public void get_withoutStart_throwsIllegalStateException() {
        // Arrange
        MultiBackgroundInitializer mbi = new MultiBackgroundInitializer();

        // Act / Assert
        try {
            mbi.get();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // start() must be called first
        }
    }

    /*
     * Helpers
     */

    private interface ThrowingAction {
        void run() throws Exception;
    }

    private static void assertThrowsNoSuchElement(ThrowingAction action) {
        try {
            action.run();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException expected) {
            // expected
        } catch (Exception other) {
            fail("Expected NoSuchElementException but got: " + other);
        }
    }

    /**
     * A BackgroundInitializer that returns a constant value.
     * Allows controlling behavior of close() for testing.
     */
    private static class ReturningInitializer<T> extends BackgroundInitializer<T> {
        private final T value;
        private volatile boolean closed;
        private ConcurrentException closeException;

        ReturningInitializer(T value) {
            this.value = value;
        }

        ReturningInitializer<T> withCloseException(ConcurrentException ex) {
            this.closeException = ex;
            return this;
        }

        boolean isClosed() {
            return closed;
        }

        @Override
        protected T initialize() {
            return value;
        }

        @Override
        public void close() throws ConcurrentException {
            if (closeException != null) {
                throw closeException;
            }
            closed = true;
        }
    }

    /**
     * A BackgroundInitializer that throws the given checked Exception from initialize().
     */
    private static class ThrowingInitializer extends BackgroundInitializer<Object> {
        private final Exception toThrow;

        ThrowingInitializer(Exception toThrow) {
            this.toThrow = toThrow;
        }

        @Override
        protected Object initialize() throws Exception {
            throw toThrow;
        }
    }
}
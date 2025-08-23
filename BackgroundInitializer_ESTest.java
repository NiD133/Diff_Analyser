package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

/**
 * Readable, behavior-focused tests for BackgroundInitializer.
 *
 * Notes:
 * - These tests avoid obscure mocks and race-prone assertions.
 * - They verify the contract described in the Javadoc: lifecycle, executor handling,
 *   and error conditions.
 * - Tests are in the same package to access protected members used by the class.
 */
public class BackgroundInitializerTest {

    @Test
    public void startAndGet_defaultInitializerReturnsNullAndMarksInitialized() throws Exception {
        BackgroundInitializer<Object> bi = new BackgroundInitializer<>();

        assertFalse("Not started yet", bi.isStarted());
        assertFalse("Not initialized yet", bi.isInitialized());

        assertTrue("First start() should return true", bi.start());

        Object result = bi.get(); // Waits for background initialization to complete.
        assertNull("Default initialize() returns null", result);

        assertTrue("isStarted() should be true after start()", bi.isStarted());
        assertTrue("isInitialized() should be true after successful get()", bi.isInitialized());
    }

    @Test
    public void start_isOnlySuccessfulOnce() {
        BackgroundInitializer<Object> bi = new BackgroundInitializer<>();

        assertTrue("First start() returns true", bi.start());
        assertFalse("Subsequent start() calls return false", bi.start());
    }

    @Test
    public void getFuture_requiresStart() {
        BackgroundInitializer<Object> bi = new BackgroundInitializer<>();

        try {
            bi.getFuture();
            fail("Expected IllegalStateException when getFuture() is called before start()");
        } catch (IllegalStateException expected) {
            // expected
        }
    }

    @Test
    public void get_requiresStart() throws Exception {
        BackgroundInitializer<Object> bi = new BackgroundInitializer<>();

        try {
            bi.get();
            fail("Expected IllegalStateException when get() is called before start()");
        } catch (IllegalStateException expected) {
            // expected
        }
    }

    @Test
    public void getActiveExecutor_beforeStartIsNull() {
        BackgroundInitializer<Object> bi = new BackgroundInitializer<>();
        assertNull("Active executor is null before start()", bi.getActiveExecutor());
    }

    @Test
    public void getExternalExecutor_defaultsToNull() {
        BackgroundInitializer<Object> bi = new BackgroundInitializer<>();
        assertNull("External executor defaults to null", bi.getExternalExecutor());
    }

    @Test
    public void start_withNoExternalExecutorCreatesTemporaryExecutor() throws Exception {
        BackgroundInitializer<Object> bi = new BackgroundInitializer<>();

        assertTrue(bi.start());
        assertNotNull("A temporary executor should be created when none is provided", bi.getActiveExecutor());

        // Ensure the background task actually completes.
        bi.get();
    }

    @Test
    public void usesProvidedExternalExecutor() throws Exception {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        try {
            BackgroundInitializer<Object> bi = new BackgroundInitializer<>();

            bi.setExternalExecutor(exec);
            assertSame("getExternalExecutor() should return the set instance", exec, bi.getExternalExecutor());

            assertTrue(bi.start());
            assertSame("Active executor should be the provided external executor", exec, bi.getActiveExecutor());

            // Ensure the background task actually completes.
            bi.get();
        } finally {
            exec.shutdownNow();
        }
    }

    @Test
    public void setExternalExecutor_afterStartThrows_andDoesNotChangeExecutor() {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        try {
            BackgroundInitializer<Object> bi = new BackgroundInitializer<>();

            assertTrue(bi.start());

            try {
                bi.setExternalExecutor(exec);
                fail("Expected IllegalStateException when setExternalExecutor() is called after start()");
            } catch (IllegalStateException expected) {
                // expected
            }
        } finally {
            exec.shutdownNow();
        }
    }

    @Test
    public void setExternalExecutor_afterStartThrows_evenWithNull() {
        BackgroundInitializer<Object> bi = new BackgroundInitializer<>();

        assertTrue(bi.start());

        try {
            bi.setExternalExecutor(null);
            fail("Expected IllegalStateException when setExternalExecutor(null) is called after start()");
        } catch (IllegalStateException expected) {
            // expected
        }
    }

    @Test
    public void getFuture_afterStartReturnsFuture() {
        BackgroundInitializer<Object> bi = new BackgroundInitializer<>();

        assertTrue(bi.start());
        Future<Object> future = bi.getFuture();
        assertNotNull("getFuture() should return a Future after start()", future);
    }

    @Test
    public void getTaskCount_defaultIsOne() {
        BackgroundInitializer<Object> bi = new BackgroundInitializer<>();
        assertEquals("Default task count should be 1", 1, bi.getTaskCount());
    }

    @Test
    public void builder_returnsBuilder() {
        assertNotNull("builder() should return a non-null builder", BackgroundInitializer.builder());
    }

    @Test
    public void getTypedException_returnsSameException() {
        BackgroundInitializer<Object> bi = new BackgroundInitializer<>();
        ConcurrentException ce = new ConcurrentException();

        Exception e = bi.getTypedException(ce);
        assertSame("getTypedException() should return the same exception instance", ce, e);
    }

    @Test
    public void close_beforeStart_doesNotStartAndGetStillRequiresStart() throws Exception {
        BackgroundInitializer<Object> bi = new BackgroundInitializer<>();

        bi.close(); // Should not implicitly start or initialize.

        try {
            bi.get();
            fail("Expected IllegalStateException when get() is called before start(), even after close()");
        } catch (IllegalStateException expected) {
            // expected
        }
    }
}
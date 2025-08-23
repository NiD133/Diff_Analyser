package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.AbstractLangTest;
import org.apache.commons.lang3.ThreadUtils;
import org.junit.jupiter.api.Test;

class BackgroundInitializerTest extends AbstractLangTest {

    /**
     * Implementation of BackgroundInitializer for testing purposes.
     */
    protected static class TestBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {
        private Exception exceptionToThrow;
        private boolean shouldSleep;
        private final CountDownLatch latch = new CountDownLatch(1);
        private boolean useLatch;
        private final CloseableCounter counter = new CloseableCounter();

        TestBackgroundInitializer() {}

        TestBackgroundInitializer(final ExecutorService executor) {
            super(executor);
        }

        public void enableLatch() {
            useLatch = true;
        }

        public CloseableCounter getCounter() {
            return counter;
        }

        @Override
        protected CloseableCounter initializeInternal() throws Exception {
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            if (shouldSleep) {
                ThreadUtils.sleep(Duration.ofMinutes(1));
            }
            if (useLatch) {
                latch.await();
            }
            return counter.increment();
        }

        public void releaseLatch() {
            latch.countDown();
        }
    }

    protected static class CloseableCounter {
        private final AtomicInteger initializeCalls = new AtomicInteger();
        private final AtomicBoolean closed = new AtomicBoolean();

        public void close() {
            closed.set(true);
        }

        public int getInitializeCalls() {
            return initializeCalls.get();
        }

        public CloseableCounter increment() {
            initializeCalls.incrementAndGet();
            return this;
        }

        public boolean isClosed() {
            return closed.get();
        }
    }

    private void verifyInitialization(final TestBackgroundInitializer initializer) throws ConcurrentException {
        assertEquals(1, initializer.get().getInitializeCalls(), "Initialize should be called once");
        assertEquals(1, initializer.getCounter().getInitializeCalls(), "Wrong number of initialize calls");
        assertNotNull(initializer.getFuture(), "Future should not be null");
    }

    protected TestBackgroundInitializer createInitializer() {
        return new TestBackgroundInitializer();
    }

    protected TestBackgroundInitializer createInitializer(final ExecutorService executor) {
        return new TestBackgroundInitializer(executor);
    }

    @Test
    void testBuilderInitialization() throws ConcurrentException {
        final BackgroundInitializer<Object> initializer = BackgroundInitializer.builder()
            .setCloser(null)
            .setExternalExecutor(null)
            .setInitializer(null)
            .get();

        assertNull(initializer.getExternalExecutor());
        assertFalse(initializer.isInitialized());
        assertFalse(initializer.isStarted());
        assertThrows(IllegalStateException.class, initializer::getFuture);
    }

    @Test
    void testBuilderWithException() throws ConcurrentException {
        final BackgroundInitializer<Object> initializer = BackgroundInitializer.builder()
            .setCloser(null)
            .setExternalExecutor(null)
            .setInitializer(() -> {
                throw new IllegalStateException("test");
            })
            .get();

        assertNull(initializer.getExternalExecutor());
        assertFalse(initializer.isInitialized());
        assertFalse(initializer.isStarted());
        assertThrows(IllegalStateException.class, initializer::getFuture);

        initializer.start();
        assertEquals("test", assertThrows(IllegalStateException.class, initializer::get).getMessage());
    }

    @Test
    void testExecutorBeforeStart() {
        final TestBackgroundInitializer initializer = createInitializer();
        assertNull(initializer.getActiveExecutor(), "Executor should be null before start");
    }

    @Test
    void testExternalExecutorDetection() throws InterruptedException, ConcurrentException {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            final TestBackgroundInitializer initializer = createInitializer(executor);
            initializer.start();
            assertSame(executor, initializer.getActiveExecutor(), "Executor mismatch");
            verifyInitialization(initializer);
        } finally {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    @Test
    void testTemporaryExecutorUsage() throws ConcurrentException {
        final TestBackgroundInitializer initializer = createInitializer();
        initializer.start();
        assertNotNull(initializer.getActiveExecutor(), "Executor should not be null");
        verifyInitialization(initializer);
    }

    @Test
    void testGetBeforeStartThrowsException() {
        final TestBackgroundInitializer initializer = createInitializer();
        assertThrows(IllegalStateException.class, initializer::get);
    }

    @Test
    void testCheckedExceptionDuringGet() {
        final TestBackgroundInitializer initializer = createInitializer();
        final Exception exception = new Exception();
        initializer.exceptionToThrow = exception;
        initializer.start();
        final ConcurrentException concurrentException = assertThrows(ConcurrentException.class, initializer::get);
        assertEquals(exception, concurrentException.getCause(), "Expected exception not thrown");
    }

    @Test
    void testGetInterruptedExceptionHandling() throws InterruptedException {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final TestBackgroundInitializer initializer = createInitializer(executor);
        final CountDownLatch latch = new CountDownLatch(1);
        initializer.shouldSleep = true;
        initializer.start();
        final AtomicReference<InterruptedException> interruptedException = new AtomicReference<>();
        final Thread getThread = new Thread(() -> {
            try {
                initializer.get();
            } catch (final ConcurrentException e) {
                if (e.getCause() instanceof InterruptedException) {
                    interruptedException.set((InterruptedException) e.getCause());
                }
            } finally {
                assertTrue(Thread.currentThread().isInterrupted(), "Thread should be interrupted");
                latch.countDown();
            }
        });
        getThread.start();
        getThread.interrupt();
        latch.await();
        executor.shutdownNow();
        executor.awaitTermination(1, TimeUnit.SECONDS);
        assertNotNull(interruptedException.get(), "InterruptedException expected");
    }

    @Test
    void testRuntimeExceptionDuringGet() {
        final TestBackgroundInitializer initializer = createInitializer();
        final RuntimeException runtimeException = new RuntimeException();
        initializer.exceptionToThrow = runtimeException;
        initializer.start();
        final Exception exception = assertThrows(Exception.class, initializer::get);
        assertEquals(runtimeException, exception, "Expected runtime exception not thrown");
    }

    @Test
    void testInitializationInvocation() throws ConcurrentException {
        final TestBackgroundInitializer initializer = createInitializer();
        initializer.start();
        verifyInitialization(initializer);
    }

    @Test
    void testTemporaryExecutorCreation() throws ConcurrentException {
        final TestBackgroundInitializer initializer = createInitializer();
        assertTrue(initializer.start(), "Start should return true");
        verifyInitialization(initializer);
        assertTrue(initializer.getActiveExecutor().isShutdown(), "Executor should be shutdown");
    }

    @Test
    void testInitializationStatus() throws ConcurrentException {
        final TestBackgroundInitializer initializer = createInitializer();
        initializer.enableLatch();
        initializer.start();
        assertTrue(initializer.isStarted(), "Initializer should be started");
        assertFalse(initializer.isInitialized(), "Initializer should not be initialized before latch release");
        initializer.releaseLatch();
        initializer.get(); // Ensure initialization is complete
        assertTrue(initializer.isInitialized(), "Initializer should be initialized after latch release");
    }

    @Test
    void testStartStatusAfterGet() throws ConcurrentException {
        final TestBackgroundInitializer initializer = createInitializer();
        initializer.start();
        verifyInitialization(initializer);
        assertTrue(initializer.isStarted(), "Initializer should be started");
    }

    @Test
    void testStartStatusBeforeStart() {
        final TestBackgroundInitializer initializer = createInitializer();
        assertFalse(initializer.isStarted(), "Initializer should not be started");
    }

    @Test
    void testStartStatusAfterStart() {
        final TestBackgroundInitializer initializer = createInitializer();
        initializer.start();
        assertTrue(initializer.isStarted(), "Initializer should be started");
    }

    @Test
    void testSetExternalExecutor() throws ConcurrentException {
        final ExecutorService executor = Executors.newCachedThreadPool();
        try {
            final TestBackgroundInitializer initializer = createInitializer();
            initializer.setExternalExecutor(executor);
            assertEquals(executor, initializer.getExternalExecutor(), "Executor mismatch");
            assertTrue(initializer.start(), "Start should return true");
            assertSame(executor, initializer.getActiveExecutor(), "Active executor mismatch");
            verifyInitialization(initializer);
            assertFalse(executor.isShutdown(), "Executor should not be shutdown");
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void testSetExecutorAfterStartThrowsException() throws ConcurrentException, InterruptedException {
        final TestBackgroundInitializer initializer = createInitializer();
        initializer.start();
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            assertThrows(IllegalStateException.class, () -> initializer.setExternalExecutor(executor));
            initializer.get();
        } finally {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    @Test
    void testMultipleStartInvocations() throws ConcurrentException {
        final TestBackgroundInitializer initializer = createInitializer();
        assertTrue(initializer.start(), "Start should return true");
        for (int i = 0; i < 10; i++) {
            assertFalse(initializer.start(), "Subsequent starts should return false");
        }
        verifyInitialization(initializer);
    }
}
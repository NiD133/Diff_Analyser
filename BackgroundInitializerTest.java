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
     * A concrete implementation of BackgroundInitializer for testing purposes.
     */
    protected static class TestBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {
        private Exception exceptionToThrow;
        private boolean shouldSleep;
        private final CountDownLatch latch = new CountDownLatch(1);
        private boolean waitForLatch;
        private final CloseableCounter counter = new CloseableCounter();

        TestBackgroundInitializer() {}

        TestBackgroundInitializer(final ExecutorService executor) {
            super(executor);
        }

        public void enableLatch() {
            waitForLatch = true;
        }

        public CloseableCounter getCounter() {
            return counter;
        }

        @Override
        protected CloseableCounter initialize() throws Exception {
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            if (shouldSleep) {
                ThreadUtils.sleep(Duration.ofMinutes(1));
            }
            if (waitForLatch) {
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
        assertEquals(1, initializer.get().getInitializeCalls(), "Initialization count mismatch");
        assertEquals(1, initializer.getCounter().getInitializeCalls(), "Invocation count mismatch");
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
    void testGetExecutorBeforeStart() {
        final TestBackgroundInitializer initializer = createInitializer();
        assertNull(initializer.getActiveExecutor(), "Executor should be null before start");
    }

    @Test
    void testExternalExecutorUsage() throws InterruptedException, ConcurrentException {
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
        assertNotNull(initializer.getActiveExecutor(), "Active executor should not be null");
        verifyInitialization(initializer);
    }

    @Test
    void testGetBeforeStartThrowsException() {
        final TestBackgroundInitializer initializer = createInitializer();
        assertThrows(IllegalStateException.class, initializer::get);
    }

    @Test
    void testGetWithCheckedException() {
        final TestBackgroundInitializer initializer = createInitializer();
        final Exception exception = new Exception();
        initializer.exceptionToThrow = exception;
        initializer.start();
        final ConcurrentException concurrentException = assertThrows(ConcurrentException.class, initializer::get);
        assertEquals(exception, concurrentException.getCause(), "Exception cause mismatch");
    }

    @Test
    void testGetWithInterruptedException() throws InterruptedException {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final TestBackgroundInitializer initializer = createInitializer(executor);
        final CountDownLatch latch = new CountDownLatch(1);
        initializer.shouldSleep = true;
        initializer.start();

        final AtomicReference<InterruptedException> interruptedExceptionRef = new AtomicReference<>();
        final Thread getThread = new Thread(() -> {
            try {
                initializer.get();
            } catch (final ConcurrentException e) {
                if (e.getCause() instanceof InterruptedException) {
                    interruptedExceptionRef.set((InterruptedException) e.getCause());
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
        assertNotNull(interruptedExceptionRef.get(), "InterruptedException should be captured");
    }

    @Test
    void testGetWithRuntimeException() {
        final TestBackgroundInitializer initializer = createInitializer();
        final RuntimeException runtimeException = new RuntimeException();
        initializer.exceptionToThrow = runtimeException;
        initializer.start();
        final Exception exception = assertThrows(Exception.class, initializer::get);
        assertEquals(runtimeException, exception, "Runtime exception mismatch");
    }

    @Test
    void testInitialization() throws ConcurrentException {
        final TestBackgroundInitializer initializer = createInitializer();
        initializer.start();
        verifyInitialization(initializer);
    }

    @Test
    void testInitializationWithTemporaryExecutor() throws ConcurrentException {
        final TestBackgroundInitializer initializer = createInitializer();
        assertTrue(initializer.start(), "Start should return true");
        verifyInitialization(initializer);
        assertTrue(initializer.getActiveExecutor().isShutdown(), "Executor should be shutdown");
    }

    @Test
    void testInitializationState() throws ConcurrentException {
        final TestBackgroundInitializer initializer = createInitializer();
        initializer.enableLatch();
        initializer.start();
        assertTrue(initializer.isStarted(), "Initializer should be started");
        assertFalse(initializer.isInitialized(), "Initializer should not be initialized before latch release");
        initializer.releaseLatch();
        initializer.get();
        assertTrue(initializer.isInitialized(), "Initializer should be initialized after latch release");
    }

    @Test
    void testIsStartedAfterGet() throws ConcurrentException {
        final TestBackgroundInitializer initializer = createInitializer();
        initializer.start();
        verifyInitialization(initializer);
        assertTrue(initializer.isStarted(), "Initializer should be started");
    }

    @Test
    void testIsStartedBeforeStart() {
        final TestBackgroundInitializer initializer = createInitializer();
        assertFalse(initializer.isStarted(), "Initializer should not be started");
    }

    @Test
    void testIsStartedAfterStart() {
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
    void testSetExternalExecutorAfterStartThrowsException() throws ConcurrentException, InterruptedException {
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
    void testStartMultipleTimes() throws ConcurrentException {
        final TestBackgroundInitializer initializer = createInitializer();
        assertTrue(initializer.start(), "First start should return true");
        for (int i = 0; i < 10; i++) {
            assertFalse(initializer.start(), "Subsequent starts should return false");
        }
        verifyInitialization(initializer);
    }
}
package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.Futures.immediateFailedFuture;
import static com.google.common.util.concurrent.Futures.immediateFuture;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

public class ListeningExecutorServiceTestTest2 extends TestCase {

    private Collection<? extends Callable<?>> recordedTasks;

    private long recordedTimeout;

    private TimeUnit recordedTimeUnit;

    private final ListeningExecutorService executorService = new FakeExecutorService();

    private class FakeExecutorService extends AbstractListeningExecutorService {

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            recordedTasks = tasks;
            recordedTimeout = timeout;
            recordedTimeUnit = unit;
            try {
                return tasks.iterator().next().call();
            } catch (Exception e) {
                throw new ExecutionException(e);
            }
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
            recordedTasks = tasks;
            recordedTimeout = timeout;
            recordedTimeUnit = unit;
            try {
                return Collections.singletonList(immediateFuture(tasks.iterator().next().call()));
            } catch (Exception e) {
                return Collections.singletonList(immediateFailedFuture(e));
            }
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) {
            recordedTimeout = timeout;
            recordedTimeUnit = unit;
            return true;
        }

        @Override
        public void execute(Runnable runnable) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void shutdown() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Runnable> shutdownNow() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isShutdown() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isTerminated() {
            throw new UnsupportedOperationException();
        }
    }

    public void testInvokeAll() throws Exception {
        Set<Callable<String>> tasks = Collections.singleton(() -> "invokeAll");
        List<Future<String>> result = executorService.invokeAll(tasks, Duration.ofDays(365));
        assertThat(result).hasSize(1);
        assertThat(Futures.getDone(result.get(0))).isEqualTo("invokeAll");
        assertThat(recordedTasks).isSameInstanceAs(tasks);
        assertThat(recordedTimeUnit).isEqualTo(NANOSECONDS);
        assertThat(Duration.ofNanos(recordedTimeout)).isEqualTo(Duration.ofDays(365));
    }
}

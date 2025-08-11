package com.google.common.util.concurrent;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * Focused, readable tests for ExecutionList.
 * 
 * Key behaviors covered:
 * - Listeners added before execute() run exactly once, in registration order.
 * - Listeners added after execute() run immediately.
 * - execute() is idempotent.
 * - Failures in an Executor (or a direct executor running a failing Runnable) are caught
 *   and do not prevent other listeners from running.
 * - Null checks on add().
 */
public class ExecutionListTest {

    // Simple inline executor to avoid extra dependencies
    private static final Executor DIRECT = Runnable::run;

    @Test
    public void executesExistingListenersInRegistrationOrder() {
        ExecutionList list = new ExecutionList();
        List<String> calls = new ArrayList<>();

        list.add(() -> calls.add("first"), DIRECT);
        list.add(() -> calls.add("second"), DIRECT);

        // Nothing should run before execute() is called
        assertTrue(calls.isEmpty());

        list.execute();

        // Existing listeners run once, in registration order
        assertEquals(List.of("first", "second"), calls);

        // execute() is idempotent; subsequent calls do not re-run listeners
        list.execute();
        assertEquals(List.of("first", "second"), calls);
    }

    @Test
    public void addAfterExecuteRunsImmediately() {
        ExecutionList list = new ExecutionList();
        AtomicInteger counter = new AtomicInteger();

        list.execute(); // switch to "executed" state

        // After execute(), added listeners are executed immediately
        list.add(counter::incrementAndGet, DIRECT);
        assertEquals(1, counter.get());

        // Adding more also runs immediately
        list.add(counter::incrementAndGet, DIRECT);
        assertEquals(2, counter.get());
    }

    @Test
    public void executorFailureIsCaughtAndDoesNotPreventOthers() {
        ExecutionList list = new ExecutionList();
        AtomicInteger ran = new AtomicInteger();

        Executor badExecutor = runnable -> { throw new RuntimeException("boom"); };

        // Mix successful and failing executions
        list.add(ran::incrementAndGet, DIRECT);             // should run
        list.add(() -> ran.incrementAndGet(), badExecutor); // executor throws; should be caught
        list.add(ran::incrementAndGet, DIRECT);             // should still run

        // Should not throw
        list.execute();

        // Only the two successful listeners should have run
        assertEquals(2, ran.get());
    }

    @Test
    public void failingRunnableWithDirectExecutorIsCaughtAndDoesNotPropagate() {
        ExecutionList list = new ExecutionList();
        AtomicInteger ran = new AtomicInteger();

        list.add(() -> { throw new RuntimeException("listener failure"); }, DIRECT);
        list.add(ran::incrementAndGet, DIRECT); // should still run despite the previous failure

        // Should not throw due to internal catch-and-log behavior
        list.execute();

        assertEquals(1, ran.get());
    }

    @Test
    public void addRejectsNullArguments() {
        ExecutionList list = new ExecutionList();

        assertThrows(NullPointerException.class, () -> list.add(null, DIRECT));
        assertThrows(NullPointerException.class, () -> list.add(() -> {}, null));
    }
}
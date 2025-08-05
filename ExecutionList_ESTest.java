/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link ExecutionList}.
 */
@RunWith(JUnit4.class)
public class ExecutionListTest {

    private ExecutionList executionList;
    private final AtomicInteger executionCounter = new AtomicInteger(0);
    private final Executor directExecutor = MoreExecutors.directExecutor();

    private final Runnable incrementingTask = new Runnable() {
        @Override
        public void run() {
            executionCounter.incrementAndGet();
        }
    };

    private final Runnable throwingTask = new Runnable() {
        @Override
        public void run() {
            throw new RuntimeException("Test exception");
        }
    };

    @Before
    public void setUp() {
        executionList = new ExecutionList();
        executionCounter.set(0);
    }

    @Test
    public void add_beforeExecute_shouldRunListenerAfterExecuteIsCalled() {
        // Arrange: Add a listener before execute() is called.
        executionList.add(incrementingTask, directExecutor);
        assertEquals("Listener should not run before execute() is called", 0, executionCounter.get());

        // Act: Execute the list.
        executionList.execute();

        // Assert: The listener should have been executed.
        assertEquals("Listener should run once after execute()", 1, executionCounter.get());
    }

    @Test
    public void add_afterExecute_shouldRunListenerImmediately() {
        // Arrange: Execute the list first.
        executionList.execute();

        // Act: Add a listener after the list has already been executed.
        executionList.add(incrementingTask, directExecutor);

        // Assert: The listener should be executed immediately upon being added.
        assertEquals("Listener added after execute() should run immediately", 1, executionCounter.get());
    }

    @Test
    public void execute_withMultipleListeners_shouldRunAllListeners() {
        // Arrange: Add multiple listeners.
        executionList.add(incrementingTask, directExecutor);
        executionList.add(incrementingTask, directExecutor);
        executionList.add(incrementingTask, directExecutor);

        // Act: Execute the list.
        executionList.execute();

        // Assert: All listeners should have been executed.
        assertEquals("All listeners should be executed", 3, executionCounter.get());
    }

    @Test
    public void execute_calledMultipleTimes_shouldOnlyRunListenersOnce() {
        // Arrange: Add a listener.
        executionList.add(incrementingTask, directExecutor);

        // Act: Execute the list multiple times.
        executionList.execute();
        executionList.execute();
        executionList.execute();

        // Assert: The listener should only be executed once.
        assertEquals("Listeners should only be executed once, even if execute() is called multiple times", 1, executionCounter.get());
    }

    @Test
    public void execute_whenListenerThrowsException_shouldNotPreventOtherListenersFromRunning() {
        // Arrange: Add a listener that throws an exception, followed by a normal one.
        // The order of execution isn't guaranteed, but both should be attempted.
        executionList.add(throwingTask, directExecutor);
        executionList.add(incrementingTask, directExecutor);

        // Act: Execute the list.
        executionList.execute();

        // Assert: The non-throwing listener should still have been executed.
        assertEquals("A throwing listener should not stop others from running", 1, executionCounter.get());
    }

    @Test
    public void add_afterExecuteWithThrowingListener_shouldStillExecuteImmediately() {
        // Arrange: Add a listener that will throw, and execute the list.
        executionList.add(throwingTask, directExecutor);
        executionList.execute();
        assertEquals("The throwing listener should not affect the counter", 0, executionCounter.get());

        // Act: Add a new listener after the list has been executed.
        executionList.add(incrementingTask, directExecutor);

        // Assert: The new listener should execute immediately.
        assertEquals("New listeners should run immediately even if a prior one threw", 1, executionCounter.get());
    }
}
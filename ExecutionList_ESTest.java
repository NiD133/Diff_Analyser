/*
 * Copyright (C) 2024 The Guava Authors
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
package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link ExecutionList}, verifying listener execution under different conditions.
 */
@RunWith(JUnit4.class)
public class ExecutionListTest {

    // Simple executor that runs commands in the calling thread
    private static final Executor DIRECT_EXECUTOR = Runnable::run;

    @Test
    public void execute_runsAddedListeners() {
        ExecutionList list = new ExecutionList();
        AtomicInteger runCount = new AtomicInteger();
        
        list.add(runCount::incrementAndGet, DIRECT_EXECUTOR);
        list.execute();
        
        assertEquals(1, runCount.get());
    }

    @Test
    public void execute_runsMultipleListeners() {
        ExecutionList list = new ExecutionList();
        AtomicInteger count1 = new AtomicInteger();
        AtomicInteger count2 = new AtomicInteger();
        
        list.add(count1::incrementAndGet, DIRECT_EXECUTOR);
        list.add(count2::incrementAndGet, DIRECT_EXECUTOR);
        list.execute();
        
        assertEquals(1, count1.get());
        assertEquals(1, count2.get());
    }

    @Test
    public void add_afterExecute_runsListenerImmediately() {
        ExecutionList list = new ExecutionList();
        list.execute();  // Mark as executed
        
        AtomicInteger runCount = new AtomicInteger();
        list.add(runCount::incrementAndGet, DIRECT_EXECUTOR);
        
        assertEquals(1, runCount.get());
    }

    @Test
    public void execute_isIdempotent() {
        ExecutionList list = new ExecutionList();
        AtomicInteger runCount = new AtomicInteger();
        list.add(runCount::incrementAndGet, DIRECT_EXECUTOR);
        
        list.execute();
        list.execute();  // Second call should have no effect
        
        assertEquals(1, runCount.get());
    }

    @Test
    public void listenerException_doesNotPreventOtherListeners() {
        ExecutionList list = new ExecutionList();
        AtomicInteger runCount = new AtomicInteger();
        
        list.add(() -> { throw new RuntimeException(); }, DIRECT_EXECUTOR);
        list.add(runCount::incrementAndGet, DIRECT_EXECUTOR);
        list.execute();
        
        assertEquals(1, runCount.get());
    }

    @Test
    public void executorException_doesNotPreventOtherListeners() {
        ExecutionList list = new ExecutionList();
        AtomicInteger runCount = new AtomicInteger();
        
        Executor throwingExecutor = command -> {
            throw new RejectedExecutionException("Test rejection");
        };
        
        list.add(() -> {}, throwingExecutor);
        list.add(runCount::incrementAndGet, DIRECT_EXECUTOR);
        list.execute();
        
        assertEquals(1, runCount.get());
    }
}
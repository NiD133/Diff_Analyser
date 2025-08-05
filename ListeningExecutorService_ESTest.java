package com.google.common.util.concurrent;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.util.concurrent.DirectExecutorService;
import java.time.Duration;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

/**
 * Test suite for ListeningExecutorService interface using DirectExecutorService implementation.
 * Tests focus on Duration-based method overloads and error handling scenarios.
 */
@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class ListeningExecutorService_ESTest extends ListeningExecutorService_ESTest_scaffolding {

    private static final Duration ZERO_DURATION = Duration.ZERO;
    private static final Duration NEGATIVE_DURATION = Duration.ofMinutes(-356L);
    
    @Test(timeout = 4000)
    public void testAwaitTermination_WhenShutdown_ReturnsTrue() throws Throwable {
        // Given: A shutdown executor service
        DirectExecutorService executorService = new DirectExecutorService();
        executorService.shutdown();
        
        // When: Awaiting termination with zero duration
        boolean hasTerminated = executorService.awaitTermination(ZERO_DURATION);
        
        // Then: Should return true since executor is already shutdown
        assertTrue("Shutdown executor should terminate immediately", hasTerminated);
    }

    @Test(timeout = 4000)
    public void testInvokeAny_WithNullDuration_ThrowsNullPointerException() throws Throwable {
        // Given: An executor service and empty task collection
        DirectExecutorService executorService = new DirectExecutorService();
        LinkedBlockingQueue<Callable<Object>> emptyTaskQueue = new LinkedBlockingQueue<>();
        
        // When & Then: Calling invokeAny with null duration should throw NPE
        try { 
            executorService.invokeAny(emptyTaskQueue, null);
            fail("Expected NullPointerException for null duration");
        } catch(NullPointerException e) {
            verifyException("com.google.common.util.concurrent.Internal", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeAny_WithMockedCallable_ThrowsNoClassDefFoundError() throws Throwable {
        // Given: An executor service with a mocked callable task
        DirectExecutorService executorService = new DirectExecutorService();
        ArrayList<Callable<Integer>> tasks = new ArrayList<>();
        Callable<Integer> mockTask = mock(Callable.class, new ViolatedAssumptionAnswer());
        tasks.add(mockTask);
        
        Duration fiveMinutesDuration = createDurationFromChronoField(5112L);
        
        // When & Then: Should throw NoClassDefFoundError due to missing TrustedListenableFutureTask
        try { 
            executorService.invokeAny(tasks, fiveMinutesDuration);
            fail("Expected NoClassDefFoundError for missing TrustedListenableFutureTask");
        } catch(NoClassDefFoundError e) {
            verifyException("com.google.common.util.concurrent.AbstractListeningExecutorService", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeAll_WithNullTaskCollection_ThrowsNullPointerException() throws Throwable {
        // Given: An executor service and null task collection
        DirectExecutorService executorService = new DirectExecutorService();
        
        // When & Then: Calling invokeAll with null collection should throw NPE
        try { 
            executorService.invokeAll(null, NEGATIVE_DURATION);
            fail("Expected NullPointerException for null task collection");
        } catch(NullPointerException e) {
            verifyException("java.util.concurrent.AbstractExecutorService", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeAll_WithMockedCallable_ThrowsNoClassDefFoundError() throws Throwable {
        // Given: An executor service with a mocked callable task
        DirectExecutorService executorService = new DirectExecutorService();
        LinkedHashSet<Callable<Object>> tasks = new LinkedHashSet<>();
        Callable<Object> mockTask = mock(Callable.class, new ViolatedAssumptionAnswer());
        tasks.add(mockTask);
        
        // When & Then: Should throw NoClassDefFoundError due to missing TrustedListenableFutureTask
        try { 
            executorService.invokeAll(tasks, ZERO_DURATION);
            fail("Expected NoClassDefFoundError for missing TrustedListenableFutureTask");
        } catch(NoClassDefFoundError e) {
            verifyException("com.google.common.util.concurrent.AbstractListeningExecutorService", e);
        }
    }

    @Test(timeout = 4000)
    public void testAwaitTermination_WithNullDuration_ThrowsNullPointerException() throws Throwable {
        // Given: An executor service
        DirectExecutorService executorService = new DirectExecutorService();
        
        // When & Then: Calling awaitTermination with null duration should throw NPE
        try { 
            executorService.awaitTermination(null);
            fail("Expected NullPointerException for null duration");
        } catch(NullPointerException e) {
            verifyException("com.google.common.util.concurrent.Internal", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeAny_WithEmptyTaskCollection_ThrowsIllegalArgumentException() throws Throwable {
        // Given: An executor service and empty task collection
        DirectExecutorService executorService = new DirectExecutorService();
        ArrayList<Callable<Object>> emptyTasks = new ArrayList<>();
        
        // When & Then: Calling invokeAny with empty collection should throw IllegalArgumentException
        try { 
            executorService.invokeAny(emptyTasks, ZERO_DURATION);
            fail("Expected IllegalArgumentException for empty task collection");
        } catch(IllegalArgumentException e) {
            verifyException("java.util.concurrent.AbstractExecutorService", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeAll_WithEmptyTaskCollection_ReturnsEmptyList() throws Throwable {
        // Given: An executor service and empty task collection
        DirectExecutorService executorService = new DirectExecutorService();
        LinkedTransferQueue<Callable<Object>> emptyTasks = new LinkedTransferQueue<>();
        
        // When: Invoking all tasks from empty collection
        List<Future<Object>> results = executorService.invokeAll(emptyTasks, ZERO_DURATION);
        
        // Then: Should return empty list
        assertEquals("Empty task collection should return empty result list", 0, results.size());
    }

    @Test(timeout = 4000)
    public void testAwaitTermination_WhenNotShutdown_ReturnsFalse() throws Throwable {
        // Given: A running (not shutdown) executor service
        DirectExecutorService executorService = new DirectExecutorService();
        
        // When: Awaiting termination with zero duration
        boolean hasTerminated = executorService.awaitTermination(ZERO_DURATION);
        
        // Then: Should return false since executor is still running
        assertFalse("Running executor should not terminate with zero timeout", hasTerminated);
    }
    
    /**
     * Helper method to create a Duration using ChronoField for test consistency
     */
    private Duration createDurationFromChronoField(long value) {
        ChronoField secondOfMinute = ChronoField.SECOND_OF_MINUTE;
        TemporalUnit rangeUnit = secondOfMinute.getRangeUnit();
        return Duration.of(value, rangeUnit);
    }
}
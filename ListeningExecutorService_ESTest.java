package com.google.common.util.concurrent;

import org.junit.Test;
import org.junit.runner.RunWith;
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

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class ListeningExecutorService_ESTest extends ListeningExecutorService_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testAwaitTerminationWithZeroDuration() throws Throwable {
        DirectExecutorService executorService = new DirectExecutorService();
        Duration zeroDuration = Duration.ZERO;
        
        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(zeroDuration);
        
        assertTrue("Executor service should terminate immediately with zero duration", terminated);
    }

    @Test(timeout = 4000)
    public void testInvokeAnyWithNullDuration() throws Throwable {
        DirectExecutorService executorService = new DirectExecutorService();
        LinkedBlockingQueue<Callable<Object>> callables = new LinkedBlockingQueue<>();
        
        try {
            executorService.invokeAny(callables, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.util.concurrent.Internal", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeAnyWithDurationAndMockedCallable() throws Throwable {
        DirectExecutorService executorService = new DirectExecutorService();
        ArrayList<Callable<Integer>> callables = new ArrayList<>();
        Callable<Integer> mockedCallable = mock(Callable.class, new ViolatedAssumptionAnswer());
        callables.add(mockedCallable);
        
        TemporalUnit secondsUnit = ChronoField.SECOND_OF_MINUTE.getRangeUnit();
        Duration duration = Duration.of(5112L, secondsUnit);
        
        try {
            executorService.invokeAny(callables, duration);
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            verifyException("com.google.common.util.concurrent.AbstractListeningExecutorService", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeAllWithNullCollection() throws Throwable {
        DirectExecutorService executorService = new DirectExecutorService();
        Duration negativeDuration = Duration.ofMinutes(-356L);
        
        try {
            executorService.invokeAll(null, negativeDuration);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.concurrent.AbstractExecutorService", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeAllWithZeroDurationAndMockedCallable() throws Throwable {
        DirectExecutorService executorService = new DirectExecutorService();
        Duration zeroDuration = Duration.ZERO;
        LinkedHashSet<Callable<Object>> callables = new LinkedHashSet<>();
        Callable<Object> mockedCallable = mock(Callable.class, new ViolatedAssumptionAnswer());
        callables.add(mockedCallable);
        
        try {
            executorService.invokeAll(callables, zeroDuration);
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            verifyException("com.google.common.util.concurrent.AbstractListeningExecutorService", e);
        }
    }

    @Test(timeout = 4000)
    public void testAwaitTerminationWithNullDuration() throws Throwable {
        DirectExecutorService executorService = new DirectExecutorService();
        
        try {
            executorService.awaitTermination(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.util.concurrent.Internal", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeAnyWithEmptyCollectionAndZeroDuration() throws Throwable {
        DirectExecutorService executorService = new DirectExecutorService();
        Duration zeroDuration = Duration.ZERO;
        ArrayList<Callable<Object>> emptyCallables = new ArrayList<>();
        
        try {
            executorService.invokeAny(emptyCallables, zeroDuration);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.util.concurrent.AbstractExecutorService", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeAllWithEmptyCollectionAndZeroDuration() throws Throwable {
        DirectExecutorService executorService = new DirectExecutorService();
        Duration zeroDuration = Duration.ZERO;
        LinkedTransferQueue<Callable<Object>> emptyCallables = new LinkedTransferQueue<>();
        
        List<Future<Object>> futures = executorService.invokeAll(emptyCallables, zeroDuration);
        assertEquals("Expected no futures to be returned for empty collection", 0, futures.size());
    }

    @Test(timeout = 4000)
    public void testAwaitTerminationWithZeroDurationReturnsFalse() throws Throwable {
        DirectExecutorService executorService = new DirectExecutorService();
        Duration zeroDuration = Duration.ZERO;
        
        boolean terminated = executorService.awaitTermination(zeroDuration);
        assertFalse("Executor service should not terminate immediately with zero duration", terminated);
    }
}
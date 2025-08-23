package org.apache.commons.lang3.concurrent;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.lang.MockException;
import org.junit.runner.RunWith;

public class MultiBackgroundInitializer_ESTestTest16 extends MultiBackgroundInitializer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        MultiBackgroundInitializer multiBackgroundInitializer0 = new MultiBackgroundInitializer();
        multiBackgroundInitializer0.initialize();
        multiBackgroundInitializer0.initialize();
        MultiBackgroundInitializer.MultiBackgroundInitializerResults multiBackgroundInitializer_MultiBackgroundInitializerResults0 = multiBackgroundInitializer0.initialize();
        multiBackgroundInitializer_MultiBackgroundInitializerResults0.isSuccessful();
        String string0 = "";
        TimeUnit timeUnit0 = TimeUnit.NANOSECONDS;
        PriorityBlockingQueue<Runnable> priorityBlockingQueue0 = new PriorityBlockingQueue<Runnable>();
        ThreadPoolExecutor threadPoolExecutor0 = null;
        try {
            threadPoolExecutor0 = new ThreadPoolExecutor(0, 0, 0, timeUnit0, priorityBlockingQueue0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.concurrent.ThreadPoolExecutor", e);
        }
    }
}

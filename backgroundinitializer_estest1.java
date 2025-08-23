package org.apache.commons.lang3.concurrent;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.lang.MockException;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.evosuite.runtime.testdata.FileSystemHandling;
import org.junit.runner.RunWith;

public class BackgroundInitializer_ESTestTest1 extends BackgroundInitializer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        TimeUnit timeUnit0 = TimeUnit.MILLISECONDS;
        SynchronousQueue<Runnable> synchronousQueue0 = new SynchronousQueue<Runnable>();
        ThreadFactory threadFactory0 = mock(ThreadFactory.class, new ViolatedAssumptionAnswer());
        doReturn((Thread) null, (Thread) null).when(threadFactory0).newThread(any(java.lang.Runnable.class));
        ThreadPoolExecutor threadPoolExecutor0 = new ThreadPoolExecutor(1, 1, 0L, timeUnit0, synchronousQueue0, threadFactory0);
        BackgroundInitializer<Delayed> backgroundInitializer0 = new BackgroundInitializer<Delayed>(threadPoolExecutor0);
        // Undeclared exception!
        try {
            backgroundInitializer0.start();
            fail("Expecting exception: RejectedExecutionException");
        } catch (RejectedExecutionException e) {
            //
            // Task java.util.concurrent.FutureTask@e1ff706 rejected from java.util.concurrent.ThreadPoolExecutor@586694bf[Running, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 0]
            //
            verifyException("java.util.concurrent.ThreadPoolExecutor$AbortPolicy", e);
        }
    }
}

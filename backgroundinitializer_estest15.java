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

public class BackgroundInitializer_ESTestTest15 extends BackgroundInitializer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        ForkJoinPool forkJoinPool0 = ForkJoinPool.commonPool();
        BackgroundInitializer<Exception> backgroundInitializer0 = new BackgroundInitializer<Exception>(forkJoinPool0);
        forkJoinPool0.isTerminating();
        BackgroundInitializer<Delayed> backgroundInitializer1 = new BackgroundInitializer<Delayed>();
        MockThrowable mockThrowable0 = new MockThrowable(" L~&");
        ConcurrentException concurrentException0 = new ConcurrentException(mockThrowable0);
        Exception exception0 = backgroundInitializer1.getTypedException(concurrentException0);
        backgroundInitializer0.getTypedException(exception0);
        backgroundInitializer0.isInitialized();
        backgroundInitializer0.isStarted();
        BackgroundInitializer<Object> backgroundInitializer2 = new BackgroundInitializer<Object>(forkJoinPool0);
        // Undeclared exception!
        try {
            backgroundInitializer2.get();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // start() must be called first!
            //
            verifyException("org.apache.commons.lang3.concurrent.BackgroundInitializer", e);
        }
    }
}

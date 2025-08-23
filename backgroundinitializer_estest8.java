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

public class BackgroundInitializer_ESTestTest8 extends BackgroundInitializer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        BackgroundInitializer<Delayed> backgroundInitializer0 = new BackgroundInitializer<Delayed>();
        backgroundInitializer0.start();
        backgroundInitializer0.get();
        ForkJoinPool forkJoinPool0 = new ForkJoinPool();
        // Undeclared exception!
        try {
            backgroundInitializer0.setExternalExecutor(forkJoinPool0);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Cannot set ExecutorService after start()!
            //
            verifyException("org.apache.commons.lang3.concurrent.BackgroundInitializer", e);
        }
    }
}

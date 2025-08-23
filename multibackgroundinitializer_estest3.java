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

public class MultiBackgroundInitializer_ESTestTest3 extends MultiBackgroundInitializer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        ForkJoinPool forkJoinPool0 = ForkJoinPool.commonPool();
        forkJoinPool0.getPoolSize();
        forkJoinPool0.getActiveThreadCount();
        forkJoinPool0.getAsyncMode();
        MultiBackgroundInitializer multiBackgroundInitializer0 = new MultiBackgroundInitializer();
        multiBackgroundInitializer0.isInitialized();
        BackgroundInitializer<MultiBackgroundInitializer.MultiBackgroundInitializerResults> backgroundInitializer0 = new BackgroundInitializer<MultiBackgroundInitializer.MultiBackgroundInitializerResults>();
        multiBackgroundInitializer0.addInitializer("org.apache.commons.lang3.concurrent.MultiBackgroundInitializer$1", backgroundInitializer0);
        multiBackgroundInitializer0.isInitialized();
        multiBackgroundInitializer0.start();
        ForkJoinPool forkJoinPool1 = new ForkJoinPool();
        MultiBackgroundInitializer multiBackgroundInitializer1 = new MultiBackgroundInitializer(forkJoinPool1);
        MultiBackgroundInitializer.MultiBackgroundInitializerResults multiBackgroundInitializer_MultiBackgroundInitializerResults0 = multiBackgroundInitializer1.initialize();
        // Undeclared exception!
        try {
            multiBackgroundInitializer_MultiBackgroundInitializerResults0.getInitializer("dwdf4`-[ZA;");
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            //
            // No child initializer with name dwdf4`-[ZA;
            //
            verifyException("org.apache.commons.lang3.concurrent.MultiBackgroundInitializer$MultiBackgroundInitializerResults", e);
        }
    }
}

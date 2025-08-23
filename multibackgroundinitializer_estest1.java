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

public class MultiBackgroundInitializer_ESTestTest1 extends MultiBackgroundInitializer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        MultiBackgroundInitializer multiBackgroundInitializer0 = new MultiBackgroundInitializer();
        assertFalse(multiBackgroundInitializer0.isStarted());
        MultiBackgroundInitializer.MultiBackgroundInitializerResults multiBackgroundInitializer_MultiBackgroundInitializerResults0 = multiBackgroundInitializer0.initialize();
        multiBackgroundInitializer0.initialize();
        String string0 = "org.apache.commons.lang3.concurrent.MultiBackgroundInitializer$1";
        BackgroundInitializer<MultiBackgroundInitializer.MultiBackgroundInitializerResults> backgroundInitializer0 = new BackgroundInitializer<MultiBackgroundInitializer.MultiBackgroundInitializerResults>();
        ForkJoinPool forkJoinPool0 = new ForkJoinPool();
        forkJoinPool0.isTerminated();
        BackgroundInitializer<String> backgroundInitializer1 = new BackgroundInitializer<String>(forkJoinPool0);
        multiBackgroundInitializer0.addInitializer("", backgroundInitializer1);
        MultiBackgroundInitializer.MultiBackgroundInitializerResults multiBackgroundInitializer_MultiBackgroundInitializerResults1 = multiBackgroundInitializer0.initialize();
        forkJoinPool0.shutdownNow();
        multiBackgroundInitializer_MultiBackgroundInitializerResults1.getException("");
        ForkJoinPool.commonPool();
        multiBackgroundInitializer_MultiBackgroundInitializerResults1.getResultObject("");
        multiBackgroundInitializer_MultiBackgroundInitializerResults0.isSuccessful();
        multiBackgroundInitializer0.isInitialized();
        String string1 = "org.apache.commons.lang3.concurrent.MultiBackgroundInitializer";
        // Undeclared exception!
        try {
            multiBackgroundInitializer_MultiBackgroundInitializerResults0.isException("org.apache.commons.lang3.concurrent.MultiBackgroundInitializer");
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            //
            // No child initializer with name org.apache.commons.lang3.concurrent.MultiBackgroundInitializer
            //
            verifyException("org.apache.commons.lang3.concurrent.MultiBackgroundInitializer$MultiBackgroundInitializerResults", e);
        }
    }
}

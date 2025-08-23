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

public class MultiBackgroundInitializer_ESTestTest9 extends MultiBackgroundInitializer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        MultiBackgroundInitializer multiBackgroundInitializer0 = new MultiBackgroundInitializer();
        boolean boolean0 = multiBackgroundInitializer0.isInitialized();
        assertFalse(boolean0);
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor0 = new ScheduledThreadPoolExecutor(390);
        BackgroundInitializer<Object> backgroundInitializer0 = new BackgroundInitializer<Object>(scheduledThreadPoolExecutor0);
        MockException mockException0 = new MockException();
        backgroundInitializer0.getTypedException(mockException0);
        MultiBackgroundInitializer multiBackgroundInitializer1 = new MultiBackgroundInitializer();
        multiBackgroundInitializer1.start();
        // Undeclared exception!
        try {
            multiBackgroundInitializer1.addInitializer("", backgroundInitializer0);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // addInitializer() must not be called after start()!
            //
            verifyException("org.apache.commons.lang3.concurrent.MultiBackgroundInitializer", e);
        }
    }
}

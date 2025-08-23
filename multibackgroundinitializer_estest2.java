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

public class MultiBackgroundInitializer_ESTestTest2 extends MultiBackgroundInitializer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        MultiBackgroundInitializer multiBackgroundInitializer0 = new MultiBackgroundInitializer();
        int int0 = multiBackgroundInitializer0.getTaskCount();
        assertEquals(1, int0);
        multiBackgroundInitializer0.close();
        multiBackgroundInitializer0.getTaskCount();
        multiBackgroundInitializer0.getTaskCount();
        multiBackgroundInitializer0.isInitialized();
        BackgroundInitializer<Object> backgroundInitializer0 = new BackgroundInitializer<Object>((ExecutorService) null);
        MockException mockException0 = new MockException();
        backgroundInitializer0.getTypedException(mockException0);
        multiBackgroundInitializer0.addInitializer(".%?bGb6EA0/?HHvyV7", backgroundInitializer0);
        multiBackgroundInitializer0.initialize();
        try {
            multiBackgroundInitializer0.initialize();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Cannot set ExecutorService after start()!
            //
            verifyException("org.apache.commons.lang3.concurrent.BackgroundInitializer", e);
        }
    }
}

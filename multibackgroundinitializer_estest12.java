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

public class MultiBackgroundInitializer_ESTestTest12 extends MultiBackgroundInitializer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        MultiBackgroundInitializer multiBackgroundInitializer0 = new MultiBackgroundInitializer();
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor0 = new ScheduledThreadPoolExecutor(3230);
        BackgroundInitializer<Object> backgroundInitializer0 = new BackgroundInitializer<Object>(scheduledThreadPoolExecutor0);
        multiBackgroundInitializer0.addInitializer("@\"f", backgroundInitializer0);
        MultiBackgroundInitializer.MultiBackgroundInitializerResults multiBackgroundInitializer_MultiBackgroundInitializerResults0 = multiBackgroundInitializer0.initialize();
        assertTrue(multiBackgroundInitializer_MultiBackgroundInitializerResults0.isSuccessful());
    }
}

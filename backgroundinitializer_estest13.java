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

public class BackgroundInitializer_ESTestTest13 extends BackgroundInitializer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        ForkJoinPool forkJoinPool0 = ForkJoinPool.commonPool();
        BackgroundInitializer<Object> backgroundInitializer0 = new BackgroundInitializer<Object>(forkJoinPool0);
        backgroundInitializer0.getTaskCount();
        backgroundInitializer0.getTaskCount();
        BackgroundInitializer<Delayed> backgroundInitializer1 = new BackgroundInitializer<Delayed>();
        MockException mockException0 = new MockException("haH:Z~P5");
        StackTraceElement[] stackTraceElementArray0 = new StackTraceElement[4];
        StackTraceElement stackTraceElement0 = new StackTraceElement("'xA", "(V?Q)7A", "{j", 0);
        stackTraceElementArray0[0] = stackTraceElement0;
        StackTraceElement stackTraceElement1 = new StackTraceElement("haH:Z~P5", "qi(qTd2vR~eY:0^sHp", "qi(qTd2vR~eY:0^sHp", (-2077));
        stackTraceElementArray0[1] = stackTraceElement1;
        backgroundInitializer0.initialize();
        StackTraceElement stackTraceElement2 = null;
        try {
            stackTraceElement2 = new StackTraceElement((String) null, "", "", 1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // Declaring class is null
            //
            verifyException("java.util.Objects", e);
        }
    }
}

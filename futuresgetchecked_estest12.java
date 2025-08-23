package com.google.common.util.concurrent;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Delayed;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.lang.MockThread;
import org.junit.runner.RunWith;

public class FuturesGetChecked_ESTestTest12 extends FuturesGetChecked_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        FuturesGetChecked.GetCheckedTypeValidator futuresGetChecked_GetCheckedTypeValidator0 = FuturesGetChecked.classValueValidator();
        ForkJoinPool.ForkJoinWorkerThreadFactory forkJoinPool_ForkJoinWorkerThreadFactory0 = ForkJoinPool.defaultForkJoinWorkerThreadFactory;
        Thread.UncaughtExceptionHandler thread_UncaughtExceptionHandler0 = MockThread.getDefaultUncaughtExceptionHandler();
        ForkJoinPool forkJoinPool0 = new ForkJoinPool(798, forkJoinPool_ForkJoinWorkerThreadFactory0, thread_UncaughtExceptionHandler0, false);
        Callable<Class<Exception>> callable0 = (Callable<Class<Exception>>) mock(Callable.class, new ViolatedAssumptionAnswer());
        doReturn((Object) null).when(callable0).call();
        ForkJoinTask<Class<Exception>> forkJoinTask0 = forkJoinPool0.submit(callable0);
        Class<Exception> class0 = Exception.class;
        Class<Exception> class1 = FuturesGetChecked.getChecked(futuresGetChecked_GetCheckedTypeValidator0, (Future<Class<Exception>>) forkJoinTask0, class0);
        assertNull(class1);
    }
}

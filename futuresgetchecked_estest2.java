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

public class FuturesGetChecked_ESTestTest2 extends FuturesGetChecked_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        Callable<Exception> callable0 = (Callable<Exception>) mock(Callable.class, new ViolatedAssumptionAnswer());
        doReturn((Object) null).when(callable0).call();
        ForkJoinTask<Exception> forkJoinTask0 = ForkJoinTask.adapt((Callable<? extends Exception>) callable0);
        ForkJoinTask<Exception> forkJoinTask1 = forkJoinTask0.fork();
        Class<Exception> class0 = Exception.class;
        TimeUnit timeUnit0 = TimeUnit.MILLISECONDS;
        FuturesGetChecked.getChecked((Future<Exception>) forkJoinTask1, class0, 422L, timeUnit0);
    }
}

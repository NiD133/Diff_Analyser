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

public class FuturesGetChecked_ESTestTest11 extends FuturesGetChecked_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        Supplier<Class<Delayed>> supplier0 = (Supplier<Class<Delayed>>) mock(Supplier.class, new ViolatedAssumptionAnswer());
        doReturn((Object) null).when(supplier0).get();
        CompletableFuture<Class<Delayed>> completableFuture0 = CompletableFuture.supplyAsync(supplier0);
        Class<Exception> class0 = Exception.class;
        FuturesGetChecked.getChecked((Future<Class<Delayed>>) completableFuture0, class0);
    }
}

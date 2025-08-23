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

public class FuturesGetChecked_ESTestTest4 extends FuturesGetChecked_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        FuturesGetChecked.GetCheckedTypeValidatorHolder.WeakSetValidator futuresGetChecked_GetCheckedTypeValidatorHolder_WeakSetValidator0 = FuturesGetChecked.GetCheckedTypeValidatorHolder.WeakSetValidator.INSTANCE;
        Class<Exception> class0 = Exception.class;
        SQLInvalidAuthorizationSpecException sQLInvalidAuthorizationSpecException0 = new SQLInvalidAuthorizationSpecException();
        Supplier<Exception> supplier0 = (Supplier<Exception>) mock(Supplier.class, new ViolatedAssumptionAnswer());
        doReturn(sQLInvalidAuthorizationSpecException0).when(supplier0).get();
        CompletableFuture<Exception> completableFuture0 = CompletableFuture.supplyAsync(supplier0);
        FuturesGetChecked.getChecked((FuturesGetChecked.GetCheckedTypeValidator) futuresGetChecked_GetCheckedTypeValidatorHolder_WeakSetValidator0, (Future<Exception>) completableFuture0, class0);
    }
}

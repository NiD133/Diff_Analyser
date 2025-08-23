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

public class FuturesGetChecked_ESTestTest7 extends FuturesGetChecked_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Class<Exception> class0 = Exception.class;
        FuturesGetChecked.GetCheckedTypeValidator futuresGetChecked_GetCheckedTypeValidator0 = FuturesGetChecked.GetCheckedTypeValidatorHolder.getBestValidator();
        try {
            FuturesGetChecked.getChecked(futuresGetChecked_GetCheckedTypeValidator0, (Future<Delayed>) null, class0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.common.util.concurrent.FuturesGetChecked", e);
        }
    }
}

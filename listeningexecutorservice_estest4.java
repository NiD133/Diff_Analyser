package com.google.common.util.concurrent;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.time.Duration;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class ListeningExecutorService_ESTestTest4 extends ListeningExecutorService_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test3() throws Throwable {
        DirectExecutorService directExecutorService0 = new DirectExecutorService();
        Duration duration0 = Duration.ofMinutes((-356L));
        // Undeclared exception!
        try {
            directExecutorService0.invokeAll((Collection<? extends Callable<Object>>) null, duration0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.concurrent.AbstractExecutorService", e);
        }
    }
}

package org.apache.commons.io.function;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.time.chrono.HijrahEra;
import java.util.Comparator;
import java.util.concurrent.ForkJoinTask;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.LongStream;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class Uncheck_ESTestTest23 extends Uncheck_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        IOBiFunction<String, String, String> iOBiFunction0 = (IOBiFunction<String, String, String>) mock(IOBiFunction.class, new ViolatedAssumptionAnswer());
        doReturn((Object) null).when(iOBiFunction0).apply(anyString(), anyString());
        String string0 = Uncheck.apply(iOBiFunction0, "T;ot>5MR", "");
        assertNull(string0);
    }
}

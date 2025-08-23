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

public class Uncheck_ESTestTest16 extends Uncheck_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        IOComparator<String> iOComparator0 = (IOComparator<String>) mock(IOComparator.class, new ViolatedAssumptionAnswer());
        doReturn((-2057)).when(iOComparator0).compare(anyString(), anyString());
        int int0 = Uncheck.compare(iOComparator0, (String) null, "");
        assertEquals(0, int0);
    }
}

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

public class Uncheck_ESTestTest13 extends Uncheck_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        IOIntSupplier iOIntSupplier0 = mock(IOIntSupplier.class, new ViolatedAssumptionAnswer());
        doReturn(1165).when(iOIntSupplier0).getAsInt();
        int int0 = Uncheck.getAsInt(iOIntSupplier0);
        assertEquals(1165, int0);
    }
}

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

public class Uncheck_ESTestTest45 extends Uncheck_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test44() throws Throwable {
        IOIntSupplier iOIntSupplier0 = mock(IOIntSupplier.class, new ViolatedAssumptionAnswer());
        doReturn((-1214)).when(iOIntSupplier0).getAsInt();
        Supplier<String> supplier0 = (Supplier<String>) mock(Supplier.class, new ViolatedAssumptionAnswer());
        IOBiConsumer<String, String> iOBiConsumer0 = IOBiConsumer.noop();
        BiConsumer<String, String> biConsumer0 = iOBiConsumer0.asBiConsumer();
        Comparator<String> comparator0 = (Comparator<String>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        BinaryOperator<String> binaryOperator0 = BinaryOperator.minBy((Comparator<? super String>) comparator0);
        IOFunction<String, String> iOFunction0 = IOFunction.identity();
        Function<String, String> function0 = iOFunction0.asFunction();
        Collector.Characteristics[] collector_CharacteristicsArray0 = (Collector.Characteristics[]) Array.newInstance(Collector.Characteristics.class, 0);
        Collector<String, String, String> collector0 = Collector.of(supplier0, biConsumer0, binaryOperator0, function0, (Collector.Characteristics[]) collector_CharacteristicsArray0);
        Supplier<String> supplier1 = collector0.supplier();
        int int0 = Uncheck.getAsInt(iOIntSupplier0, supplier1);
        assertEquals((-1214), int0);
    }
}

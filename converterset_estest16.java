package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.Seconds;
import org.joda.time.chrono.CopticChronology;
import org.junit.runner.RunWith;

public class ConverterSet_ESTestTest16 extends ConverterSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        Converter[] converterArray0 = new Converter[1];
        NullConverter nullConverter0 = NullConverter.INSTANCE;
        converterArray0[0] = (Converter) nullConverter0;
        ConverterSet converterSet0 = new ConverterSet(converterArray0);
        Class<ConverterSet.Entry> class0 = ConverterSet.Entry.class;
        Converter converter0 = converterSet0.select(class0);
        assertNull(converter0);
    }
}

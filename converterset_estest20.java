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

public class ConverterSet_ESTestTest20 extends ConverterSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        Converter[] converterArray0 = new Converter[5];
        LongConverter longConverter0 = LongConverter.INSTANCE;
        converterArray0[0] = (Converter) longConverter0;
        ReadablePartialConverter readablePartialConverter0 = new ReadablePartialConverter();
        converterArray0[1] = (Converter) readablePartialConverter0;
        CalendarConverter calendarConverter0 = new CalendarConverter();
        converterArray0[2] = (Converter) calendarConverter0;
        ReadableIntervalConverter readableIntervalConverter0 = new ReadableIntervalConverter();
        converterArray0[3] = (Converter) readableIntervalConverter0;
        converterArray0[4] = (Converter) readableIntervalConverter0;
        ConverterSet converterSet0 = new ConverterSet(converterArray0);
        Class<Long> class0 = Long.class;
        Converter converter0 = converterSet0.select(class0);
        assertSame(converter0, longConverter0);
    }
}

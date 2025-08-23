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

public class ConverterSet_ESTestTest2 extends ConverterSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        Converter[] converterArray0 = new Converter[8];
        ReadableInstantConverter readableInstantConverter0 = new ReadableInstantConverter();
        converterArray0[0] = (Converter) readableInstantConverter0;
        ReadableIntervalConverter readableIntervalConverter0 = new ReadableIntervalConverter();
        converterArray0[1] = (Converter) readableIntervalConverter0;
        ReadablePeriodConverter readablePeriodConverter0 = new ReadablePeriodConverter();
        converterArray0[2] = (Converter) readablePeriodConverter0;
        CalendarConverter calendarConverter0 = new CalendarConverter();
        converterArray0[3] = (Converter) calendarConverter0;
        CalendarConverter calendarConverter1 = new CalendarConverter();
        ConverterSet converterSet0 = new ConverterSet(converterArray0);
        ConverterSet converterSet1 = converterSet0.add(calendarConverter1, converterArray0);
        assertNotSame(converterSet1, converterSet0);
    }
}

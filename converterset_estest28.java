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

public class ConverterSet_ESTestTest28 extends ConverterSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        Class<ConverterSet.Entry> class0 = ConverterSet.Entry.class;
        ReadablePartialConverter readablePartialConverter0 = new ReadablePartialConverter();
        ConverterSet.Entry converterSet_Entry0 = new ConverterSet.Entry(class0, readablePartialConverter0);
        CopticChronology copticChronology0 = CopticChronology.getInstance();
        Interval interval0 = null;
        try {
            interval0 = new Interval(converterSet_Entry0, copticChronology0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // No interval converter found for type: org.joda.time.convert.ConverterSet$Entry
            //
            verifyException("org.joda.time.convert.ConverterManager", e);
        }
    }
}

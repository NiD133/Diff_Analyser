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

public class ConverterSet_ESTestTest15 extends ConverterSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        StringConverter stringConverter0 = StringConverter.INSTANCE;
        Converter[] converterArray0 = new Converter[0];
        ConverterSet converterSet0 = new ConverterSet(converterArray0);
        // Undeclared exception!
        try {
            converterSet0.add(stringConverter0, converterArray0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 0
            //
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }
}

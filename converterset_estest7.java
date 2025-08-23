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

public class ConverterSet_ESTestTest7 extends ConverterSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Converter[] converterArray0 = new Converter[1];
        ConverterSet converterSet0 = new ConverterSet(converterArray0);
        // Undeclared exception!
        try {
            converterSet0.remove(converterArray0[0], converterArray0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }
}

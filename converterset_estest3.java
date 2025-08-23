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

public class ConverterSet_ESTestTest3 extends ConverterSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Hours hours0 = Hours.ONE;
        Seconds seconds0 = hours0.toStandardSeconds();
        PeriodType periodType0 = seconds0.getPeriodType();
        MutablePeriod mutablePeriod0 = new MutablePeriod((Object) null, periodType0);
    }
}

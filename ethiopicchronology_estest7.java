package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class EthiopicChronology_ESTestTest7 extends EthiopicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        EthiopicChronology ethiopicChronology0 = EthiopicChronology.getInstanceUTC();
        long long0 = ethiopicChronology0.calculateFirstDayOfYearMillis(15271875);
        assertEquals(481881796790400000L, long0);
    }
}
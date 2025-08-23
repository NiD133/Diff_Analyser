package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class EthiopicChronology_ESTestTest15 extends EthiopicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        DateTimeZone dateTimeZone0 = DateTimeZone.forOffsetMillis((-2263));
        EthiopicChronology ethiopicChronology0 = EthiopicChronology.getInstance(dateTimeZone0);
        boolean boolean0 = ethiopicChronology0.isLeapDay(1209600011L);
        assertFalse(boolean0);
    }
}

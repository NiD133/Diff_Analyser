package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class EthiopicChronology_ESTestTest9 extends EthiopicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        DateTimeZone dateTimeZone0 = DateTimeZone.forOffsetMillis((-1962));
        EthiopicChronology ethiopicChronology0 = EthiopicChronology.getInstance(dateTimeZone0, 1);
        assertEquals(1, EthiopicChronology.EE);
    }
}

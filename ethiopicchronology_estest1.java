package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class EthiopicChronology_ESTestTest1 extends EthiopicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        EthiopicChronology ethiopicChronology0 = EthiopicChronology.getInstance((DateTimeZone) null);
        boolean boolean0 = ethiopicChronology0.isLeapDay((-56623968000000L));
        assertFalse(boolean0);
    }
}

package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class EthiopicChronology_ESTestTest16 extends EthiopicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        EthiopicChronology ethiopicChronology0 = EthiopicChronology.getInstance((DateTimeZone) null);
        Chronology chronology0 = ethiopicChronology0.withZone((DateTimeZone) null);
        assertSame(ethiopicChronology0, chronology0);
    }
}

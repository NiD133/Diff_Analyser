package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class EthiopicChronology_ESTestTest13 extends EthiopicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        DateTimeZone dateTimeZone0 = DateTimeZone.forOffsetMillis((-1962));
        EthiopicChronology ethiopicChronology0 = EthiopicChronology.getInstance(dateTimeZone0);
        LenientChronology lenientChronology0 = LenientChronology.getInstance(ethiopicChronology0);
        Object object0 = new Object();
        EthiopicChronology ethiopicChronology1 = new EthiopicChronology(lenientChronology0, object0, 1);
        assertFalse(ethiopicChronology1.equals((Object) ethiopicChronology0));
    }
}

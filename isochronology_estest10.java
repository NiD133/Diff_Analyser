package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class ISOChronology_ESTestTest10 extends ISOChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        ISOChronology iSOChronology0 = ISOChronology.getInstanceUTC();
        DateTimeZone dateTimeZone0 = DateTimeZone.forOffsetMillis((-1322));
        Chronology chronology0 = iSOChronology0.withZone(dateTimeZone0);
        boolean boolean0 = iSOChronology0.equals(chronology0);
        assertFalse(boolean0);
    }
}

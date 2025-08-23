package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class CopticChronology_ESTestTest17 extends CopticChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        DateTimeZone dateTimeZone0 = DateTimeZone.forOffsetMillis((-1686));
        CopticChronology copticChronology0 = CopticChronology.getInstance(dateTimeZone0, 7);
        Chronology chronology0 = copticChronology0.withZone(dateTimeZone0);
        assertSame(chronology0, copticChronology0);
    }
}
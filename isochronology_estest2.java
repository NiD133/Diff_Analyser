package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class ISOChronology_ESTestTest2 extends ISOChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        ISOChronology iSOChronology0 = ISOChronology.getInstance();
        DateTimeZone dateTimeZone0 = DateTimeZone.forOffsetMillis(7593750);
        ISOChronology iSOChronology1 = (ISOChronology) iSOChronology0.withZone(dateTimeZone0);
        AssembledChronology.Fields assembledChronology_Fields0 = new AssembledChronology.Fields();
        iSOChronology1.assemble(assembledChronology_Fields0);
        assertNotSame(iSOChronology1, iSOChronology0);
    }
}

package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class ISOChronology_ESTestTest6 extends ISOChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        ISOChronology iSOChronology0 = ISOChronology.getInstanceUTC();
        String string0 = iSOChronology0.toString();
        assertEquals("ISOChronology[UTC]", string0);
    }
}

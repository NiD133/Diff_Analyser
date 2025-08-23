package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class CopticChronology_ESTestTest5 extends CopticChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        CopticChronology copticChronology0 = CopticChronology.getInstanceUTC();
        CopticChronology copticChronology1 = null;
        try {
            copticChronology1 = new CopticChronology(copticChronology0, copticChronology0, 634);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Invalid min days in first week: 634
            //
            verifyException("org.joda.time.chrono.BasicChronology", e);
        }
    }
}

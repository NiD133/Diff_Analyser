package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class CopticChronology_ESTestTest4 extends CopticChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        CopticChronology copticChronology0 = CopticChronology.getInstance();
        // Undeclared exception!
        try {
            copticChronology0.isLeapDay((-9223372036854775768L));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // The instant is below the supported minimum of 0001-01-01T00:00:00.000Z (CopticChronology[UTC])
            //
            verifyException("org.joda.time.chrono.LimitChronology", e);
        }
    }
}

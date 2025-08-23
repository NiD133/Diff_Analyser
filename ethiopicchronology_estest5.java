package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class EthiopicChronology_ESTestTest5 extends EthiopicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        EthiopicChronology ethiopicChronology0 = EthiopicChronology.getInstanceUTC();
        // Undeclared exception!
        try {
            ethiopicChronology0.isLeapDay((-9223372036854775808L));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // The instant is below the supported minimum of 0001-01-01T00:00:00.000Z (EthiopicChronology[UTC])
            //
            verifyException("org.joda.time.chrono.LimitChronology", e);
        }
    }
}

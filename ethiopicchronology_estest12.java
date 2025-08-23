package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class EthiopicChronology_ESTestTest12 extends EthiopicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        EthiopicChronology ethiopicChronology0 = null;
        try {
            ethiopicChronology0 = new EthiopicChronology((Chronology) null, (Object) null, (-159));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Invalid min days in first week: -159
            //
            verifyException("org.joda.time.chrono.BasicChronology", e);
        }
    }
}

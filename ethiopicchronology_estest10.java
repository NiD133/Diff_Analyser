package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class EthiopicChronology_ESTestTest10 extends EthiopicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        DateTimeZone dateTimeZone0 = DateTimeZone.forOffsetMillis(1900);
        // Undeclared exception!
        try {
            EthiopicChronology.getInstance(dateTimeZone0, (-4898));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Invalid min days in first week: -4898
            //
            verifyException("org.joda.time.chrono.EthiopicChronology", e);
        }
    }
}

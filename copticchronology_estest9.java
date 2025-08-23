package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class CopticChronology_ESTestTest9 extends CopticChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        DateTimeZone dateTimeZone0 = DateTimeZone.forOffsetMillis((-1686));
        // Undeclared exception!
        try {
            CopticChronology.getInstance(dateTimeZone0, (-1686));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Invalid min days in first week: -1686
            //
            verifyException("org.joda.time.chrono.CopticChronology", e);
        }
    }
}

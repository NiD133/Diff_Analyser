package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.TimeZone;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.junit.runner.RunWith;

public class GregorianChronology_ESTestTest11 extends GregorianChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        DateTimeZone dateTimeZone0 = DateTimeZone.forOffsetMillis(531);
        // Undeclared exception!
        try {
            GregorianChronology.getInstance(dateTimeZone0, 531);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Invalid min days in first week: 531
            //
            verifyException("org.joda.time.chrono.GregorianChronology", e);
        }
    }
}

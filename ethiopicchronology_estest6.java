package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class EthiopicChronology_ESTestTest6 extends EthiopicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        DateTimeZone dateTimeZone0 = DateTimeZone.forOffsetMillis((-4246));
        EthiopicChronology ethiopicChronology0 = EthiopicChronology.getInstance(dateTimeZone0);
        // Undeclared exception!
        try {
            ethiopicChronology0.isLeapDay((-9223372036854775808L));
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // Adding time zone offset caused overflow
            //
            verifyException("org.joda.time.DateTimeZone", e);
        }
    }
}

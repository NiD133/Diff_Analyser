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

public class GregorianChronology_ESTestTest4 extends GregorianChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        GregorianChronology gregorianChronology0 = GregorianChronology.getInstance();
        boolean boolean0 = gregorianChronology0.isLeapYear((-900));
        assertFalse(boolean0);
    }
}

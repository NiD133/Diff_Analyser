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

public class GregorianChronology_ESTestTest18 extends GregorianChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        GregorianChronology gregorianChronology0 = GregorianChronology.getInstance();
        long long0 = gregorianChronology0.calculateFirstDayOfYearMillis(3000);
        assertEquals(32503680000000L, long0);
    }
}

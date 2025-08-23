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

public class GregorianChronology_ESTestTest22 extends GregorianChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        TimeZone timeZone0 = TimeZone.getTimeZone("tySc3*yvDsM");
        DateTimeZone dateTimeZone0 = DateTimeZone.forTimeZone(timeZone0);
        GregorianChronology gregorianChronology0 = GregorianChronology.getInstance(dateTimeZone0, 1);
        int int0 = gregorianChronology0.getMinYear();
        assertEquals((-292275054), int0);
    }
}

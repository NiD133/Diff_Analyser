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

public class GregorianChronology_ESTestTest10 extends GregorianChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        GregorianChronology gregorianChronology0 = GregorianChronology.getInstanceUTC();
        LocalDateTime localDateTime0 = new LocalDateTime(199L, (Chronology) gregorianChronology0);
        DateTimeZone dateTimeZone0 = DateTimeZone.forOffsetMillis((-3631));
        DateTime dateTime0 = localDateTime0.toDateTime(dateTimeZone0);
        assertEquals(3830L, dateTime0.getMillis());
    }
}

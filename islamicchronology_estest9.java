package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.TimeZone;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.tz.UTCProvider;
import org.junit.runner.RunWith;

public class IslamicChronology_ESTestTest9 extends IslamicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        DateTimeZone dateTimeZone0 = DateTimeZone.getDefault();
        IslamicChronology islamicChronology0 = IslamicChronology.getInstance(dateTimeZone0);
        long long0 = islamicChronology0.setYear((-42246144000000L), 1900);
        assertEquals(15651100800000L, long0);
    }
}

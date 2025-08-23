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

public class IslamicChronology_ESTestTest49 extends IslamicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test48() throws Throwable {
        IslamicChronology islamicChronology0 = IslamicChronology.getInstance();
        long long0 = islamicChronology0.setYear(2602045900800000L, 46);
        assertEquals((-41113267200000L), long0);
    }
}

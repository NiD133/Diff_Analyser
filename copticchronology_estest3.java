package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class CopticChronology_ESTestTest3 extends CopticChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        CopticChronology copticChronology0 = CopticChronology.getInstance();
        long long0 = copticChronology0.getApproxMillisAtEpochDividedByTwo();
        assertEquals(26607895200000L, long0);
    }
}

package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.time.Clock;
// ... numerous other unused imports
import org.evosuite.runtime.mock.java.time.chrono.MockJapaneseDate;
import org.junit.runner.RunWith;

public class JulianChronology_ESTestTest8 extends JulianChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        JulianChronology julianChronology0 = new JulianChronology();
        JulianEra julianEra0 = julianChronology0.eraOf(0);
        assertEquals(JulianEra.BC, julianEra0);
    }
}
package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.time.Clock;
import java.time.DateTimeException;
// ... other imports
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
// ... other imports
import org.junit.runner.RunWith;

public class InternationalFixedChronology_ESTestTest39 extends InternationalFixedChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        InternationalFixedChronology internationalFixedChronology0 = new InternationalFixedChronology();
        // Undeclared exception!
        try {
            internationalFixedChronology0.date(4, 4, 29);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            //
            // Invalid date: 4/4/29
            //
            verifyException("org.threeten.extra.chrono.InternationalFixedDate", e);
        }
    }
}
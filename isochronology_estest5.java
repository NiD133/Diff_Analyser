package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class ISOChronology_ESTestTest5 extends ISOChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        ISOChronology iSOChronology0 = ISOChronology.getInstance();
        // Undeclared exception!
        try {
            iSOChronology0.assemble((AssembledChronology.Fields) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.joda.time.chrono.ISOChronology", e);
        }
    }
}
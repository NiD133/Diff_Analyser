package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class CopticChronology_ESTestTest2 extends CopticChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        DateTimeZone dateTimeZone0 = DateTimeZone.getDefault();
        CopticChronology copticChronology0 = CopticChronology.getInstance(dateTimeZone0);
        AssembledChronology.Fields assembledChronology_Fields0 = new AssembledChronology.Fields();
        copticChronology0.assemble(assembledChronology_Fields0);
        assertEquals(1, CopticChronology.AM);
    }
}

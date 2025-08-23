package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class EthiopicChronology_ESTestTest2 extends EthiopicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        EthiopicChronology ethiopicChronology0 = EthiopicChronology.getInstanceUTC();
        AssembledChronology.Fields assembledChronology_Fields0 = new AssembledChronology.Fields();
        ethiopicChronology0.assemble(assembledChronology_Fields0);
        assertEquals(1, EthiopicChronology.EE);
    }
}

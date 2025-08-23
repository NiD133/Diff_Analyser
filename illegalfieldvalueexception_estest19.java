package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IllegalFieldValueException_ESTestTest19 extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        Long long0 = new Long((-42521587200000L));
        IllegalFieldValueException illegalFieldValueException0 = new IllegalFieldValueException("nrMiC", (Number) null, (Number) null, long0);
        illegalFieldValueException0.getIllegalNumberValue();
        assertEquals("Value null for nrMiC must not be larger than -42521587200000", illegalFieldValueException0.getMessage());
    }
}

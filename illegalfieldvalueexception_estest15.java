package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IllegalFieldValueException_ESTestTest15 extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        IllegalFieldValueException illegalFieldValueException0 = new IllegalFieldValueException("", (Number) null, (Number) null, (Number) null);
        String string0 = illegalFieldValueException0.getIllegalValueAsString();
        assertEquals("Value null for  is not supported", illegalFieldValueException0.getMessage());
        assertNotNull(string0);
    }
}

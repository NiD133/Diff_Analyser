package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IllegalFieldValueException_ESTestTest22 extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        IllegalFieldValueException illegalFieldValueException0 = new IllegalFieldValueException("", (Number) null, (Number) null, (Number) null);
        illegalFieldValueException0.getIllegalStringValue();
        assertEquals("Value null for  is not supported", illegalFieldValueException0.getMessage());
    }
}

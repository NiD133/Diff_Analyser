package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IllegalFieldValueException_ESTestTest14 extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        IllegalFieldValueException illegalFieldValueException0 = new IllegalFieldValueException("", (Number) null, (Number) null, (Number) null);
        illegalFieldValueException0.prependMessage("");
        assertEquals(": Value null for  is not supported", illegalFieldValueException0.getMessage());
    }
}

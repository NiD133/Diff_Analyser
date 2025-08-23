package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IllegalFieldValueException_ESTestTest13 extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        Byte byte0 = new Byte((byte) 0);
        IllegalFieldValueException illegalFieldValueException0 = new IllegalFieldValueException("", byte0, byte0, byte0);
        illegalFieldValueException0.prependMessage((String) null);
        assertEquals("Value 0 for  must be in the range [0,0]", illegalFieldValueException0.getMessage());
    }
}

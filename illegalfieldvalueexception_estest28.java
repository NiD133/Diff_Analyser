package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IllegalFieldValueException_ESTestTest28 extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        IllegalFieldValueException illegalFieldValueException0 = new IllegalFieldValueException("sg", "sg");
        illegalFieldValueException0.getDateTimeFieldType();
        assertEquals("Value \"sg\" for sg is not supported", illegalFieldValueException0.getMessage());
    }
}

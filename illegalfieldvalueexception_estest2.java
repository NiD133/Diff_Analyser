package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IllegalFieldValueException_ESTestTest2 extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        DurationFieldType durationFieldType0 = DurationFieldType.CENTURIES_TYPE;
        IllegalFieldValueException illegalFieldValueException0 = new IllegalFieldValueException(durationFieldType0, "Value ");
        illegalFieldValueException0.getIllegalStringValue();
        assertEquals("Value \"Value \" for centuries is not supported", illegalFieldValueException0.getMessage());
    }
}

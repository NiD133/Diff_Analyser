package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IllegalFieldValueException_ESTestTest6 extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.secondOfMinute();
        IllegalFieldValueException illegalFieldValueException0 = new IllegalFieldValueException(dateTimeFieldType0, "");
        illegalFieldValueException0.getDateTimeFieldType();
        assertEquals("Value \"\" for secondOfMinute is not supported", illegalFieldValueException0.getMessage());
    }
}

package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IllegalFieldValueException_ESTestTest23 extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.weekyear();
        Long long0 = new Long((-42521587200000L));
        IllegalFieldValueException illegalFieldValueException0 = new IllegalFieldValueException(dateTimeFieldType0, long0, long0, long0);
        String string0 = illegalFieldValueException0.getMessage();
        assertEquals("Value -42521587200000 for weekyear must be in the range [-42521587200000,-42521587200000]", string0);
    }
}

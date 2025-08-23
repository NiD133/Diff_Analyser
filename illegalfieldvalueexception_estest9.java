package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IllegalFieldValueException_ESTestTest9 extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        IllegalFieldValueException illegalFieldValueException0 = null;
        try {
            illegalFieldValueException0 = new IllegalFieldValueException((DateTimeFieldType) null, "4s1_{!");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.joda.time.IllegalFieldValueException", e);
        }
    }
}

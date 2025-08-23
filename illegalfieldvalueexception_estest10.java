package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IllegalFieldValueException_ESTestTest10 extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        Float float0 = new Float(2440587.5);
        IllegalFieldValueException illegalFieldValueException0 = null;
        try {
            illegalFieldValueException0 = new IllegalFieldValueException((DateTimeFieldType) null, float0, "");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.joda.time.IllegalFieldValueException", e);
        }
    }
}

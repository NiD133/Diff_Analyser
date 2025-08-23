package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IllegalFieldValueException_ESTestTest4 extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        IllegalFieldValueException illegalFieldValueException0 = new IllegalFieldValueException((String) null, (Number) null, (Number) null, (Number) null);
        illegalFieldValueException0.getFieldName();
        assertEquals("Value null for null is not supported", illegalFieldValueException0.getMessage());
    }
}

package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IllegalFieldValueException_ESTestTest18 extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        Float float0 = new Float(0.0F);
        IllegalFieldValueException illegalFieldValueException0 = new IllegalFieldValueException("", (Number) null, float0, (Number) null);
    }
}

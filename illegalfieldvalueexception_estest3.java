package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IllegalFieldValueException_ESTestTest3 extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        IllegalFieldValueException illegalFieldValueException0 = new IllegalFieldValueException("ZON+$0'' {[}Z#94 eu", "");
        illegalFieldValueException0.getIllegalStringValue();
        assertEquals("Value \"\" for ZON+$0'' {[}Z#94 eu is not supported", illegalFieldValueException0.getMessage());
    }
}

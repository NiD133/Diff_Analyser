package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSetUtils_ESTestTest1 extends CharSetUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        String[] stringArray0 = new String[4];
        stringArray0[0] = "0-9";
        String string0 = CharSetUtils.squeeze("offset cannot be negative", stringArray0);
        assertEquals("offset cannot be negative", string0);
    }
}

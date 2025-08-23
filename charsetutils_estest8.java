package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSetUtils_ESTestTest8 extends CharSetUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        String[] stringArray0 = new String[2];
        stringArray0[0] = "h|Bv_9mUP7'&Y";
        String string0 = CharSetUtils.squeeze("Aborting to protect against StackOverflowError - output of one loop is the input of another", stringArray0);
        assertEquals("Aborting to protect against StackOverflowError - output of one loop is the input of another", string0);
    }
}

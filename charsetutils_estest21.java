package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSetUtils_ESTestTest21 extends CharSetUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        String[] stringArray0 = new String[2];
        stringArray0[0] = "h|Bv_9mUP7'&Y";
        boolean boolean0 = CharSetUtils.containsAny("h|Bv_9mUP7'&Y", stringArray0);
        assertTrue(boolean0);
    }
}

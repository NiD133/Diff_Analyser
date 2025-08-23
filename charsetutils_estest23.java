package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSetUtils_ESTestTest23 extends CharSetUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        String[] stringArray0 = new String[7];
        boolean boolean0 = CharSetUtils.containsAny("", stringArray0);
        assertFalse(boolean0);
    }
}

package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSetUtils_ESTestTest10 extends CharSetUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        String[] stringArray0 = new String[8];
        String string0 = CharSetUtils.squeeze((String) null, stringArray0);
        assertNull(string0);
    }
}

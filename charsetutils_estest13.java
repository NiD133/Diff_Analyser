package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSetUtils_ESTestTest13 extends CharSetUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        String[] stringArray0 = new String[5];
        String string0 = CharSetUtils.keep((String) null, stringArray0);
        assertNull(string0);
    }
}

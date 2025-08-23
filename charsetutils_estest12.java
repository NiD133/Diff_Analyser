package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSetUtils_ESTestTest12 extends CharSetUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        String[] stringArray0 = new String[4];
        String string0 = CharSetUtils.keep("", stringArray0);
        assertEquals("", string0);
    }
}

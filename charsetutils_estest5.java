package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSetUtils_ESTestTest5 extends CharSetUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        String[] stringArray0 = new String[2];
        stringArray0[0] = ",']t[O'q)5c-[";
        String string0 = CharSetUtils.squeeze("...", stringArray0);
        assertEquals("...", string0);
    }
}

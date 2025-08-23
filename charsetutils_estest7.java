package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSetUtils_ESTestTest7 extends CharSetUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        String[] stringArray0 = new String[2];
        stringArray0[0] = "Jk{Z^]6e/!S>zlTbb#wl";
        String string0 = CharSetUtils.squeeze("offset cannot be negative", stringArray0);
        assertEquals("ofset canot be negative", string0);
    }
}

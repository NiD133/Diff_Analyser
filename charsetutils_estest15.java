package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSetUtils_ESTestTest15 extends CharSetUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        String[] stringArray0 = new String[8];
        stringArray0[0] = "A-Z";
        String string0 = CharSetUtils.delete("A-Z", stringArray0);
        assertEquals("-", string0);
    }
}

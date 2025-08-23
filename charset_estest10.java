package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSet_ESTestTest10 extends CharSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        String[] stringArray0 = new String[9];
        stringArray0[5] = "o|pC ~`^YDc:";
        CharSet charSet0 = CharSet.getInstance(stringArray0);
        assertNotNull(charSet0);
    }
}

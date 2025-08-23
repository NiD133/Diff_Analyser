package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSet_ESTestTest9 extends CharSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        String[] stringArray0 = new String[9];
        stringArray0[2] = "pDqg&f|G+l]-#pX9-?k";
        CharSet charSet0 = CharSet.getInstance(stringArray0);
        boolean boolean0 = charSet0.contains('T');
        assertTrue(boolean0);
    }
}

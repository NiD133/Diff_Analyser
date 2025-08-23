package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSet_ESTestTest8 extends CharSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        String[] stringArray0 = new String[9];
        CharSet charSet0 = CharSet.getInstance(stringArray0);
        Object object0 = new Object();
        boolean boolean0 = charSet0.equals(object0);
        assertFalse(boolean0);
    }
}

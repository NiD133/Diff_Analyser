package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSet_ESTestTest7 extends CharSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        CharSet charSet0 = CharSet.ASCII_ALPHA_UPPER;
        boolean boolean0 = charSet0.equals(charSet0);
        assertTrue(boolean0);
    }
}

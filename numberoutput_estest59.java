package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest59 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test58() throws Throwable {
        String string0 = NumberOutput.toString((double) 9007199254740992000L, true);
        assertEquals("9.007199254740992E18", string0);
    }
}

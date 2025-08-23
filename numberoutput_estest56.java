package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest56 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test55() throws Throwable {
        boolean boolean0 = NumberOutput.notFinite(2650.0F);
        assertFalse(boolean0);
    }
}

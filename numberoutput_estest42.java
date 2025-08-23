package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest42 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test41() throws Throwable {
        byte[] byteArray0 = new byte[16];
        // Undeclared exception!
        try {
            NumberOutput.outputInt(2084322301, byteArray0, 2084322301);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 2084322301
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}

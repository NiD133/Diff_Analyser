package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest76 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test75() throws Throwable {
        char[] charArray0 = new char[14];
        // Undeclared exception!
        try {
            NumberOutput.outputInt(Integer.MIN_VALUE, charArray0, Integer.MIN_VALUE);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }
}

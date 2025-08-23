package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest77 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test76() throws Throwable {
        // Undeclared exception!
        try {
            NumberOutput.outputLong((-9223372036854775768L), (char[]) null, (-1467));
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
        }
    }
}

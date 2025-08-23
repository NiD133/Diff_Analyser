package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Comparator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SignedBytes_ESTestTest16 extends SignedBytes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        // Undeclared exception!
        try {
            SignedBytes.join("Out of range: %s", (byte[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.common.primitives.SignedBytes", e);
        }
    }
}

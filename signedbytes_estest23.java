package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Comparator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SignedBytes_ESTestTest23 extends SignedBytes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        byte[] byteArray0 = new byte[0];
        // Undeclared exception!
        try {
            SignedBytes.max(byteArray0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.common.base.Preconditions", e);
        }
    }
}

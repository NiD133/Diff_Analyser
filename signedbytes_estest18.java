package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Comparator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SignedBytes_ESTestTest18 extends SignedBytes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        byte[] byteArray0 = new byte[13];
        // Undeclared exception!
        try {
            SignedBytes.sortDescending(byteArray0, (int) (byte) 127, (int) (byte) 127);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // java.lang.String@0000000001 (java.lang.Integer@0000000002) must not be greater than size (java.lang.Integer@0000000003)
            //
            verifyException("com.google.common.base.Preconditions", e);
        }
    }
}

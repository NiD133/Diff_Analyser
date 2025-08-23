package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Comparator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SignedBytes_ESTestTest29 extends SignedBytes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        // Undeclared exception!
        try {
            SignedBytes.checkedCast((-1826L));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Out of range: java.lang.Long@0000000001
            //
            verifyException("com.google.common.base.Preconditions", e);
        }
    }
}

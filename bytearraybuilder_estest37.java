package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest37 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test36() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = ByteArrayBuilder.fromInitial((byte[]) null, 1495);
        // Undeclared exception!
        try {
            byteArrayBuilder0.appendThreeBytes(4000);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }
}
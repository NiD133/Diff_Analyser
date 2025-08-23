package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest40 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test39() throws Throwable {
        byte[] byteArray0 = new byte[1];
        ByteArrayBuilder byteArrayBuilder0 = ByteArrayBuilder.fromInitial(byteArray0, (byte) (-1));
        // Undeclared exception!
        try {
            byteArrayBuilder0.appendFourBytes((byte) (-1));
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // -1
            //
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }
}
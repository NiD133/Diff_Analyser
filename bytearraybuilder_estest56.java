package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest56 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test55() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder();
        byte[] byteArray0 = byteArrayBuilder0.completeAndCoalesce(500);
        // Undeclared exception!
        try {
            byteArrayBuilder0.write(byteArray0, 500, 500);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }
}

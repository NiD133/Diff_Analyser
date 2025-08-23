package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest26 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder();
        ByteArrayBuilder byteArrayBuilder1 = ByteArrayBuilder.fromInitial(byteArrayBuilder0.NO_BYTES, (-538));
        // Undeclared exception!
        try {
            byteArrayBuilder1.toByteArray();
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }
}

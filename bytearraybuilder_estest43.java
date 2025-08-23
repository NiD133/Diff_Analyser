package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest43 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test42() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = null;
        try {
            byteArrayBuilder0 = new ByteArrayBuilder((BufferRecycler) null, (-1216));
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }
}

package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest29 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder();
        byteArrayBuilder0.setCurrentSegmentLength((-1));
        // Undeclared exception!
        try {
            byteArrayBuilder0.getClearAndRelease();
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }
}

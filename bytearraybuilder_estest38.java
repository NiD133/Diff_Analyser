package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest38 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test37() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder();
        byteArrayBuilder0.setCurrentSegmentLength((-870));
        // Undeclared exception!
        try {
            byteArrayBuilder0.appendThreeBytes((-2056));
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // -870
            //
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }
}

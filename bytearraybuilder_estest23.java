package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest23 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder(130740);
        byteArrayBuilder0.setCurrentSegmentLength((-2165));
        byte[] byteArray0 = new byte[1];
        // Undeclared exception!
        try {
            byteArrayBuilder0.write(byteArray0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }
}

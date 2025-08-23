package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest21 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        CircularByteBuffer circularByteBuffer0 = null;
        try {
            circularByteBuffer0 = new CircularByteBuffer((-21));
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.io.IOUtils", e);
        }
    }
}

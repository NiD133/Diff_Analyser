package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest17 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer();
        // Undeclared exception!
        try {
            circularByteBuffer0.peek((byte[]) null, 1254, 1254);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // Buffer
            //
            verifyException("java.util.Objects", e);
        }
    }
}

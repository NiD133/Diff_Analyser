package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest42 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test41() throws Throwable {
        byte[] byteArray0 = new byte[22];
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer(2);
        // Undeclared exception!
        try {
            circularByteBuffer0.add(byteArray0, 2, 8192);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // No space available
            //
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }
}
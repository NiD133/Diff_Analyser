package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest25 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        byte[] byteArray0 = new byte[22];
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer(2);
        // Undeclared exception!
        try {
            circularByteBuffer0.read(byteArray0, 2, 2);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Currently, there are only 0in the buffer, not 2
            //
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }
}

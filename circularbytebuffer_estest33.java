package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest33 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        byte[] byteArray0 = new byte[4];
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer();
        // Undeclared exception!
        try {
            circularByteBuffer0.peek(byteArray0, 7, 7);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Illegal offset: 7
            //
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }
}

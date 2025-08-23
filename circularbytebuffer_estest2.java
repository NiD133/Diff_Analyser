package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest2 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer();
        byte[] byteArray0 = new byte[4];
        // Undeclared exception!
        try {
            circularByteBuffer0.read(byteArray0, (int) (byte) 3, 1);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Currently, there are only 0in the buffer, not 1
            //
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }
}

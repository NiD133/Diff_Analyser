package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest45 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test44() throws Throwable {
        byte[] byteArray0 = new byte[16];
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer();
        // Undeclared exception!
        try {
            circularByteBuffer0.add(byteArray0, (int) (byte) (-4), (int) (byte) (-4));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Illegal offset: -4
            //
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }
}

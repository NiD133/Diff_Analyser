package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest27 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer();
        // Undeclared exception!
        try {
            circularByteBuffer0.read();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // No bytes available.
            //
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }
}

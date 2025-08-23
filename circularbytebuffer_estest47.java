package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest47 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test46() throws Throwable {
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer((byte) 0);
        // Undeclared exception!
        try {
            circularByteBuffer0.add((byte) 0);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // No space available
            //
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }
}

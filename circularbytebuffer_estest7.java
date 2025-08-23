package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest7 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer();
        byte[] byteArray0 = new byte[2];
        circularByteBuffer0.add((byte) 0);
        circularByteBuffer0.read();
        boolean boolean0 = circularByteBuffer0.peek(byteArray0, (byte) 0, 1);
        assertFalse(circularByteBuffer0.hasBytes());
        assertTrue(boolean0);
    }
}

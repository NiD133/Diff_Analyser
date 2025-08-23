package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest28 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        byte[] byteArray0 = new byte[16];
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer(2);
        boolean boolean0 = circularByteBuffer0.peek(byteArray0, 2, 2);
        assertEquals(2, circularByteBuffer0.getSpace());
        assertTrue(boolean0);
    }
}

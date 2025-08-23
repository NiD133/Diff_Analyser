package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest5 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer();
        circularByteBuffer0.add((byte) 0);
        circularByteBuffer0.add((byte) 0);
        byte byte0 = circularByteBuffer0.read();
        assertTrue(circularByteBuffer0.hasBytes());
        assertEquals((byte) 0, byte0);
    }
}

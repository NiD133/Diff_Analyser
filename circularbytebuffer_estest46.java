package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest46 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test45() throws Throwable {
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer((byte) 1);
        circularByteBuffer0.add((byte) 1);
        assertEquals(0, circularByteBuffer0.getSpace());
        byte byte0 = circularByteBuffer0.read();
        assertEquals(1, circularByteBuffer0.getSpace());
        assertEquals((byte) 1, byte0);
    }
}

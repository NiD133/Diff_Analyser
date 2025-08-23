package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest48 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test47() throws Throwable {
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer();
        circularByteBuffer0.add((byte) 0);
        boolean boolean0 = circularByteBuffer0.hasBytes();
        assertEquals(1, circularByteBuffer0.getCurrentNumberOfBytes());
        assertTrue(boolean0);
    }
}

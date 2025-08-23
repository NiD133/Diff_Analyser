package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest10 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer();
        circularByteBuffer0.add((byte) 0);
        int int0 = circularByteBuffer0.getSpace();
        assertEquals(1, circularByteBuffer0.getCurrentNumberOfBytes());
        assertEquals(8191, int0);
    }
}

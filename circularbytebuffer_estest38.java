package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest38 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test37() throws Throwable {
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer();
        boolean boolean0 = circularByteBuffer0.hasSpace();
        assertEquals(8192, circularByteBuffer0.getSpace());
        assertTrue(boolean0);
    }
}

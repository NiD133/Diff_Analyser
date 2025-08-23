package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest49 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test48() throws Throwable {
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer((byte) 0);
        int int0 = circularByteBuffer0.getCurrentNumberOfBytes();
        assertFalse(circularByteBuffer0.hasSpace());
        assertEquals(0, int0);
    }
}

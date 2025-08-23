package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest36 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test35() throws Throwable {
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer(0);
        boolean boolean0 = circularByteBuffer0.hasSpace(0);
        assertTrue(boolean0);
        assertFalse(circularByteBuffer0.hasSpace());
    }
}

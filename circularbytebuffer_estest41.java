package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest41 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        byte[] byteArray0 = new byte[22];
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer(2);
        circularByteBuffer0.add(byteArray0, 2, 2);
        assertFalse(circularByteBuffer0.hasSpace());
        circularByteBuffer0.read(byteArray0, 2, 2);
        assertEquals(0, circularByteBuffer0.getCurrentNumberOfBytes());
    }
}

package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest50 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test49() throws Throwable {
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer();
        circularByteBuffer0.clear();
        assertEquals(8192, circularByteBuffer0.getSpace());
    }
}

package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CircularByteBuffer_ESTestTest19 extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        CircularByteBuffer circularByteBuffer0 = new CircularByteBuffer(978);
        // Undeclared exception!
        try {
            circularByteBuffer0.add((byte[]) null, 978, 978);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // Buffer
            //
            verifyException("java.util.Objects", e);
        }
    }
}

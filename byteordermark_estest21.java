package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteOrderMark_ESTestTest21 extends ByteOrderMark_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        int[] intArray0 = new int[3];
        ByteOrderMark byteOrderMark0 = null;
        try {
            byteOrderMark0 = new ByteOrderMark("", intArray0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // No charsetName specified
            //
            verifyException("org.apache.commons.io.ByteOrderMark", e);
        }
    }
}
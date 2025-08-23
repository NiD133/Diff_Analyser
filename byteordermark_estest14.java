package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteOrderMark_ESTestTest14 extends ByteOrderMark_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        int[] intArray0 = new int[1];
        ByteOrderMark byteOrderMark0 = new ByteOrderMark("eS5", intArray0);
        int[] intArray1 = byteOrderMark0.getRawBytes();
        boolean boolean0 = byteOrderMark0.matches(intArray1);
        assertTrue(boolean0);
    }
}

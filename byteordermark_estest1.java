package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteOrderMark_ESTestTest1 extends ByteOrderMark_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_16BE;
        int[] intArray0 = new int[8];
        intArray0[0] = (int) '\uFEFF';
        boolean boolean0 = byteOrderMark0.matches(intArray0);
        assertFalse(boolean0);
    }
}

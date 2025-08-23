package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteOrderMark_ESTestTest18 extends ByteOrderMark_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_32LE;
        int[] intArray0 = new int[7];
        ByteOrderMark byteOrderMark1 = new ByteOrderMark("str", intArray0);
        boolean boolean0 = byteOrderMark0.equals(byteOrderMark1);
        assertFalse(boolean0);
    }
}

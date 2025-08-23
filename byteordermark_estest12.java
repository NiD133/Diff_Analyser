package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteOrderMark_ESTestTest12 extends ByteOrderMark_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_8;
        int[] intArray0 = new int[1];
        boolean boolean0 = byteOrderMark0.matches(intArray0);
        assertFalse(boolean0);
    }
}

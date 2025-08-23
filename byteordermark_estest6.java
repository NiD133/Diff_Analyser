package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteOrderMark_ESTestTest6 extends ByteOrderMark_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        int[] intArray0 = new int[4];
        intArray0[2] = (-127);
        ByteOrderMark byteOrderMark0 = new ByteOrderMark("@P", intArray0);
        int int0 = byteOrderMark0.get(2);
        assertEquals((-127), int0);
    }
}

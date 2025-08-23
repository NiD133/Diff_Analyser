package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteOrderMark_ESTestTest24 extends ByteOrderMark_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        int[] intArray0 = new int[8];
        ByteOrderMark byteOrderMark0 = new ByteOrderMark("U", intArray0);
        int int0 = byteOrderMark0.get(3);
        assertEquals(0, int0);
    }
}

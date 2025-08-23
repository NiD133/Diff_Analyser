package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteOrderMark_ESTestTest19 extends ByteOrderMark_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        int[] intArray0 = new int[6];
        ByteOrderMark byteOrderMark0 = new ByteOrderMark("Bt^D2-fqe2We[-^J#", intArray0);
        Object object0 = new Object();
        boolean boolean0 = byteOrderMark0.equals(object0);
        assertFalse(boolean0);
    }
}

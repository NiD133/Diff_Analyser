package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteOrderMark_ESTestTest10 extends ByteOrderMark_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        int[] intArray0 = new int[6];
        ByteOrderMark byteOrderMark0 = new ByteOrderMark("Bt^D2-fqe2We[-^J#", intArray0);
        String string0 = byteOrderMark0.toString();
        assertEquals("ByteOrderMark[Bt^D2-fqe2We[-^J#: 0x0,0x0,0x0,0x0,0x0,0x0]", string0);
    }
}

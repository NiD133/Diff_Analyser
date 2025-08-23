package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SegmentUtils_ESTestTest22 extends SegmentUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        long[][] longArray0 = new long[6][0];
        long[] longArray1 = new long[2];
        longArray0[0] = longArray1;
        int int0 = SegmentUtils.countBit16(longArray0);
        assertEquals(0, int0);
    }
}

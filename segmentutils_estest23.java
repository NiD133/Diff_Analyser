package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SegmentUtils_ESTestTest23 extends SegmentUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        long[] longArray0 = new long[5];
        longArray0[1] = (long) (-1653);
        int int0 = SegmentUtils.countBit16(longArray0);
        assertEquals(1, int0);
    }
}

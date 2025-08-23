package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SegmentUtils_ESTestTest2 extends SegmentUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        int int0 = SegmentUtils.countArgs("Can't read beyond end of stream (n = %,d, count = %,d, maxLength = %,d, remaining  %,d)", 103);
        assertEquals(26, int0);
    }
}

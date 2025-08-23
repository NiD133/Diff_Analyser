package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SegmentUtils_ESTestTest18 extends SegmentUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        long[][] longArray0 = new long[0][5];
        int int0 = SegmentUtils.countMatches(longArray0, (IMatcher) null);
        assertEquals(0, int0);
    }
}
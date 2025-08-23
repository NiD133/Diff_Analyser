package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SegmentUtils_ESTestTest28 extends SegmentUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        int int0 = SegmentUtils.countArgs("&L(LkEf;|)7g<");
        assertEquals(2, int0);
    }
}

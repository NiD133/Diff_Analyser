package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SegmentUtils_ESTestTest27 extends SegmentUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        int int0 = SegmentUtils.countInvokeInterfaceArgs("(X=K[}a)\"3/[Ns");
        assertEquals(5, int0);
    }
}
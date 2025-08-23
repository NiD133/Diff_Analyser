package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MurmurHash2_ESTestTest10 extends MurmurHash2_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        int int0 = MurmurHash2.hash32((byte[]) null, 0);
        assertEquals(275646681, int0);
    }
}

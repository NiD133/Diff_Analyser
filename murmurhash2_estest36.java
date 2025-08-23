package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MurmurHash2_ESTestTest36 extends MurmurHash2_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test35() throws Throwable {
        byte[] byteArray0 = new byte[0];
        int int0 = MurmurHash2.hash32(byteArray0, (-1819289676));
        assertEquals((-563837603), int0);
    }
}

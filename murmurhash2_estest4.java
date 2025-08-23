package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MurmurHash2_ESTestTest4 extends MurmurHash2_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        byte[] byteArray0 = new byte[2];
        long long0 = MurmurHash2.hash64(byteArray0, (int) (byte) 0, (int) (byte) 0);
        assertEquals(0L, long0);
    }
}

package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MurmurHash2_ESTestTest20 extends MurmurHash2_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        byte[] byteArray0 = new byte[6];
        long long0 = MurmurHash2.hash64(byteArray0, 5, (int) (byte) 56);
        assertEquals((-3113210640657759650L), long0);
    }
}

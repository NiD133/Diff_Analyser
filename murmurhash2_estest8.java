package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MurmurHash2_ESTestTest8 extends MurmurHash2_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        byte[] byteArray0 = new byte[6];
        byteArray0[0] = (byte) 18;
        int int0 = MurmurHash2.hash32(byteArray0, 2, (-3970));
        assertEquals((-1628438012), int0);
    }
}

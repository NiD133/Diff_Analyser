package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MurmurHash2_ESTestTest28 extends MurmurHash2_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        byte[] byteArray0 = new byte[2];
        int int0 = MurmurHash2.hash32(byteArray0, (-1564), (int) (byte) 0);
        assertEquals(1307949917, int0);
    }
}

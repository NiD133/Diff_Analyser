package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MurmurHash2_ESTestTest33 extends MurmurHash2_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        byte[] byteArray0 = new byte[6];
        long long0 = MurmurHash2.hash64(byteArray0, 0, (int) (byte) (-66));
        assertEquals(2692789288766115115L, long0);
    }
}

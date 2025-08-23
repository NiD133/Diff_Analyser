package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PercentCodec_ESTestTest12 extends PercentCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        byte[] byteArray0 = new byte[5];
        PercentCodec percentCodec0 = new PercentCodec(byteArray0, true);
        byte[] byteArray1 = new byte[9];
        byteArray1[2] = (byte) 22;
        byte[] byteArray2 = percentCodec0.encode(byteArray1);
        assertEquals(25, byteArray2.length);
    }
}

package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PercentCodec_ESTestTest20 extends PercentCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        PercentCodec percentCodec0 = new PercentCodec();
        byte[] byteArray0 = new byte[2];
        byteArray0[0] = (byte) (-114);
        byte[] byteArray1 = percentCodec0.encode(byteArray0);
        assertArrayEquals(new byte[] { (byte) 37, (byte) 56, (byte) 69, (byte) 0 }, byteArray1);
    }
}

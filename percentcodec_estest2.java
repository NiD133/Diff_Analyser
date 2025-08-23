package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PercentCodec_ESTestTest2 extends PercentCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        byte[] byteArray0 = new byte[5];
        byteArray0[3] = (byte) 70;
        PercentCodec percentCodec0 = new PercentCodec(byteArray0, true);
        byte[] byteArray1 = percentCodec0.decode(byteArray0);
        assertArrayEquals(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 70, (byte) 0 }, byteArray1);
    }
}
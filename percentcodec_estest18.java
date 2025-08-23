package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PercentCodec_ESTestTest18 extends PercentCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        byte[] byteArray0 = new byte[1];
        byteArray0[0] = (byte) 43;
        PercentCodec percentCodec0 = new PercentCodec(byteArray0, true);
        byte[] byteArray1 = percentCodec0.decode(byteArray0);
        byte[] byteArray2 = percentCodec0.encode(byteArray1);
        assertArrayEquals(new byte[] { (byte) 43 }, byteArray2);
    }
}

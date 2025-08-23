package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PercentCodec_ESTestTest4 extends PercentCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        byte[] byteArray0 = new byte[5];
        byteArray0[3] = (byte) 70;
        byte[] byteArray1 = new byte[0];
        PercentCodec percentCodec0 = new PercentCodec(byteArray1, true);
        byte[] byteArray2 = percentCodec0.encode(byteArray0);
        assertSame(byteArray2, byteArray0);
    }
}

package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PercentCodec_ESTestTest7 extends PercentCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        byte[] byteArray0 = new byte[5];
        PercentCodec percentCodec0 = new PercentCodec(byteArray0, true);
        byte[] byteArray1 = new byte[7];
        byteArray1[6] = (byte) 63;
        byte[] byteArray2 = percentCodec0.encode(byteArray1);
        assertEquals(19, byteArray2.length);
    }
}

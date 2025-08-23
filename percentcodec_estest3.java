package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PercentCodec_ESTestTest3 extends PercentCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        byte[] byteArray0 = new byte[5];
        PercentCodec percentCodec0 = new PercentCodec(byteArray0, true);
        byte[] byteArray1 = new byte[9];
        byteArray1[0] = (byte) (-19);
        byte[] byteArray2 = percentCodec0.encode(byteArray1);
        byte[] byteArray3 = percentCodec0.decode(byteArray2);
        assertEquals(9, byteArray3.length);
        assertEquals(27, byteArray2.length);
        assertArrayEquals(new byte[] { (byte) (-19), (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }, byteArray3);
    }
}

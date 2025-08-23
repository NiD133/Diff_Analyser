package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BinaryCodec_ESTestTest6 extends BinaryCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        BinaryCodec binaryCodec0 = new BinaryCodec();
        byte[] byteArray0 = new byte[8];
        byte[] byteArray1 = binaryCodec0.decode(byteArray0);
        assertArrayEquals(new byte[] { (byte) 0 }, byteArray1);
    }
}

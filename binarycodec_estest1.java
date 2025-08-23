package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BinaryCodec_ESTestTest1 extends BinaryCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        byte[] byteArray0 = new byte[9];
        byteArray0[1] = (byte) 102;
        byte[] byteArray1 = BinaryCodec.fromAscii(byteArray0);
        assertArrayEquals(new byte[] { (byte) 0 }, byteArray1);
    }
}

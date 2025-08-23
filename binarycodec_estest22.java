package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BinaryCodec_ESTestTest22 extends BinaryCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        byte[] byteArray0 = new byte[3];
        byteArray0[2] = (byte) 48;
        byte[] byteArray1 = BinaryCodec.toAsciiBytes(byteArray0);
        byte[] byteArray2 = BinaryCodec.fromAscii(byteArray1);
        assertArrayEquals(new byte[] { (byte) 0, (byte) 0, (byte) 48 }, byteArray2);
    }
}

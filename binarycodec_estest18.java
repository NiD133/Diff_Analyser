package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BinaryCodec_ESTestTest18 extends BinaryCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        byte[] byteArray0 = new byte[1];
        byteArray0[0] = (byte) 64;
        char[] charArray0 = BinaryCodec.toAsciiChars(byteArray0);
        byte[] byteArray1 = BinaryCodec.fromAscii(charArray0);
        assertArrayEquals(new char[] { '0', '1', '0', '0', '0', '0', '0', '0' }, charArray0);
        assertArrayEquals(new byte[] { (byte) 64 }, byteArray1);
    }
}

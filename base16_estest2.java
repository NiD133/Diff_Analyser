package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Base16_ESTestTest2 extends Base16_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        CodecPolicy codecPolicy0 = CodecPolicy.STRICT;
        Base16 base16_0 = new Base16(false, codecPolicy0);
        byte[] byteArray0 = new byte[3];
        byteArray0[1] = (byte) (-23);
        byte[] byteArray1 = base16_0.encode(byteArray0);
        assertArrayEquals(new byte[] { (byte) 48, (byte) 48, (byte) 69, (byte) 57, (byte) 48, (byte) 48 }, byteArray1);
    }
}

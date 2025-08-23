package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Base16_ESTestTest4 extends Base16_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        Base16 base16_0 = new Base16();
        byte[] byteArray0 = new byte[4];
        BaseNCodec.Context baseNCodec_Context0 = new BaseNCodec.Context();
        base16_0.encode(byteArray0, 0, (int) (byte) 0, baseNCodec_Context0);
        assertArrayEquals(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0 }, byteArray0);
    }
}

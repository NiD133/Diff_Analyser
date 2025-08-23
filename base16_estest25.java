package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Base16_ESTestTest25 extends Base16_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        Base16 base16_0 = new Base16();
        byte[] byteArray0 = new byte[10];
        BaseNCodec.Context baseNCodec_Context0 = new BaseNCodec.Context();
        base16_0.decode(byteArray0, (-1800), (-1800), baseNCodec_Context0);
        base16_0.decode(byteArray0, (-1800), (-1800), baseNCodec_Context0);
        assertArrayEquals(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }, byteArray0);
    }
}

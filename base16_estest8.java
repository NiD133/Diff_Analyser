package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Base16_ESTestTest8 extends Base16_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        CodecPolicy codecPolicy0 = CodecPolicy.STRICT;
        Base16 base16_0 = new Base16(false, codecPolicy0);
        byte[] byteArray0 = new byte[3];
        BaseNCodec.Context baseNCodec_Context0 = new BaseNCodec.Context();
        baseNCodec_Context0.ibitWorkArea = (int) (byte) (-23);
        // Undeclared exception!
        try {
            base16_0.decode(byteArray0, 4, 64, baseNCodec_Context0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 4
            //
            verifyException("org.apache.commons.codec.binary.Base16", e);
        }
    }
}

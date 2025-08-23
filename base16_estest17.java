package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Base16_ESTestTest17 extends Base16_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        CodecPolicy codecPolicy0 = CodecPolicy.LENIENT;
        Base16 base16_0 = new Base16(true, codecPolicy0);
        byte[] byteArray0 = new byte[1];
        BaseNCodec.Context baseNCodec_Context0 = new BaseNCodec.Context();
        baseNCodec_Context0.ibitWorkArea = 76;
        // Undeclared exception!
        try {
            base16_0.decode(byteArray0, (int) (byte) 1, (int) (byte) 1, baseNCodec_Context0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 1
            //
            verifyException("org.apache.commons.codec.binary.Base16", e);
        }
    }
}

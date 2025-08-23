package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Base16_ESTestTest15 extends Base16_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        byte[] byteArray0 = new byte[6];
        Base16 base16_0 = new Base16();
        BaseNCodec.Context baseNCodec_Context0 = new BaseNCodec.Context();
        // Undeclared exception!
        try {
            base16_0.encode(byteArray0, 76, 76, baseNCodec_Context0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 76
            //
            verifyException("org.apache.commons.codec.binary.Base16", e);
        }
    }
}

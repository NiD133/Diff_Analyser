package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Base16_ESTestTest7 extends Base16_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Base16 base16_0 = new Base16();
        byte[] byteArray0 = new byte[7];
        BaseNCodec.Context baseNCodec_Context0 = new BaseNCodec.Context();
        // Undeclared exception!
        try {
            base16_0.decode(byteArray0, (int) (byte) 6, (int) (byte) 6, baseNCodec_Context0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Invalid octet in encoded value: 0
            //
            verifyException("org.apache.commons.codec.binary.Base16", e);
        }
    }
}

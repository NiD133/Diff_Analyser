package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Base16_ESTestTest14 extends Base16_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        Base16 base16_0 = new Base16();
        byte[] byteArray0 = new byte[3];
        // Undeclared exception!
        try {
            base16_0.encode(byteArray0, 76, (int) (byte) 0, (BaseNCodec.Context) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.codec.binary.Base16", e);
        }
    }
}

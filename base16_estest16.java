package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Base16_ESTestTest16 extends Base16_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        Base16 base16_0 = new Base16();
        BaseNCodec.Context baseNCodec_Context0 = new BaseNCodec.Context();
        // Undeclared exception!
        try {
            base16_0.decode((byte[]) null, 64, 64, baseNCodec_Context0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.codec.binary.Base16", e);
        }
    }
}

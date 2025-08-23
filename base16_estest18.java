package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Base16_ESTestTest18 extends Base16_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        Base16 base16_0 = null;
        try {
            base16_0 = new Base16(true, (CodecPolicy) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // codecPolicy
            //
            verifyException("java.util.Objects", e);
        }
    }
}

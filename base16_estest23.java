package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Base16_ESTestTest23 extends Base16_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        Base16 base16_0 = new Base16();
        // Undeclared exception!
        try {
            base16_0.decode("BDT");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Invalid octet in encoded value: 84
            //
            verifyException("org.apache.commons.codec.binary.Base16", e);
        }
    }
}

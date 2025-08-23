package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Base16_ESTestTest19 extends Base16_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        CodecPolicy codecPolicy0 = CodecPolicy.STRICT;
        Base16 base16_0 = new Base16(false, codecPolicy0);
        // Undeclared exception!
        try {
            base16_0.decode("F");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Strict decoding: Last encoded character is a valid base 16 alphabet character but not a possible encoding. Decoding requires at least two characters to create one byte.
            //
            verifyException("org.apache.commons.codec.binary.Base16", e);
        }
    }
}

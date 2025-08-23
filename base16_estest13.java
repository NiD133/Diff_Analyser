package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Base16_ESTestTest13 extends Base16_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        Base16 base16_0 = new Base16();
        boolean boolean0 = base16_0.isInAlphabet((byte) 92);
        assertFalse(boolean0);
    }
}

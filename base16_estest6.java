package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Base16_ESTestTest6 extends Base16_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Base16 base16_0 = new Base16();
        byte[] byteArray0 = base16_0.decode("21");
        assertArrayEquals(new byte[] { (byte) 33 }, byteArray0);
    }
}

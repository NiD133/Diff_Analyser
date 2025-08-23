package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Base16_ESTestTest22 extends Base16_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        byte[] byteArray0 = new byte[5];
        Base16 base16_0 = new Base16();
        byte[] byteArray1 = base16_0.encode(byteArray0, (-1832), (-1832));
        assertNotSame(byteArray0, byteArray1);
    }
}

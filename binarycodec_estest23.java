package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BinaryCodec_ESTestTest23 extends BinaryCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        byte[] byteArray0 = new byte[0];
        byte[] byteArray1 = BinaryCodec.fromAscii(byteArray0);
        assertNotSame(byteArray1, byteArray0);
    }
}

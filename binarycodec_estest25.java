package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BinaryCodec_ESTestTest25 extends BinaryCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        byte[] byteArray0 = new byte[17];
        BinaryCodec binaryCodec0 = new BinaryCodec();
        byte[] byteArray1 = binaryCodec0.encode(byteArray0);
        assertEquals(136, byteArray1.length);
    }
}

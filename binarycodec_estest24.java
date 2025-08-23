package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BinaryCodec_ESTestTest24 extends BinaryCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        byte[] byteArray0 = new byte[1];
        String string0 = BinaryCodec.toAsciiString(byteArray0);
        assertEquals("00000000", string0);
    }
}

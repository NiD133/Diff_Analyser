package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BinaryCodec_ESTestTest20 extends BinaryCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        BinaryCodec binaryCodec0 = new BinaryCodec();
        byte[] byteArray0 = binaryCodec0.toByteArray("lb{1yPz");
        String string0 = BinaryCodec.toAsciiString(byteArray0);
        assertEquals("", string0);
    }
}

package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BinaryCodec_ESTestTest10 extends BinaryCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        byte[] byteArray0 = new byte[17];
        byte[] byteArray1 = BinaryCodec.toAsciiBytes(byteArray0);
        byte[] byteArray2 = BinaryCodec.toAsciiBytes(byteArray1);
        BinaryCodec binaryCodec0 = new BinaryCodec();
        // Undeclared exception!
        binaryCodec0.encode(byteArray2);
    }
}

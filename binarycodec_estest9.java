package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BinaryCodec_ESTestTest9 extends BinaryCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        byte[] byteArray0 = new byte[16];
        byte[] byteArray1 = BinaryCodec.toAsciiBytes(byteArray0);
        byte[] byteArray2 = BinaryCodec.toAsciiBytes(byteArray1);
        // Undeclared exception!
        BinaryCodec.toAsciiBytes(byteArray2);
    }
}

package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BinaryCodec_ESTestTest8 extends BinaryCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        byte[] byteArray0 = new byte[5];
        byte[] byteArray1 = BinaryCodec.toAsciiBytes(byteArray0);
        byte[] byteArray2 = BinaryCodec.toAsciiBytes(byteArray1);
        byte[] byteArray3 = BinaryCodec.toAsciiBytes(byteArray2);
        // Undeclared exception!
        BinaryCodec.toAsciiChars(byteArray3);
    }
}

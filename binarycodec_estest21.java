package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BinaryCodec_ESTestTest21 extends BinaryCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        byte[] byteArray0 = BinaryCodec.fromAscii((char[]) null);
        assertEquals(0, byteArray0.length);
    }
}

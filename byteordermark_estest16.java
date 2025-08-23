package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteOrderMark_ESTestTest16 extends ByteOrderMark_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_32LE;
        byte[] byteArray0 = byteOrderMark0.getBytes();
        assertArrayEquals(new byte[] { (byte) (-1), (byte) (-2), (byte) 0, (byte) 0 }, byteArray0);
    }
}
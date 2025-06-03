package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_8;
        byte[] byteArray0 = byteOrderMark0.getBytes();
        assertArrayEquals(new byte[] { (byte) (-17), (byte) (-69), (byte) (-65) }, byteArray0);
    }
}

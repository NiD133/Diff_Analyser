package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteOrderMark_ESTestTest17 extends ByteOrderMark_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_32LE;
        ByteOrderMark byteOrderMark1 = ByteOrderMark.UTF_32BE;
        boolean boolean0 = byteOrderMark0.equals(byteOrderMark1);
        assertFalse(boolean0);
        assertFalse(byteOrderMark1.equals((Object) byteOrderMark0));
    }
}

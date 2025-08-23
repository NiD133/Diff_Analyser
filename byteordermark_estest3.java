package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteOrderMark_ESTestTest3 extends ByteOrderMark_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_16LE;
        ByteOrderMark byteOrderMark1 = ByteOrderMark.UTF_16BE;
        boolean boolean0 = byteOrderMark1.equals(byteOrderMark0);
        assertFalse(byteOrderMark0.equals((Object) byteOrderMark1));
        assertFalse(boolean0);
    }
}

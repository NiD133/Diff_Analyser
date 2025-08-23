package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteOrderMark_ESTestTest5 extends ByteOrderMark_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_32BE;
        int int0 = byteOrderMark0.get(3);
        assertEquals(255, int0);
    }
}

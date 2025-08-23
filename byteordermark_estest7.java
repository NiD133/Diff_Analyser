package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteOrderMark_ESTestTest7 extends ByteOrderMark_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_8;
        // Undeclared exception!
        try {
            byteOrderMark0.get((-5));
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // -5
            //
            verifyException("org.apache.commons.io.ByteOrderMark", e);
        }
    }
}

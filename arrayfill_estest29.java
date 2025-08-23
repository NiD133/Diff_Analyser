package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Arrays;
import org.apache.commons.lang3.function.FailableIntFunction;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

public class ArrayFill_ESTestTest29 extends ArrayFill_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        byte[] byteArray0 = new byte[0];
        byte[] byteArray1 = ArrayFill.fill(byteArray0, (byte) 0);
        assertSame(byteArray0, byteArray1);
    }
}

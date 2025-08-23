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

public class ArrayFill_ESTestTest20 extends ArrayFill_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        long[] longArray0 = ArrayFill.fill((long[]) null, 1003L);
        assertNull(longArray0);
    }
}

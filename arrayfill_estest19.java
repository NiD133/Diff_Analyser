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

public class ArrayFill_ESTestTest19 extends ArrayFill_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        long[] longArray0 = new long[8];
        long[] longArray1 = ArrayFill.fill(longArray0, (-1L));
        assertArrayEquals(new long[] { (-1L), (-1L), (-1L), (-1L), (-1L), (-1L), (-1L), (-1L) }, longArray1);
    }
}

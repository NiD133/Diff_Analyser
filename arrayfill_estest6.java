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

public class ArrayFill_ESTestTest6 extends ArrayFill_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        int[] intArray0 = new int[7];
        int[] intArray1 = ArrayFill.fill(intArray0, (-1));
        assertArrayEquals(new int[] { (-1), (-1), (-1), (-1), (-1), (-1), (-1) }, intArray1);
    }
}

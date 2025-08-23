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

public class ArrayFill_ESTestTest21 extends ArrayFill_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        int[] intArray0 = new int[0];
        int[] intArray1 = ArrayFill.fill(intArray0, 0);
        assertSame(intArray1, intArray0);
    }
}

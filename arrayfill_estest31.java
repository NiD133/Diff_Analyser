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

public class ArrayFill_ESTestTest31 extends ArrayFill_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        boolean[] booleanArray0 = new boolean[4];
        boolean[] booleanArray1 = ArrayFill.fill(booleanArray0, true);
        assertTrue(Arrays.equals(new boolean[] { true, true, true, true }, booleanArray1));
    }
}

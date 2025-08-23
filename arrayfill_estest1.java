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

public class ArrayFill_ESTestTest1 extends ArrayFill_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        boolean[] booleanArray0 = new boolean[0];
        boolean[] booleanArray1 = ArrayFill.fill(booleanArray0, true);
        assertSame(booleanArray0, booleanArray1);
    }
}

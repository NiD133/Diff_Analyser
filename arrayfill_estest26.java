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

public class ArrayFill_ESTestTest26 extends ArrayFill_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        double[] doubleArray0 = ArrayFill.fill((double[]) null, (double) (short) (-3333));
        assertNull(doubleArray0);
    }
}

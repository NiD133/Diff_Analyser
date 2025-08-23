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

public class ArrayFill_ESTestTest14 extends ArrayFill_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        Object[] objectArray0 = new Object[4];
        Object[] objectArray1 = ArrayFill.fill(objectArray0, (FailableIntFunction<?, Throwable>) null);
        assertSame(objectArray0, objectArray1);
    }
}

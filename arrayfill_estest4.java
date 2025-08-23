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

public class ArrayFill_ESTestTest4 extends ArrayFill_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        Throwable[] throwableArray0 = new Throwable[8];
        MockThrowable mockThrowable0 = new MockThrowable();
        Throwable[] throwableArray1 = ArrayFill.fill(throwableArray0, (Throwable) mockThrowable0);
        assertSame(throwableArray1, throwableArray0);
    }
}

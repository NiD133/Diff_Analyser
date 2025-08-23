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

public class ArrayFill_ESTestTest32 extends ArrayFill_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        boolean[] booleanArray0 = ArrayFill.fill((boolean[]) null, true);
        assertNull(booleanArray0);
    }
}

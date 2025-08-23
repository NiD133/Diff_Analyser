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

public class ArrayFill_ESTestTest16 extends ArrayFill_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        FailableIntFunction<Throwable, Throwable> failableIntFunction0 = FailableIntFunction.nop();
        Throwable[] throwableArray0 = ArrayFill.fill((Throwable[]) null, (FailableIntFunction<? extends Throwable, Throwable>) failableIntFunction0);
        assertNull(throwableArray0);
    }
}

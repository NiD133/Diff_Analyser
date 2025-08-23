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

public class ArrayFill_ESTestTest11 extends ArrayFill_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        Throwable[] throwableArray0 = new Throwable[3];
        FailableIntFunction<Object, Throwable> failableIntFunction0 = FailableIntFunction.nop();
        // Undeclared exception!
        try {
            ArrayFill.fill((Object[]) throwableArray0, (Object) failableIntFunction0);
            fail("Expecting exception: ArrayStoreException");
        } catch (ArrayStoreException e) {
            //
            // org.apache.commons.lang3.function.FailableIntFunction$$Lambda$153/1790855301
            //
            verifyException("java.util.Arrays", e);
        }
    }
}

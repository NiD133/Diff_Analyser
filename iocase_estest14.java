package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IOCase_ESTestTest14 extends IOCase_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        IOCase iOCase0 = IOCase.SENSITIVE;
        // Undeclared exception!
        try {
            iOCase0.checkCompareTo((String) null, (String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // str1
            //
            verifyException("java.util.Objects", e);
        }
    }
}

package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IOCase_ESTestTest12 extends IOCase_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        // Undeclared exception!
        try {
            IOCase.forName("7VKlZdfe6fjn*5");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Illegal IOCase name: 7VKlZdfe6fjn*5
            //
            verifyException("org.apache.commons.io.IOCase", e);
        }
    }
}

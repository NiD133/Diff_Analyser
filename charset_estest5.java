package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSet_ESTestTest5 extends CharSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        CharSet charSet0 = null;
        try {
            charSet0 = new CharSet((String[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.Arrays", e);
        }
    }
}

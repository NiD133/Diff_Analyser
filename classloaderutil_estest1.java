package org.apache.commons.jxpath.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ClassLoaderUtil_ESTestTest1 extends ClassLoaderUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
        // Undeclared exception!
        try {
            ClassLoaderUtil.getClass((String) null, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // className
            //
            verifyException("java.util.Objects", e);
        }
    }
}

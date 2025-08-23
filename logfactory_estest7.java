package org.apache.ibatis.logging;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.ibatis.logging.commons.JakartaCommonsLoggingImpl;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LogFactory_ESTestTest7 extends LogFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        // Undeclared exception!
        try {
            LogFactory.getLog((String) null);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Error creating logger for logger null.  Cause: java.lang.reflect.InvocationTargetException
            //
            verifyException("org.apache.ibatis.logging.LogFactory", e);
        }
    }
}

package org.apache.ibatis.logging;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.ibatis.logging.commons.JakartaCommonsLoggingImpl;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LogFactory_ESTestTest10 extends LogFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        // Undeclared exception!
        try {
            LogFactory.useLog4JLogging();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Error setting Log implementation.  Cause: java.lang.NoClassDefFoundError: org/apache/log4j/Priority
            //
            verifyException("org.apache.ibatis.logging.LogFactory", e);
        }
    }
}

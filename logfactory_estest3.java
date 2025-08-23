package org.apache.ibatis.logging;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.ibatis.logging.commons.JakartaCommonsLoggingImpl;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LogFactory_ESTestTest3 extends LogFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Class<Object> class0 = Object.class;
        Log log0 = LogFactory.getLog(class0);
        assertNotNull(log0);
    }
}

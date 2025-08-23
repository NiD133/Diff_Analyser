package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.File;
import java.util.Date;
import java.util.Map;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

public class TypeHandler_ESTestTest15 extends TypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        // Undeclared exception!
        try {
            TypeHandler.createDate("w2E%~v5+#");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // org.evosuite.runtime.mock.java.lang.MockThrowable: java.text.ParseException: Unparseable date: \"w2E%~v5+#\"
            //
            verifyException("org.apache.commons.cli.TypeHandler", e);
        }
    }
}

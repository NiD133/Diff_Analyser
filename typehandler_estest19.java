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

public class TypeHandler_ESTestTest19 extends TypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        try {
            TypeHandler.openFile("EEE MMM dd HH:mm:ss zzz yyyy");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            //
            // java.io.FileNotFoundException: EEE MMM dd HH:mm:ss zzz yyyy (No such file or directory)
            //
            verifyException("org.apache.commons.cli.ParseException", e);
        }
    }
}

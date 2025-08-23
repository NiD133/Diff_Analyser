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

public class TypeHandler_ESTestTest12 extends TypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        try {
            TypeHandler.createClass("converterMap");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            //
            // java.lang.ClassNotFoundException: Class 'converterMap.class' should be in target project, but could not be found!
            //
            verifyException("org.apache.commons.cli.ParseException", e);
        }
    }
}
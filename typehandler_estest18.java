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

public class TypeHandler_ESTestTest18 extends TypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        // Undeclared exception!
        try {
            TypeHandler.createFiles("org.apache.commons.cli.Converter");
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // Not yet implemented
            //
            verifyException("org.apache.commons.cli.TypeHandler", e);
        }
    }
}

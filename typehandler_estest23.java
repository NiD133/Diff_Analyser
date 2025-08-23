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

public class TypeHandler_ESTestTest23 extends TypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        Class<?> class0 = TypeHandler.createClass("org.apache.commons.cli.Converter");
        assertEquals(1537, class0.getModifiers());
    }
}

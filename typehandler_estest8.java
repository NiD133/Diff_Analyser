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

public class TypeHandler_ESTestTest8 extends TypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Class<Date> class0 = Date.class;
        try {
            TypeHandler.createValue("converterMap", (Object) class0);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            //
            // java.text.ParseException: Unparseable date: \"converterMap\"
            //
            verifyException("org.apache.commons.cli.ParseException", e);
        }
    }
}
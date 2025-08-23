package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.math.BigDecimal;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BigDecimalParser_ESTestTest17 extends BigDecimalParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        // Undeclared exception!
        try {
            BigDecimalParser.parseWithFastParser("");
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            //
            // ch/randelshofer/fastdoubleparser/JavaBigDecimalParser
            //
            verifyException("com.fasterxml.jackson.core.io.BigDecimalParser", e);
        }
    }
}

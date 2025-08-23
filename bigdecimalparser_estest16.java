package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.math.BigDecimal;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BigDecimalParser_ESTestTest16 extends BigDecimalParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        char[] charArray0 = new char[6];
        // Undeclared exception!
        try {
            BigDecimalParser.parseWithFastParser(charArray0, 1754, (-2552));
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            //
            // ch/randelshofer/fastdoubleparser/JavaBigDecimalParser
            //
            verifyException("com.fasterxml.jackson.core.io.BigDecimalParser", e);
        }
    }
}

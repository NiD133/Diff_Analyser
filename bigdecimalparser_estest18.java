package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.math.BigDecimal;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BigDecimalParser_ESTestTest18 extends BigDecimalParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        char[] charArray0 = new char[3];
        // Undeclared exception!
        try {
            BigDecimalParser.parse(charArray0);
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            //
            // Value \"\u0000\u0000\u0000\" can not be deserialized as `java.math.BigDecimal`, reason:  Not a valid number representation
            //
            verifyException("com.fasterxml.jackson.core.io.BigDecimalParser", e);
        }
    }
}

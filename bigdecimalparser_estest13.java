package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.math.BigDecimal;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BigDecimalParser_ESTestTest13 extends BigDecimalParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        // Undeclared exception!
        try {
            BigDecimalParser.parse("eA8ojpN");
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            //
            // Value \"eA8ojpN\" can not be deserialized as `java.math.BigDecimal`, reason:  Not a valid number representation
            //
            verifyException("com.fasterxml.jackson.core.io.BigDecimalParser", e);
        }
    }
}

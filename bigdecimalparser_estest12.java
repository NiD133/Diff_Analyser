package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.math.BigDecimal;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BigDecimalParser_ESTestTest12 extends BigDecimalParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        char[] charArray0 = new char[0];
        // Undeclared exception!
        try {
            BigDecimalParser.parse(charArray0, 265, 265);
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
        }
    }
}

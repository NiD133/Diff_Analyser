package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonArray_ESTestTest46 extends JsonArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test45() throws Throwable {
        JsonArray jsonArray0 = new JsonArray();
        jsonArray0.add("");
        // Undeclared exception!
        try {
            jsonArray0.getAsBigDecimal();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.math.BigDecimal", e);
        }
    }
}

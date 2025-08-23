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

public class JsonArray_ESTestTest44 extends JsonArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test43() throws Throwable {
        JsonArray jsonArray0 = new JsonArray();
        Boolean boolean0 = new Boolean(true);
        jsonArray0.add(boolean0);
        // Undeclared exception!
        try {
            jsonArray0.getAsBigInteger();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            //
            // For input string: \"true\"
            //
            verifyException("java.lang.NumberFormatException", e);
        }
    }
}

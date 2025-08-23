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

public class JsonArray_ESTestTest42 extends JsonArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test41() throws Throwable {
        JsonArray jsonArray0 = new JsonArray();
        jsonArray0.add((Number) null);
        // Undeclared exception!
        try {
            jsonArray0.getAsBoolean();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // JsonNull
            //
            verifyException("com.google.gson.JsonElement", e);
        }
    }
}

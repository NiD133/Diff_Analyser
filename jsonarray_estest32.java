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

public class JsonArray_ESTestTest32 extends JsonArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        JsonArray jsonArray0 = new JsonArray();
        Character character0 = Character.valueOf('~');
        jsonArray0.add(character0);
        // Undeclared exception!
        try {
            jsonArray0.getAsLong();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            //
            // For input string: \"~\"
            //
            verifyException("java.lang.NumberFormatException", e);
        }
    }
}

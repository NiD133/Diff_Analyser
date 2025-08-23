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

public class JsonArray_ESTestTest27 extends JsonArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        JsonArray jsonArray0 = new JsonArray();
        // Undeclared exception!
        try {
            jsonArray0.set(1, (JsonElement) null);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // Index: 1, Size: 0
            //
            verifyException("java.util.ArrayList", e);
        }
    }
}

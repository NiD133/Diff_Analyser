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

public class JsonArray_ESTestTest64 extends JsonArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test63() throws Throwable {
        JsonArray jsonArray0 = new JsonArray();
        Boolean boolean0 = Boolean.valueOf(true);
        jsonArray0.add(boolean0);
        boolean boolean1 = jsonArray0.getAsBoolean();
        assertTrue(boolean1);
    }
}

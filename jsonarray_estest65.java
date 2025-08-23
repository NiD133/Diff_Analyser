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

public class JsonArray_ESTestTest65 extends JsonArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test64() throws Throwable {
        JsonArray jsonArray0 = new JsonArray();
        JsonArray jsonArray1 = jsonArray0.deepCopy();
        boolean boolean0 = jsonArray1.equals(jsonArray0);
        assertTrue(boolean0);
    }
}

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

public class JsonArray_ESTestTest13 extends JsonArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        JsonArray jsonArray0 = new JsonArray();
        Float float0 = new Float(0.0);
        jsonArray0.add((Number) float0);
        float float1 = jsonArray0.getAsFloat();
        assertEquals(0.0F, float1, 0.01F);
    }
}

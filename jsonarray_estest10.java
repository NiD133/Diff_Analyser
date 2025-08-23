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

public class JsonArray_ESTestTest10 extends JsonArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        JsonArray jsonArray0 = new JsonArray();
        Float float0 = new Float(0.0);
        jsonArray0.add((Number) float0);
        long long0 = jsonArray0.getAsLong();
        assertEquals(0L, long0);
    }
}

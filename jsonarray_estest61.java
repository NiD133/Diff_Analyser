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

public class JsonArray_ESTestTest61 extends JsonArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test60() throws Throwable {
        JsonArray jsonArray0 = new JsonArray(846);
        Float float0 = new Float((double) 846);
        jsonArray0.add((Number) float0);
        boolean boolean0 = jsonArray0.remove((JsonElement) jsonArray0);
        assertFalse(jsonArray0.isEmpty());
        assertFalse(boolean0);
    }
}

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

public class JsonArray_ESTestTest51 extends JsonArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test50() throws Throwable {
        JsonArray jsonArray0 = new JsonArray();
        JsonArray jsonArray1 = new JsonArray();
        jsonArray1.add((JsonElement) jsonArray0);
        JsonArray jsonArray2 = jsonArray1.deepCopy();
        boolean boolean0 = jsonArray1.contains(jsonArray2);
        assertFalse(jsonArray1.equals((Object) jsonArray0));
        assertFalse(boolean0);
    }
}

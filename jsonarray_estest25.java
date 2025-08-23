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

public class JsonArray_ESTestTest25 extends JsonArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        JsonArray jsonArray0 = new JsonArray();
        JsonPrimitive jsonPrimitive0 = new JsonPrimitive("");
        jsonArray0.add((JsonElement) jsonPrimitive0);
        JsonElement jsonElement0 = jsonArray0.get(0);
        assertFalse(jsonElement0.isJsonObject());
    }
}

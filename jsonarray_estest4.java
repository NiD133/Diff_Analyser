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

public class JsonArray_ESTestTest4 extends JsonArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        JsonArray jsonArray0 = new JsonArray(846);
        Character character0 = Character.valueOf('\\');
        jsonArray0.add(character0);
        JsonElement jsonElement0 = jsonArray0.remove(0);
        assertFalse(jsonElement0.isJsonNull());
    }
}

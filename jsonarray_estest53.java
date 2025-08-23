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

public class JsonArray_ESTestTest53 extends JsonArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test52() throws Throwable {
        JsonArray jsonArray0 = new JsonArray();
        Character character0 = Character.valueOf('6');
        jsonArray0.add(character0);
        byte byte0 = jsonArray0.getAsByte();
        assertEquals((byte) 6, byte0);
    }
}

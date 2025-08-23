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

public class JsonArray_ESTestTest21 extends JsonArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        JsonArray jsonArray0 = new JsonArray();
        Integer integer0 = new Integer((-3));
        jsonArray0.add((Number) integer0);
        byte byte0 = jsonArray0.getAsByte();
        assertEquals((byte) (-3), byte0);
    }
}

package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.math.BigDecimal;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BigDecimalParser_ESTestTest15 extends BigDecimalParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        BigDecimal bigDecimal0 = BigDecimalParser.parse(".1");
        assertEquals((byte) 0, bigDecimal0.byteValue());
    }
}

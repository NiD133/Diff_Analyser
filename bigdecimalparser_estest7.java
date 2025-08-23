package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.math.BigDecimal;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BigDecimalParser_ESTestTest7 extends BigDecimalParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        BigDecimal bigDecimal0 = BigDecimalParser.parse("7e2");
        assertEquals((short) 700, bigDecimal0.shortValue());
    }
}

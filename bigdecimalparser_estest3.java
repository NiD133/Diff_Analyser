package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.math.BigDecimal;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BigDecimalParser_ESTestTest3 extends BigDecimalParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        char[] charArray0 = new char[2];
        charArray0[0] = '-';
        charArray0[1] = '0';
        BigDecimal bigDecimal0 = BigDecimalParser.parse(charArray0);
        assertEquals((short) 0, bigDecimal0.shortValue());
    }
}

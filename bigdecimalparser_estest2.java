package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.math.BigDecimal;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BigDecimalParser_ESTestTest2 extends BigDecimalParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        char[] charArray0 = new char[9];
        charArray0[1] = '2';
        BigDecimal bigDecimal0 = BigDecimalParser.parse(charArray0, 1, 1);
        assertEquals((short) 2, bigDecimal0.shortValue());
    }
}

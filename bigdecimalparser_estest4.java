package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.math.BigDecimal;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BigDecimalParser_ESTestTest4 extends BigDecimalParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        char[] charArray0 = new char[1];
        charArray0[0] = '4';
        BigDecimal bigDecimal0 = BigDecimalParser.parse(charArray0);
        assertEquals((byte) 4, bigDecimal0.byteValue());
    }
}

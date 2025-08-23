package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class StringUtils_ESTestTest1 extends StringUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        char[] charArray0 = new char[8];
        charArray0[0] = 'h';
        byte[] byteArray0 = StringUtils.convertCharsToBytes(charArray0);
        assertEquals(16, byteArray0.length);
    }
}

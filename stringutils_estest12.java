package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class StringUtils_ESTestTest12 extends StringUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        byte[] byteArray0 = new byte[4];
        byteArray0[1] = (byte) 9;
        ByteBuffer byteBuffer0 = new ByteBuffer(71);
        StringUtils.escapeString(byteArray0, byteBuffer0);
        assertEquals(7, byteBuffer0.size());
    }
}

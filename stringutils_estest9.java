package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class StringUtils_ESTestTest9 extends StringUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        byte[] byteArray0 = new byte[5];
        byteArray0[2] = (byte) 13;
        ByteBuffer byteBuffer0 = new ByteBuffer();
        StringUtils.escapeString(byteArray0, byteBuffer0);
        assertEquals(8, byteBuffer0.size());
    }
}

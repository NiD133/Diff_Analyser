package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class StringUtils_ESTestTest11 extends StringUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        ByteBuffer byteBuffer0 = new ByteBuffer((byte) 10);
        byte[] byteArray0 = new byte[7];
        byteArray0[5] = (byte) 10;
        StringUtils.escapeString(byteArray0, byteBuffer0);
        assertEquals(10, byteBuffer0.size());
    }
}

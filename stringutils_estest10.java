package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class StringUtils_ESTestTest10 extends StringUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        byte[] byteArray0 = new byte[4];
        byteArray0[1] = (byte) 9;
        ByteBuffer byteBuffer0 = new ByteBuffer(71);
        byte[] byteArray1 = StringUtils.escapeString(byteArray0);
        StringUtils.escapeString(byteArray1, byteBuffer0);
        assertEquals(12, byteBuffer0.size());
        assertArrayEquals(new byte[] { (byte) 40, (byte) 0, (byte) 92, (byte) 116, (byte) 0, (byte) 0, (byte) 41 }, byteArray1);
    }
}

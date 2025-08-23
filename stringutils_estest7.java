package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class StringUtils_ESTestTest7 extends StringUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        byte[] byteArray0 = new byte[6];
        byteArray0[4] = (byte) 12;
        ByteBuffer byteBuffer0 = new ByteBuffer((byte) (-35));
        StringUtils.escapeString(byteArray0, byteBuffer0);
        assertEquals(9, byteBuffer0.size());
    }
}

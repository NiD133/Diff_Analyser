package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class StringUtils_ESTestTest4 extends StringUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        byte[] byteArray0 = new byte[0];
        ByteBuffer byteBuffer0 = new ByteBuffer();
        byteBuffer0.count = (-27);
        // Undeclared exception!
        try {
            StringUtils.escapeString(byteArray0, byteBuffer0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // -27
            //
            verifyException("com.itextpdf.text.pdf.ByteBuffer", e);
        }
    }
}

package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest17 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        Vector vector0 = new Vector(0.0F, 0.0F, 0.0F);
        // Undeclared exception!
        try {
            vector0.get(360);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 360
            //
            verifyException("com.itextpdf.text.pdf.parser.Vector", e);
        }
    }
}

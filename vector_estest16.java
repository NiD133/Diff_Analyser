package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest16 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        Vector vector0 = new Vector((-2350.2F), (-2350.2F), (-2350.2F));
        // Undeclared exception!
        try {
            vector0.subtract((Vector) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.itextpdf.text.pdf.parser.Vector", e);
        }
    }
}

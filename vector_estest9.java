package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest9 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        Vector vector0 = new Vector((-1465.0F), (-1465.0F), 1);
        float float0 = vector0.length();
        assertEquals(2071.823F, float0, 0.01F);
    }
}

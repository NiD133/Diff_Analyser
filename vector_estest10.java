package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest10 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        Vector vector0 = new Vector(0.0F, 0.0F, 0.0F);
        Vector vector1 = new Vector((-1465.0F), (-1465.0F), 1);
        Vector vector2 = vector0.subtract(vector1);
        float float0 = vector2.get(0);
        assertEquals(2071.823F, vector2.length(), 0.01F);
        assertEquals(1465.0F, float0, 0.01F);
    }
}

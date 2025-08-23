package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest23 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        Vector vector0 = new Vector(0.0F, 0.0F, 3.3516045F);
        Matrix matrix0 = new Matrix(0.0F, 0);
        Vector vector1 = vector0.cross(matrix0);
        boolean boolean0 = vector0.equals(vector1);
        assertTrue(boolean0);
        assertEquals(11.233253F, vector0.lengthSquared(), 0.01F);
    }
}

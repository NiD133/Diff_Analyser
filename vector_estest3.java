package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest3 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Vector vector0 = new Vector(0.0F, 0.0F, 0.0F);
        Vector vector1 = new Vector((-1465.0F), (-1465.0F), 1);
        Vector vector2 = vector0.subtract(vector1);
        Matrix matrix0 = new Matrix(490.69257F, (-667.658F), (-1.0F), 0.0F, (-1465.0F), 0.0F);
        Vector vector3 = vector2.cross(matrix0);
        Vector vector4 = vector3.multiply(8);
        assertEquals(9710969.0F, vector4.length(), 0.01F);
    }
}

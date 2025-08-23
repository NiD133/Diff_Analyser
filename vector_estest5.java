package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest5 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        Vector vector0 = new Vector(0.0F, 0.0F, 0.0F);
        Vector vector1 = vector0.normalize();
        Matrix matrix0 = new Matrix(135.0858F, 0.0F, 2, 0, 0.0F, (-1.0F));
        Vector vector2 = vector1.cross(matrix0);
        assertTrue(vector2.equals((Object) vector1));
        assertEquals(0.0F, vector0.length(), 0.01F);
    }
}

package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest6 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Vector vector0 = new Vector(0.0F, 0.0F, 0.0F);
        Matrix matrix0 = new Matrix(135.0858F, 0.0F, 2, 0, 0.0F, (-1.0F));
        Vector vector1 = vector0.cross(matrix0);
        assertTrue(vector1.equals((Object) vector0));
    }
}

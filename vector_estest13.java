package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest13 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        Vector vector0 = new Vector(0.0F, 0.0F, (-1.0F));
        Vector vector1 = new Vector(0.0F, (-1.0F), 2);
        float float0 = vector0.dot(vector1);
        assertEquals((-2.0F), float0, 0.01F);
        assertEquals(5.0F, vector1.lengthSquared(), 0.01F);
    }
}

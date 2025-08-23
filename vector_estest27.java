package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest27 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        Vector vector0 = new Vector(0.0F, 0.0F, 3.3516045F);
        Vector vector1 = new Vector(0.0F, 0.0F, 1);
        boolean boolean0 = vector0.equals(vector1);
        assertEquals(1.0F, vector1.length(), 0.01F);
        assertFalse(boolean0);
    }
}

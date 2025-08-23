package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest30 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        Vector vector0 = new Vector((-2905.637F), (-2905.637F), (-1.0F));
        Vector vector1 = vector0.subtract(vector0);
        assertEquals(1.6885452E7F, vector0.lengthSquared(), 0.01F);
        assertEquals(0.0F, vector1.length(), 0.01F);
    }
}

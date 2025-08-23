package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest28 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        Vector vector0 = new Vector((-2905.637F), (-2905.637F), (-1.0F));
        Vector vector1 = vector0.cross(vector0);
        assertEquals(4109.191F, vector0.length(), 0.01F);
        assertEquals(0.0F, vector1.length(), 0.01F);
    }
}

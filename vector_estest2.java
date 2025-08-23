package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest2 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        Vector vector0 = new Vector(0.0F, 0.0F, 0.0F);
        Vector vector1 = new Vector(1103.0195F, 0.0F, 8.0F);
        float float0 = vector0.dot(vector1);
        assertEquals(0.0F, float0, 0.01F);
        assertEquals(1103.0486F, vector1.length(), 0.01F);
    }
}

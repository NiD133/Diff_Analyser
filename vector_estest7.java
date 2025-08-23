package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest7 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Vector vector0 = new Vector(0.0F, 0.0F, 742.77F);
        Vector vector1 = vector0.multiply(0.0F);
        assertEquals(0.0F, vector1.length(), 0.01F);
        assertEquals(742.77F, vector0.length(), 0.01F);
    }
}
